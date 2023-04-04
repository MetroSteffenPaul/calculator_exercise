package digital.metro.pricing.calculator.repository;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A dummy implementation for testing purposes. In production, we would get real prices from a database.
 */
@Component
public class PriceRepository {

    public static final String CUSTOMER1_RATE = "0.90";
    public static final String CUSTOMER2_RATE = "0.85";
    private Map<String, BigDecimal> prices = new HashMap<>();
    private Random random = new Random();

    public BigDecimal getPriceByArticleId(String articleId) {
        return prices.computeIfAbsent(articleId,
                key -> BigDecimal.valueOf(0.5d + random.nextDouble() * 29.50d).setScale(2, RoundingMode.HALF_UP));
    }

    public BigDecimal getPriceByArticleIdAndCustomerId(String articleId, String customerId) {
        switch(customerId) {
            case "customer-1":
                BigDecimal price1 = getPriceByArticleId(articleId).multiply(new BigDecimal(CUSTOMER1_RATE)).setScale(2, RoundingMode.HALF_UP);
                return price1;
            case "customer-2":
                BigDecimal price12=  getPriceByArticleId(articleId).multiply(new BigDecimal(CUSTOMER2_RATE)).setScale(2, RoundingMode.HALF_UP);
                return price12;
        }

        return getPriceByArticleId(articleId);
    }
}
