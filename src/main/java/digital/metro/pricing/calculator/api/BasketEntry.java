package digital.metro.pricing.calculator.api;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Shopping Basket entry for a single article.
 */
public class BasketEntry {

    private final String articleId;
    private final BigDecimal quantity;

    public BasketEntry(String articleId, BigDecimal quantity) {
        this.articleId = Objects.requireNonNull(articleId);
        this.quantity = Objects.requireNonNull(quantity);

        if (this.quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity should be strictly positive, provided " + this.quantity);
        }
    }

    public String getArticleId() {
        return articleId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    @Override
    public int hashCode() {
        return articleId.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BasketEntry)) {
            return false;
        }
        if (this == other) {
            return true;
        }

        BasketEntry entry = (BasketEntry) other;

        return entry.articleId.equals(articleId);
    }
}
