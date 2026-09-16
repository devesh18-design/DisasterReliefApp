package com.relief;

import com.relief.model.*;
import com.relief.service.*;
import com.relief.ui.DashboardFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize Core Services
        InventoryService inventoryService = new InventoryService();
        AllocationEngine allocationEngine = new AllocationEngine(inventoryService);

        // 2. Pre-seed Warehouse Inventory (Multiple items per category)
        // Medical Supplies
        inventoryService.addSupply(new SupplyItem("ITM-MED-01", "Trauma First-Aid Kits", SupplyCategory.MEDICAL, 60));
        inventoryService.addSupply(new SupplyItem("ITM-MED-02", "Emergency Insulin & Cold-Chain Vials", SupplyCategory.MEDICAL, 25));
        inventoryService.addSupply(new SupplyItem("ITM-MED-03", "Broad-Spectrum Antibiotics (Packs)", SupplyCategory.MEDICAL, 100));

        // Water Supplies
        inventoryService.addSupply(new SupplyItem("ITM-WTR-01", "Purified Water Packs (10L)", SupplyCategory.WATER, 150));
        inventoryService.addSupply(new SupplyItem("ITM-WTR-02", "Emergency Water Purification Tablets (Boxes)", SupplyCategory.WATER, 80));

        // Food Supplies
        inventoryService.addSupply(new SupplyItem("ITM-FOD-01", "High-Calorie Ready-to-Eat Rations (MRE)", SupplyCategory.FOOD, 250));
        inventoryService.addSupply(new SupplyItem("ITM-FOD-02", "Infant Nutrition & Milk Formula", SupplyCategory.FOOD, 40));
        inventoryService.addSupply(new SupplyItem("ITM-FOD-03", "Dry Grains & Rice Sacks (25kg)", SupplyCategory.FOOD, 90));

        // Shelter Supplies
        inventoryService.addSupply(new SupplyItem("ITM-SHL-01", "All-Weather Family Tents", SupplyCategory.SHELTER, 35));
        inventoryService.addSupply(new SupplyItem("ITM-SHL-02", "Thermal Blankets (5-Pack)", SupplyCategory.SHELTER, 120));
        inventoryService.addSupply(new SupplyItem("ITM-SHL-03", "Heavy-Duty Tarpaulin Sheets", SupplyCategory.SHELTER, 75));

        // 3. Pre-seed Diverse Relief Camps
        ReliefCamp campAlpha = new ReliefCamp("CMP-01", "Sector 4 Coastal Camp", "Bay Harbor Zone", 420);
        ReliefCamp campBeta  = new ReliefCamp("CMP-02", "North Ridge School Shelter", "Mountain Foothills", 180);
        ReliefCamp campGamma = new ReliefCamp("CMP-03", "Central Metro Underpass", "Urban Downtown", 650);
        ReliefCamp campDelta = new ReliefCamp("CMP-04", "Riverside Community Center", "Lowland Floodplain", 310);
        ReliefCamp campEcho  = new ReliefCamp("CMP-05", "Highland Sports Arena", "Upper Valley", 520);

        // 4. Pre-seed Multi-Tiered Camp Relief Demands
        // CRITICAL Urgency Demands (Triage Priority 4)
        allocationEngine.submitDemand(new DemandRequest("REQ-201", campDelta, SupplyCategory.WATER, 120, UrgencyLevel.CRITICAL));
        allocationEngine.submitDemand(new DemandRequest("REQ-202", campAlpha, SupplyCategory.MEDICAL, 50, UrgencyLevel.CRITICAL));
        allocationEngine.submitDemand(new DemandRequest("REQ-203", campGamma, SupplyCategory.MEDICAL, 30, UrgencyLevel.CRITICAL));

        // HIGH Urgency Demands (Triage Priority 3)
        allocationEngine.submitDemand(new DemandRequest("REQ-204", campGamma, SupplyCategory.FOOD, 200, UrgencyLevel.HIGH));
        allocationEngine.submitDemand(new DemandRequest("REQ-205", campBeta, SupplyCategory.SHELTER, 40, UrgencyLevel.HIGH));
        allocationEngine.submitDemand(new DemandRequest("REQ-206", campDelta, SupplyCategory.FOOD, 80, UrgencyLevel.HIGH));

        // MEDIUM Urgency Demands (Triage Priority 2)
        allocationEngine.submitDemand(new DemandRequest("REQ-207", campEcho, SupplyCategory.WATER, 100, UrgencyLevel.MEDIUM));
        allocationEngine.submitDemand(new DemandRequest("REQ-208", campAlpha, SupplyCategory.SHELTER, 30, UrgencyLevel.MEDIUM));
        allocationEngine.submitDemand(new DemandRequest("REQ-209", campEcho, SupplyCategory.FOOD, 110, UrgencyLevel.MEDIUM));

        // LOW Urgency Demands (Triage Priority 1)
        allocationEngine.submitDemand(new DemandRequest("REQ-210", campBeta, SupplyCategory.SHELTER, 20, UrgencyLevel.LOW));
        allocationEngine.submitDemand(new DemandRequest("REQ-211", campDelta, SupplyCategory.WATER, 40, UrgencyLevel.LOW));

        // 5. Launch Native Visual Dashboard
        SwingUtilities.invokeLater(() -> {
            DashboardFrame dashboard = new DashboardFrame(inventoryService, allocationEngine);
            dashboard.setVisible(true);
        });
    }
}