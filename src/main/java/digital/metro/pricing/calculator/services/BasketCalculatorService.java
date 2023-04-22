package digital.metro.pricing.calculator.services;

import digital.metro.pricing.calculator.api.Basket;
import digital.metro.pricing.calculator.api.BasketCalculationResult;
import digital.metro.pricing.calculator.api.BasketEntry;
import digital.metro.pricing.calculator.dao.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class BasketCalculatorService {

    private PriceRepository priceRepository;

    private PromotionsService promotionsService;

    @Autowired
    public BasketCalculatorService(PriceRepository priceRepository, PromotionsService promotionsService) {
        this.priceRepository = priceRepository;
        this.promotionsService = promotionsService;
    }

    public BasketCalculationResult calculateBasket(Basket basket) {

        Map<String, BigDecimal> pricedArticles =
                basket.getEntries()
                        .stream()
                        .collect(toMap(
                                BasketEntry::getArticleId,
                                entry -> calculateArticle(entry, basket.getCustomerId())));

        return new BasketCalculationResult(basket.getCustomerId(), pricedArticles);
    }

    public BigDecimal calculateArticle(BasketEntry basketEntry, String customerId) {
        String articleId = basketEntry.getArticleId();

        return applyPromotion(priceRepository.getPriceByArticleId(articleId), customerId);
    }

    private BigDecimal applyPromotion(BigDecimal price, String customerId) {
        return promotionsService.promotionFor(customerId).multiply(price).setScale(2, RoundingMode.HALF_UP);
    }
}
