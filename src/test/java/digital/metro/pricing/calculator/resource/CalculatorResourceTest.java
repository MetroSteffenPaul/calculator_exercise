package digital.metro.pricing.calculator.resource;

import digital.metro.pricing.calculator.request.Basket;
import digital.metro.pricing.calculator.request.BasketEntry;
import digital.metro.pricing.calculator.response.BasketCalculationResult;
import digital.metro.pricing.calculator.service.BasketCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CalculatorResourceTest {
    @Mock
    private BasketCalculatorService service;
    private CalculatorResource resource;
    private static final String CUSTOMER_1 = "customer-1";
    private static final String ARTICLE_1 = "article-1";

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        resource = new CalculatorResource(service);
    }

    @Test
    void calculateBasket() {
        //given
        BasketEntry basketEntry = new BasketEntry(ARTICLE_1, BigDecimal.ONE);
        Basket basket = new Basket(CUSTOMER_1, Collections.singleton(basketEntry));
        Map<String, BigDecimal> pricedBasketEntries = new HashMap<>();
        pricedBasketEntries.put(ARTICLE_1, BigDecimal.TEN);
        BasketCalculationResult serviceResult = new BasketCalculationResult(CUSTOMER_1, pricedBasketEntries, BigDecimal.TEN);
        when(service.calculateBasket(basket)).thenReturn(serviceResult);

        //when
        BasketCalculationResult result = resource.calculateBasket(basket);

        //then
        assertNotNull(result);
        assertEquals(CUSTOMER_1, result.getCustomerId());
        assertEquals(BigDecimal.TEN, result.getTotalAmount());
        assertNotNull(result.getPricedBasketEntries());
        assertNotNull(result.getPricedBasketEntries().get(ARTICLE_1));
        assertEquals(BigDecimal.TEN, result.getPricedBasketEntries().get(ARTICLE_1));
        verify(service).calculateBasket(basket);
    }
}