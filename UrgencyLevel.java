package com.relief.model;

public enum UrgencyLevel {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);

    private final int weight;

    UrgencyLevel(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}