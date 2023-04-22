package digital.metro.pricing.calculator.services;

import digital.metro.pricing.calculator.api.Basket;
import digital.metro.pricing.calculator.api.BasketCalculationResult;
import digital.metro.pricing.calculator.api.BasketEntry;
import digital.metro.pricing.calculator.dao.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BasketCalculatorService {

    private PriceRepository priceRepository;

    @Autowired
    public BasketCalculatorService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public BasketCalculationResult calculateBasket(Basket basket) {
        Map<String, BigDecimal> pricedArticles =
                basket.getEntries()
                        .stream()
                        .collect(Collectors.toMap(
                                BasketEntry::getArticleId,
                                entry -> calculateArticle(entry, basket.getCustomerId())));

        return new BasketCalculationResult(basket.getCustomerId(), pricedArticles);
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
}
