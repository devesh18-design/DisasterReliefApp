package com.relief.model;

public class DemandRequest implements Comparable<DemandRequest> {
    private final String requestId;
    private final ReliefCamp camp;
    private final SupplyCategory category;
    private final int requestedQuantity;
    private final UrgencyLevel urgency;
    private int fulfilledQuantity;

    public DemandRequest(String requestId, ReliefCamp camp, SupplyCategory category, int requestedQuantity, UrgencyLevel urgency) {
        this.requestId = requestId;
        this.camp = camp;
        this.category = category;
        this.requestedQuantity = requestedQuantity;
        this.urgency = urgency;
        this.fulfilledQuantity = 0;
    }

    public String getRequestId() { return requestId; }
    public ReliefCamp getCamp() { return camp; }
    public SupplyCategory getCategory() { return category; }
    public int getRequestedQuantity() { return requestedQuantity; }
    public UrgencyLevel getUrgency() { return urgency; }
    public int getFulfilledQuantity() { return fulfilledQuantity; }

    public int getPendingQuantity() {
        return requestedQuantity - fulfilledQuantity;
    }

    public void fulfill(int amount) {
        this.fulfilledQuantity += amount;
    }

    public boolean isFullyFulfilled() {
        return fulfilledQuantity >= requestedQuantity;
    }

    @Override
    public int compareTo(DemandRequest other) {
        // Higher urgency weight gets served first (Max-Heap behavior)
        return Integer.compare(other.urgency.getWeight(), this.urgency.getWeight());
    }
}