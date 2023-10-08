package digital.metro.pricing.calculator;

import java.math.BigDecimal;

public class BasketEntry {

    private ArticleId articleId;
    private BigDecimal quantity;
    private BigDecimal price;

    public BasketEntry(ArticleId articleId, BigDecimal quantity) {
        this.articleId = articleId;
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ArticleId getArticleId() {
        return articleId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void updatePrice(BigDecimal price) {
        this.price = price;
    }
}
