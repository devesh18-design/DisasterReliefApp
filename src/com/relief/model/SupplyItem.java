package com.relief.model;

public class SupplyItem {
    private final String itemId;
    private final String name;
    private final SupplyCategory category;
    private int quantity;

    public SupplyItem(String itemId, String name, SupplyCategory category, int quantity) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    public String getItemId() { return itemId; }
    public String getName() { return name; }
    public SupplyCategory getCategory() { return category; }
    public int getQuantity() { return quantity; }

    public void deductQuantity(int qty) {
        if (qty > this.quantity) {
            this.quantity = 0;
        } else {
            this.quantity -= qty;
        }
    }
}