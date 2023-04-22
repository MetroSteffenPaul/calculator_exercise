package digital.metro.pricing.calculator.dao;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A dummy implementation for testing purposes. In production, we would get real prices from a database.
 */
@Component
public class PriceRepository {

    private final Map<String, BigDecimal> prices = new ConcurrentHashMap<>();

    private final Random random = new Random();

    public BigDecimal getPriceByArticleId(String articleId) {
        return prices.computeIfAbsent(articleId, this::defaultPriceOf);
    }

    private BigDecimal defaultPriceOf(String articleId) {
        return BigDecimal.valueOf(0.5d + random.nextDouble() * 29.50d);
    }
}
