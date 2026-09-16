package com.relief.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DispatchRecord {
    private final String dispatchId;
    private final String campName;
    private final String itemName;
    private final int quantity;
    private final LocalDateTime timestamp;

    public DispatchRecord(String dispatchId, String campName, String itemName, int quantity) {
        this.dispatchId = dispatchId;
        this.campName = campName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.timestamp = LocalDateTime.now();
    }

    public String getDispatchId() { return dispatchId; }
    public String getCampName() { return campName; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    
    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String toCsvRow() {
        return String.join(",", dispatchId, campName, itemName, String.valueOf(quantity), timestamp.toString());
    }
}