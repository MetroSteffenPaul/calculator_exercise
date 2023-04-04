package digital.metro.pricing.calculator;

import digital.metro.pricing.calculator.model.Basket;
import digital.metro.pricing.calculator.model.BasketEntry;
import digital.metro.pricing.calculator.repository.PriceRepository;
import digital.metro.pricing.calculator.service.BasketCalculatorService;
import digital.metro.pricing.calculator.view.BasketCalculationResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

public class BasketCalculatorServiceTest {

    @Mock
    private PriceRepository mockPriceRepository;

    private BasketCalculatorService service;
    private final BigDecimal CUSTOMER1_RATE  = new BigDecimal("0.90");

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
        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(price);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), null);

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(price);
    }

    @Test
    public void whenArticleForCustomerReturnArticlePriceForCustomer() {
        // GIVEN
        String articleId = "article-1";
        BigDecimal standardPrice = new BigDecimal("34.29");
        BigDecimal customerPrice = new BigDecimal("29.99");
        String customerId = "customer-1";

        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(standardPrice);
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId(articleId, customerId)).thenReturn(customerPrice);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), customerId);

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(customerPrice);
    }

    @Test
    public void whenUndefinedCustomerAndExistentArticleReturnStandardPrice() {
        // GIVEN
        String articleId = "article-1";
        BigDecimal standardPrice = new BigDecimal("34.29");
        String customerId = "customer-4";

        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId(articleId, customerId)).thenReturn(standardPrice);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), customerId);

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(standardPrice);
    }

    @Test
    public void testCalculateBasket() {
        // GIVEN
        final String customerId = "customer-1";
        Basket basket = new Basket(customerId, Set.of(
                new BasketEntry("article-1", BigDecimal.ONE),
                new BasketEntry("article-2", BigDecimal.ONE),
                new BasketEntry("article-3", BigDecimal.ONE)));

        Map<String, BigDecimal> prices = Map.of(
                "article-1", new BigDecimal("1.50"),
                "article-2", new BigDecimal("0.29"),
                "article-3", new BigDecimal("9.99"));

        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId("article-1", customerId)).thenReturn(prices.get("article-1").multiply(CUSTOMER1_RATE).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId("article-2", customerId)).thenReturn(prices.get("article-2").multiply(CUSTOMER1_RATE).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId("article-3", customerId)).thenReturn(prices.get("article-3").multiply(CUSTOMER1_RATE).setScale(2, RoundingMode.HALF_UP));


        // WHEN
        BasketCalculationResult result = service.calculateBasket(basket);

        // THEN
        Assertions.assertThat(result.getCustomerId()).isEqualTo(customerId);
        //   Assertions.assertThat(result.getPricedBasketEntries()).isEqualTo(prices);
        Assertions.assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("10.60"));
    }

    @Test
    public void testCalculateBasketWithMultiplyArticle() {
        // GIVEN
        final String customerId = "customer-1";
        Basket basket = new Basket(customerId, Set.of(
                new BasketEntry("article-1", BigDecimal.ONE),
                new BasketEntry("article-2", new BigDecimal(3)),
                new BasketEntry("article-3", BigDecimal.ONE)));

        Map<String, BigDecimal> prices = Map.of(
                "article-1", new BigDecimal("1.50"),
                "article-2", new BigDecimal("0.29"),
                "article-3", new BigDecimal("9.99"));

        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId("article-1", customerId)).thenReturn(prices.get("article-1").multiply(CUSTOMER1_RATE).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId("article-2", customerId)).thenReturn(prices.get("article-2").multiply(CUSTOMER1_RATE).setScale(2, RoundingMode.HALF_UP));
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId("article-3", customerId)).thenReturn(prices.get("article-3").multiply(CUSTOMER1_RATE).setScale(2, RoundingMode.HALF_UP));


        // WHEN
        BasketCalculationResult result = service.calculateBasket(basket);

        // THEN
        Assertions.assertThat(result.getCustomerId()).isEqualTo(customerId);
        Assertions.assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("11.12"));
    }
}
