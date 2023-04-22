package digital.metro.pricing.calculator.services;

import digital.metro.pricing.calculator.api.Basket;
import digital.metro.pricing.calculator.api.BasketCalculationResult;
import digital.metro.pricing.calculator.api.BasketEntry;
import digital.metro.pricing.calculator.dao.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
        String articleId = "article-1";
        BigDecimal price = new BigDecimal("34.29");
        when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(price);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), null);

        // THEN
        assertThat(result).isEqualByComparingTo(price);
    }

    @Test
    public void testCalculateArticleForCustomer() {
        // GIVEN
        String articleId = "article-1";
        BigDecimal standardPrice = new BigDecimal("34.29");
        BigDecimal customerPrice = new BigDecimal("29.99");
        String customerId = "customer-1";

        when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(standardPrice);
        when(mockPriceRepository.getPriceByArticleIdAndCustomerId(articleId, customerId)).thenReturn(customerPrice);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), "customer-1");

        // THEN
        assertThat(result).isEqualByComparingTo(customerPrice);
    }

    @Test
    public void testCalculateBasket() {
        // GIVEN
        Basket basket = new Basket("customer-1", Set.of(
                new BasketEntry("article-1", BigDecimal.ONE),
                new BasketEntry("article-2", BigDecimal.ONE),
                new BasketEntry("article-3", BigDecimal.ONE)));

        Map<String, BigDecimal> prices = Map.of(
                "article-1", new BigDecimal("1.50"),
                "article-2", new BigDecimal("0.29"),
                "article-3", new BigDecimal("9.99"));

        when(mockPriceRepository.getPriceByArticleId("article-1")).thenReturn(prices.get("article-1"));
        when(mockPriceRepository.getPriceByArticleId("article-2")).thenReturn(prices.get("article-2"));
        when(mockPriceRepository.getPriceByArticleId("article-3")).thenReturn(prices.get("article-3"));

        // WHEN
        BasketCalculationResult result = service.calculateBasket(basket);

        // THEN
        assertThat(result.getCustomerId()).isEqualTo("customer-1");
        assertThat(result.getPricedBasketEntries()).isEqualTo(prices);
        assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("11.78"));
    }

    @Test
    public void testBasketEntrySets() {
        Set<BasketEntry> entries = new HashSet<>();

        entries.add(new BasketEntry("article1", BigDecimal.ONE));
        entries.add(new BasketEntry("article1", new BigDecimal("1.0")));

        assertThat(entries).hasSize(1);
    }
}
