package com.relief.ui;

import com.relief.model.*;
import com.relief.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DashboardFrame extends JFrame {
    private final InventoryService inventoryService;
    private final AllocationEngine allocationEngine;

    private DefaultTableModel inventoryTableModel;
    private DefaultTableModel demandTableModel;
    private DefaultTableModel logTableModel;
    private JLabel totalStockLabel;
    private JLabel pendingRequestsLabel;

    private int manualSupplyCounter = 100;
    private int manualDemandCounter = 300;

    public DashboardFrame(InventoryService inventoryService, AllocationEngine allocationEngine) {
        this.inventoryService = inventoryService;
        this.allocationEngine = allocationEngine;
        initializeUI();
        refreshViews();
    }

    private void initializeUI() {
        setTitle("Disaster Relief & Supply Chain Allocation Engine");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Metrics Bar
        JPanel metricsPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 5, 12));
        totalStockLabel = new JLabel("Total Stock Available: 0", SwingConstants.CENTER);
        pendingRequestsLabel = new JLabel("Pending Demands: 0", SwingConstants.CENTER);
        
        styleMetricCard(totalStockLabel, new Color(41, 128, 185));
        styleMetricCard(pendingRequestsLabel, new Color(192, 57, 43));
        metricsPanel.add(totalStockLabel);
        metricsPanel.add(pendingRequestsLabel);
        add(metricsPanel, BorderLayout.NORTH);

        // Center Split View (Inventory & Demands)
        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centerSplit.setResizeWeight(0.5);

        // Left: Inventory Table & Add Button
        JPanel inventoryPanel = new JPanel(new BorderLayout(5, 5));
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Central Warehouse Stock"));
        inventoryTableModel = new DefaultTableModel(new String[]{"ID", "Item Name", "Category", "Quantity"}, 0);
        JTable inventoryTable = new JTable(inventoryTableModel);
        inventoryPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        JButton addSupplyBtn = new JButton("+ Add New Donation/Supply");
        addSupplyBtn.addActionListener(e -> showAddSupplyDialog());
        inventoryPanel.add(addSupplyBtn, BorderLayout.SOUTH);
        centerSplit.setLeftComponent(inventoryPanel);

        // Right: Demand Queue Table & Add Button
        JPanel demandPanel = new JPanel(new BorderLayout(5, 5));
        demandPanel.setBorder(BorderFactory.createTitledBorder("Camp Relief Demands (Priority Sorted)"));
        demandTableModel = new DefaultTableModel(new String[]{"Req ID", "Camp Name", "Category", "Pending Qty", "Urgency"}, 0);
        JTable demandTable = new JTable(demandTableModel);
        demandPanel.add(new JScrollPane(demandTable), BorderLayout.CENTER);

        JButton addDemandBtn = new JButton("+ Submit Camp Demand");
        addDemandBtn.addActionListener(e -> showAddDemandDialog());
        demandPanel.add(addDemandBtn, BorderLayout.SOUTH);
        centerSplit.setRightComponent(demandPanel);

        // Bottom Panel (Execution & Logs)
        JPanel bottomContainer = new JPanel(new BorderLayout(5, 5));
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(5, 12, 12, 12));

        logTableModel = new DefaultTableModel(new String[]{"Dispatch ID", "Camp", "Supplied Item", "Quantity", "Time"}, 0);
        JTable logTable = new JTable(logTableModel);
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Real-Time Dispatch Ledger"));
        logPanel.setPreferredSize(new Dimension(1000, 180));
        logPanel.add(new JScrollPane(logTable), BorderLayout.CENTER);

        // Control Toolbar
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton allocateBtn = new JButton("Run Allocation Engine");
        allocateBtn.setBackground(new Color(39, 174, 96));
        allocateBtn.setForeground(Color.WHITE);
        allocateBtn.setFocusPainted(false);

        JButton exportBtn = new JButton("Export Manifest (CSV)");

        allocateBtn.addActionListener(e -> {
            List<DispatchRecord> results = allocationEngine.executeAllocation();
            refreshViews();
            JOptionPane.showMessageDialog(this, "Allocation Complete! Dispatched " + results.size() + " consignments.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        exportBtn.addActionListener(e -> exportToCsv());

        controls.add(allocateBtn);
        controls.add(exportBtn);

        bottomContainer.add(logPanel, BorderLayout.CENTER);
        bottomContainer.add(controls, BorderLayout.SOUTH);

        add(centerSplit, BorderLayout.CENTER);
        add(bottomContainer, BorderLayout.SOUTH);
    }

    private void styleMetricCard(JLabel label, Color bgColor) {
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // Modal to add supplies dynamically
    private void showAddSupplyDialog() {
        JTextField nameField = new JTextField();
        JComboBox<SupplyCategory> categoryBox = new JComboBox<>(SupplyCategory.values());
        JTextField qtyField = new JTextField();

        Object[] fields = {
            "Item Name:", nameField,
            "Category:", categoryBox,
            "Quantity Units:", qtyField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Warehouse Stock", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                int qty = Integer.parseInt(qtyField.getText().trim());
                if (name.isEmpty() || qty <= 0) throw new IllegalArgumentException();

                SupplyCategory category = (SupplyCategory) categoryBox.getSelectedItem();
                String itemId = "ITM-NEW-" + (++manualSupplyCounter);
                inventoryService.addSupply(new SupplyItem(itemId, name, category, qty));
                refreshViews();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please provide a valid item name and positive numeric quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Modal to submit camp demands dynamically
    private void showAddDemandDialog() {
        JTextField campNameField = new JTextField();
        JTextField locationField = new JTextField();
        JComboBox<SupplyCategory> categoryBox = new JComboBox<>(SupplyCategory.values());
        JTextField qtyField = new JTextField();
        JComboBox<UrgencyLevel> urgencyBox = new JComboBox<>(UrgencyLevel.values());

        Object[] fields = {
            "Camp Name:", campNameField,
            "Location:", locationField,
            "Required Category:", categoryBox,
            "Quantity Needed:", qtyField,
            "Urgency Level:", urgencyBox
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Submit Camp Distress Demand", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String campName = campNameField.getText().trim();
                String location = locationField.getText().trim();
                int qty = Integer.parseInt(qtyField.getText().trim());
                if (campName.isEmpty() || qty <= 0) throw new IllegalArgumentException();

                SupplyCategory category = (SupplyCategory) categoryBox.getSelectedItem();
                UrgencyLevel urgency = (UrgencyLevel) urgencyBox.getSelectedItem();

                ReliefCamp camp = new ReliefCamp("CMP-" + (++manualDemandCounter), campName, location.isEmpty() ? "Field Site" : location, 100);
                String reqId = "REQ-" + manualDemandCounter;

                allocationEngine.submitDemand(new DemandRequest(reqId, camp, category, qty, urgency));
                refreshViews();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please provide valid details and positive numeric quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshViews() {
        // Refresh Inventory
        inventoryTableModel.setRowCount(0);
        int totalStock = 0;
        for (SupplyItem item : inventoryService.getAllSupplies()) {
            inventoryTableModel.addRow(new Object[]{item.getItemId(), item.getName(), item.getCategory(), item.getQuantity()});
            totalStock += item.getQuantity();
        }
        totalStockLabel.setText("Total Stock Units in Hub: " + totalStock);

        // Refresh Demands
        demandTableModel.setRowCount(0);
        int pendingCount = 0;
        for (DemandRequest req : allocationEngine.getAllRequests()) {
            if (!req.isFullyFulfilled()) {
                demandTableModel.addRow(new Object[]{
                    req.getRequestId(),
                    req.getCamp().getCampName(),
                    req.getCategory(),
                    req.getPendingQuantity(),
                    req.getUrgency()
                });
                pendingCount++;
            }
        }
        pendingRequestsLabel.setText("Active Unfulfilled Demands: " + pendingCount);

        // Refresh Log Table
        logTableModel.setRowCount(0);
        for (DispatchRecord rec : allocationEngine.getDispatchLedger()) {
            logTableModel.addRow(new Object[]{rec.getDispatchId(), rec.getCampName(), rec.getItemName(), rec.getQuantity(), rec.getFormattedTime()});
        }
    }

    private void exportToCsv() {
        try (FileWriter writer = new FileWriter("dispatch_manifest.csv")) {
            writer.write("DispatchID,CampName,ItemName,Quantity,Timestamp\n");
            for (DispatchRecord rec : allocationEngine.getDispatchLedger()) {
                writer.write(rec.toCsvRow() + "\n");
            }
            JOptionPane.showMessageDialog(this, "Manifest saved successfully as dispatch_manifest.csv", "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error exporting CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}