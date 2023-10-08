package digital.metro.pricing.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import digital.metro.pricing.calculator.BasketCalculationResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BasketCalculationResultSerializer extends JsonSerializer<BasketCalculationResult> {
    @Override
    public void serialize(BasketCalculationResult basketCalculationResult, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("customerId", basketCalculationResult.getCustomerId().getId());
        Map<String, BigDecimal> basketEntries = new HashMap<>();
        basketCalculationResult.getPricedBasketEntries().forEach(be -> basketEntries.put(be.getArticleId().getId(), be.getPrice()));
        jsonGenerator.writeObjectField ("pricedBasketEntries", basketEntries);
        jsonGenerator.writeNumberField("totalAmount", basketCalculationResult.getTotalAmount());
        jsonGenerator.writeEndObject();
    }
}
