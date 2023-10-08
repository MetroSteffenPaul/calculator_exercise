package digital.metro.pricing.calculator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import digital.metro.pricing.serializer.BasketCalculationResultSerializer;

import java.math.BigDecimal;
import java.util.Set;

@JsonSerialize(using = BasketCalculationResultSerializer.class)
public class BasketCalculationResult {

    private CustomerId customerId;
    private Set<BasketEntry> pricedBasketEntries;
    private BigDecimal totalAmount;

    private BasketCalculationResult() {
    }

    public BasketCalculationResult(CustomerId customerId, Set<BasketEntry> pricedBasketEntries, BigDecimal totalAmount) {
        this.customerId = customerId;
        this.pricedBasketEntries = pricedBasketEntries;
        this.totalAmount = totalAmount;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Set<BasketEntry> getPricedBasketEntries() {
        return pricedBasketEntries;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
