package digital.metro.pricing.calculator.api;

import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class Basket {

    private final String customerId;
    private final Set<BasketEntry> entries;

    public Basket(String customerId, Set<BasketEntry> entries) {
        this.customerId = customerId;
        this.entries = entries != null ? unmodifiableSet(entries) : Set.of();
    }

    public String getCustomerId() {
        return customerId;
    }

    public Set<BasketEntry> getEntries() {
        return entries;
    }
}
