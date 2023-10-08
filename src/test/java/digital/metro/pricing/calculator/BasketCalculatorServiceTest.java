package digital.metro.pricing.calculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BasketCalculatorServiceTest {

    @Mock
    private PriceRepository mockPriceRepository;

    private BasketCalculatorService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        service = new BasketCalculatorService(mockPriceRepository);
    }

    @Test
    public void testCalculateArticle() {
        // GIVEN
        ArticleId articleId = new ArticleId("article-1");
        BigDecimal price = new BigDecimal("34.29");
        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(price);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), null);

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(price);
    }

    @Test
    public void testCalculateArticleForCustomer() {
        // GIVEN
        ArticleId articleId = new ArticleId("article-1");
        BigDecimal standardPrice = new BigDecimal("34.29");
        BigDecimal customerPrice = new BigDecimal("29.99");
        CustomerId customerId = new CustomerId("customer-1");
        BasketEntry basketEntry = new BasketEntry(articleId, BigDecimal.ONE);

        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(standardPrice);
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId(articleId, customerId)).thenReturn(Optional.of(customerPrice));

        // WHEN
        BigDecimal result = service.calculateArticle(basketEntry, customerId);

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(customerPrice);
    }

    @Test
    public void testCalculateBasket() {
        // GIVEN
        CustomerId customerId = new CustomerId("customer-1");
        ArticleId articleId1 = new ArticleId("article-1");
        ArticleId articleId2 = new ArticleId("article-2");
        ArticleId articleId3 = new ArticleId("article-3");
        Basket basket = new Basket(customerId, Set.of(
                new BasketEntry(articleId1, BigDecimal.ONE),
                new BasketEntry(articleId2, BigDecimal.ONE),
                new BasketEntry(articleId3, BigDecimal.ONE)));

        Map<String, BigDecimal> prices = Map.of(
                "article-1", new BigDecimal("1.50"),
                "article-2", new BigDecimal("0.29"),
                "article-3", new BigDecimal("9.99"));

        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId1)).thenReturn(prices.get("article-1"));
        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId2)).thenReturn(prices.get("article-2"));
        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId3)).thenReturn(prices.get("article-3"));

        // WHEN
        BasketCalculationResult result = service.calculateBasket(basket);

        // THEN
        Assertions.assertThat(result.getCustomerId().getId()).isEqualTo("customer-1");
        Assertions.assertThat(transformToMap(result.getPricedBasketEntries())).isEqualTo(prices);
        Assertions.assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("12.79"));
    }

    private Map<String, BigDecimal> transformToMap(Set<BasketEntry> entries) {
        return entries.stream()
                .collect(Collectors.toMap(be -> be.getArticleId().getId(),
                        BasketEntry::getPrice
                ));
    }
}
