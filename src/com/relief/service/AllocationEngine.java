package com.relief.service;

import com.relief.model.*;
import java.util.*;

public class AllocationEngine {
    private final InventoryService inventoryService;
    private final PriorityQueue<DemandRequest> requestQueue;
    private final List<DemandRequest> allRequests;
    private final List<DispatchRecord> dispatchLedger;
    private int dispatchCounter = 1000;

    public AllocationEngine(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.requestQueue = new PriorityQueue<>();
        this.allRequests = new ArrayList<>();
        this.dispatchLedger = new ArrayList<>();
    }

    public void submitDemand(DemandRequest request) {
        allRequests.add(request);
        requestQueue.offer(request);
    }

    public List<DemandRequest> getAllRequests() {
        return Collections.unmodifiableList(allRequests);
    }

    public List<DispatchRecord> getDispatchLedger() {
        return Collections.unmodifiableList(dispatchLedger);
    }

    public List<DispatchRecord> executeAllocation() {
        List<DispatchRecord> cycleDispatches = new ArrayList<>();
        List<DemandRequest> unfulfilledTemp = new ArrayList<>();

        while (!requestQueue.isEmpty()) {
            DemandRequest highestPriorityDemand = requestQueue.poll();
            SupplyCategory requiredCat = highestPriorityDemand.getCategory();
            List<SupplyItem> availableStocks = inventoryService.getAvailableSuppliesByCategory(requiredCat);

            for (SupplyItem stock : availableStocks) {
                if (highestPriorityDemand.isFullyFulfilled()) break;

                int needed = highestPriorityDemand.getPendingQuantity();
                int currentStock = stock.getQuantity();

                if (currentStock > 0) {
                    int allocated = Math.min(needed, currentStock);
                    stock.deductQuantity(allocated);
                    highestPriorityDemand.fulfill(allocated);

                    DispatchRecord record = new DispatchRecord(
                        "DSP-" + (++dispatchCounter),
                        highestPriorityDemand.getCamp().getCampName(),
                        stock.getName(),
                        allocated
                    );
                    dispatchLedger.add(record);
                    cycleDispatches.add(record);
                }
            }

            if (!highestPriorityDemand.isFullyFulfilled()) {
                unfulfilledTemp.add(highestPriorityDemand);
            }
        }

        // Re-enqueue demands that could not be fully met for future donations
        requestQueue.addAll(unfulfilledTemp);
        return cycleDispatches;
    }
}