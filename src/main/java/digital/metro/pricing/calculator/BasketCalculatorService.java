package digital.metro.pricing.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BasketCalculatorService {

    private final PriceRepository priceRepository;

    @Autowired
    public BasketCalculatorService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public BasketCalculationResult calculateBasket(Basket basket) {
        updateBasketWithPrices(basket);
        return new BasketCalculationResult(basket.getCustomerId(), basket.getEntries(), basket.calculateTotalAmount());
    }

    public BigDecimal calculateArticle(BasketEntry be, CustomerId customerId) {
        ArticleId articleId = be.getArticleId();
        Optional<BigDecimal> customerPrice = Optional.empty();
        if (customerId != null) {
            customerPrice = priceRepository.getPriceByArticleIdAndCustomerId(articleId, customerId);
        }
        return customerPrice.orElse(priceRepository.getPriceByArticleId(articleId));
    }

    private void updateBasketWithPrices(Basket basket) {
        basket.getEntries()
                .forEach(be ->
                    be.updatePrice(calculateArticle(be, basket.getCustomerId()))
                );
    }
}
