package digital.metro.pricing.calculator.service;

import digital.metro.pricing.calculator.repository.PriceRepository;
import digital.metro.pricing.calculator.request.Basket;
import digital.metro.pricing.calculator.request.BasketEntry;
import digital.metro.pricing.calculator.response.BasketCalculationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BasketCalculatorService {

    private final PriceRepository priceRepository;

    @Autowired
    public BasketCalculatorService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public BasketCalculationResult calculateBasket(Basket basket) {
        Map<String, BigDecimal> pricedArticles = basket.getEntries()
                .stream()
                .collect(Collectors.toMap(
                        BasketEntry::getArticleId,
                        entry -> calculateArticle(entry, basket.getCustomerId())));

        BigDecimal totalAmount = getTotalAmount(pricedArticles);

        return new BasketCalculationResult(basket.getCustomerId(), pricedArticles, totalAmount);
    }

    public BigDecimal calculateArticle(BasketEntry basketEntry, String customerId) {
        String articleId = basketEntry.getArticleId();

        if (customerId != null) {
            BigDecimal customerPrice = priceRepository.getPriceByArticleIdAndCustomerId(articleId, customerId);
            if (customerPrice != null) {
                return customerPrice;
            }
        }
        return priceRepository.getPriceByArticleId(articleId);
    }

    private BigDecimal getTotalAmount(Map<String, BigDecimal> pricedArticles) {
        return pricedArticles.values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
