package digital.metro.pricing.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import digital.metro.pricing.calculator.ArticleId;
import digital.metro.pricing.calculator.Basket;
import digital.metro.pricing.calculator.BasketEntry;
import digital.metro.pricing.calculator.CustomerId;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class BasketDeserializer extends JsonDeserializer<Basket> {

    @Override
    public Basket deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        var customerId = new CustomerId(node.get("customerId").asText());
        var entries = new HashSet<BasketEntry>();
        var entriesIterator = node.get("entries").elements();
        while (entriesIterator.hasNext()) {
            var entriesNode = entriesIterator.next();
            var articleId = new ArticleId(entriesNode.get("articleId").asText());
            var quantity = BigDecimal.valueOf(entriesNode.get("quantity").asLong());
            entries.add(new BasketEntry(articleId, quantity));
        }
        return new Basket(customerId, entries);
    }
}
