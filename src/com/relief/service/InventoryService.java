package com.relief.service;

import com.relief.model.SupplyCategory;
import com.relief.model.SupplyItem;
import java.util.*;

public class InventoryService {
    private final Map<String, SupplyItem> stockRegistry = new LinkedHashMap<>();

    public void addSupply(SupplyItem item) {
        stockRegistry.put(item.getItemId(), item);
    }

    public List<SupplyItem> getAllSupplies() {
        return new ArrayList<>(stockRegistry.values());
    }

    public List<SupplyItem> getAvailableSuppliesByCategory(SupplyCategory category) {
        List<SupplyItem> matches = new ArrayList<>();
        for (SupplyItem item : stockRegistry.values()) {
            if (item.getCategory() == category && item.getQuantity() > 0) {
                matches.add(item);
            }
        }
        return matches;
    }
}