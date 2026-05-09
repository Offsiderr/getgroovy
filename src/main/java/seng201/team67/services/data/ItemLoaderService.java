package seng201.team67.services.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import seng201.team67.models.items.ConditionalItem;
import seng201.team67.models.items.CosumableItem;
import seng201.team67.models.items.EquippedItem;
import seng201.team67.models.items.Item;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ItemLoaderService {

    private static final String resourcePath = "/data/items.json";

    public List<Item> loadAll() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("items.json not found at " + resourcePath);
            }

            List<Item> items = new ArrayList<>();
            JsonNode root = mapper.readTree(is);

            for (JsonNode itemNode : root) {
                JsonNode normalizedItemNode = normalizeEffects(itemNode);
                String type = normalizedItemNode.path("type").asText();
                Item item = switch (type) {
                    case "CONSUMABLE" -> mapper.treeToValue(normalizedItemNode, CosumableItem.class);
                    case "EQUIPPED" -> mapper.treeToValue(normalizedItemNode, EquippedItem.class);
                    case "CONDITIONAL" -> mapper.treeToValue(normalizedItemNode, ConditionalItem.class);
                    default -> throw new IllegalArgumentException("Unknown item type: " + type);
                };
                items.add(item);
            }

            return items;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load items: " + e.getMessage(), e);
        }
    }

    private JsonNode normalizeEffects(JsonNode itemNode) {
        if (!itemNode.isObject()) {
            return itemNode;
        }

        ObjectNode normalizedItemNode = ((ObjectNode) itemNode).deepCopy();
        JsonNode effectsNode = normalizedItemNode.get("effects");

        if (effectsNode == null && normalizedItemNode.has("effect")) {
            ArrayNode effectsArray = normalizedItemNode.arrayNode();
            effectsArray.add(normalizedItemNode.get("effect"));
            normalizedItemNode.set("effects", effectsArray);
        }

        return normalizedItemNode;
    }
}
