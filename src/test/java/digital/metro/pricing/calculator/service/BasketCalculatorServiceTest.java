package digital.metro.pricing.calculator.service;

import digital.metro.pricing.calculator.repository.PriceRepository;
import digital.metro.pricing.calculator.request.Basket;
import digital.metro.pricing.calculator.request.BasketEntry;
import digital.metro.pricing.calculator.response.BasketCalculationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class BasketCalculatorServiceTest {

    private static final String ARTICLE_1 = "article-1";
    private static final String ARTICLE_2 = "article-2";
    private static final String ARTICLE_3 = "article-3";
    private static final String CUSTOMER_1 = "customer-1";
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
        String articleId = ARTICLE_1;
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
        String articleId = ARTICLE_1;
        BigDecimal standardPrice = new BigDecimal("34.29");
        BigDecimal customerPrice = new BigDecimal("29.99");

        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(standardPrice);
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId(articleId, CUSTOMER_1)).thenReturn(customerPrice);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), CUSTOMER_1);

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(customerPrice);
    }

    @Test
    public void testCalculateBasket() {
        // GIVEN
        Basket basket = new Basket(CUSTOMER_1, Set.of(
                new BasketEntry(ARTICLE_1, BigDecimal.ONE),
                new BasketEntry(ARTICLE_2, BigDecimal.ONE),
                new BasketEntry(ARTICLE_3, BigDecimal.ONE)));

        Map<String, BigDecimal> prices = Map.of(
                ARTICLE_1, new BigDecimal("1.50"),
                ARTICLE_2, new BigDecimal("0.29"),
                ARTICLE_3, new BigDecimal("9.99"));

        Mockito.when(mockPriceRepository.getPriceByArticleId(ARTICLE_1)).thenReturn(prices.get(ARTICLE_1));
        Mockito.when(mockPriceRepository.getPriceByArticleId(ARTICLE_2)).thenReturn(prices.get(ARTICLE_2));
        Mockito.when(mockPriceRepository.getPriceByArticleId(ARTICLE_3)).thenReturn(prices.get(ARTICLE_3));

        // WHEN
        BasketCalculationResult result = service.calculateBasket(basket);

        // THEN
        Assertions.assertThat(result.getCustomerId()).isEqualTo(CUSTOMER_1);
        Assertions.assertThat(result.getPricedBasketEntries()).isEqualTo(prices);
        Assertions.assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("11.78"));
    }
}
