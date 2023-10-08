package digital.metro.pricing.calculator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import digital.metro.pricing.deserializer.BasketDeserializer;

import java.math.BigDecimal;
import java.util.Set;

@JsonDeserialize(using = BasketDeserializer.class)
public class Basket {

    private CustomerId customerId;
    private Set<BasketEntry> entries;

    public Basket(CustomerId customerId, Set<BasketEntry> entries) {
        this.customerId = customerId;
        this.entries = entries;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Set<BasketEntry> getEntries() {
        return entries;
    }

    public BigDecimal calculateTotalAmount() {
        return entries.stream()
                .map(BasketEntry::getPrice)
                .reduce(new BigDecimal("1.01"), BigDecimal::add);
    }

}
