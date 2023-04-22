package digital.metro.pricing.calculator.api;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * Shopping Basket total amounts: grand total + total by article
 */
public class BasketCalculationResult {

    private String customerId;

    /**
     * Totals by article: key = articleId, value = total (price * quantity) for that article
     */
    private Map<String, BigDecimal> pricedBasketEntries;
    private BigDecimal totalAmount;

    private BasketCalculationResult() {
    }

    public BasketCalculationResult(String customerId, Map<String, BigDecimal> pricedBasketEntries) {
        this.customerId = customerId;
        this.pricedBasketEntries = pricedBasketEntries != null ? unmodifiableMap(pricedBasketEntries) : Map.of();
        this.totalAmount = calculateTotalAmount();
    }

    private BigDecimal calculateTotalAmount() {
        return this.pricedBasketEntries.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getCustomerId() {
        return customerId;
    }

    public Map<String, BigDecimal> getPricedBasketEntries() {
        return pricedBasketEntries;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
