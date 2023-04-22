package digital.metro.pricing.calculator.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Service for supplying customer promotions.
 */
@Service
public class PromotionsService {

    /**
     * Promotions for specific customers. Key = customerId, value = promotion factor,
     * must be in the interval (0, 1).
     * In a Production environment, this would be read from the database or configs.
     */
    private final Map<String, BigDecimal> promotionsByCustomer = Map.of(
            "customer-1", new BigDecimal("0.9"),
            "customer-2", new BigDecimal("0.85")
    );

    /**
     * If the supplied customer has a promotional factor registered, return that;
     * otherwise simply return 1. This is done to encourage a fluent programming style,
     * rather than supplying a separate hasPromotion(customerId) check, or asking the caller
     * to do null checks on the result.
     *
     * @param customerId
     * @return the registered promotional factor or 1 if no promotion for that customer
     */
    public BigDecimal promotionFor(String customerId) {
        return promotionsByCustomer.getOrDefault(customerId, BigDecimal.ONE);
    }

}
