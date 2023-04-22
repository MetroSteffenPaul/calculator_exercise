package digital.metro.pricing.calculator.api.web;

import digital.metro.pricing.calculator.api.Basket;
import digital.metro.pricing.calculator.api.BasketCalculationResult;
import digital.metro.pricing.calculator.api.BasketEntry;
import digital.metro.pricing.calculator.services.BasketCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class CalculatorResource {

    private BasketCalculatorService basketCalculatorService;

    @Autowired
    public CalculatorResource(BasketCalculatorService basketCalculatorService) {
        this.basketCalculatorService = basketCalculatorService;
    }

    @PostMapping("/calculator/calculate-basket")
    public BasketCalculationResult calculateBasket(@RequestBody Basket basket) {
        return basketCalculatorService.calculateBasket(basket);
    }

    @GetMapping("/calculator/article/{articleId}")
    public BigDecimal getArticlePrice(@PathVariable String articleId) {
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), null);
    }

    @GetMapping("/calculator/getarticlepriceforcustomer")
    public BigDecimal getArticlePriceForCustomer(@RequestParam String articleId, @RequestParam String customerId) {
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), customerId);
    }
}
