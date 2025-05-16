package Dashboard;

import main.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Arrays;

public class dashboardPanel extends JPanel {
    private static final String[] dateSet = {"Today", "This Week", "This Month"};
    private static final String[] tableFilter = {"Sale ID", "Customer Name", "Quantity", "Total Price", "Order Status",};
    private static final String[] columnNames1 = {"Sale ID", "Customer Name", "Quantity", "Total Price", "Order Status"};
    private static final String[] columnNames2 = {"Total Revenue", "Total Orders", "Total Quantity Sold"};
    private static final String[] columnNames3 = {"Item Name", "Total Quantity Sold", "Total Price"};
    private static final String[] columnNames4 = {"Item Name", "Item Type", "Location", "Stock"};

    private JComboBox<String> dateSetDropdown, tableFilterDropdown;
    private JTable salesTable, stockLevelTable;
    private DefaultTableModel salesTableModel, summaryTableModel, topSellingModel, stockLevelModel;
    private JTextField dbSearchField;
    private JButton inventoryButton, transactButton, ordersButton, accountButton, userButton, salesHistoryButton;

    private static final dashboardPanel dashboardPanel = new dashboardPanel();

    private boolean ascending = true;


    public dashboardPanel() {
        salesTableModel = new DefaultTableModel(columnNames1, 0);
        summaryTableModel = new DefaultTableModel(columnNames2, 0);
        topSellingModel = new DefaultTableModel(columnNames3, 0);
        stockLevelModel = new DefaultTableModel(columnNames4, 0);

        salesTable = new JTable(salesTableModel);
        stockLevelTable = new JTable(stockLevelModel);

        dashboardDB.loadStockLevelData(stockLevelModel);

        dashboardUI();
        refreshDashboard();
    }


    public static void gotoDashboard() {
        dashboardPanel.refreshDashboard();
        Main.changeCard("dashboard");
    }

    private static void gotoInventory() {
    }

    private static void gotoTransact() {
    }

    private static void gotoOrders() {
    }

    private static void gotoAccount() {
    }

    private static void gotoUserAdmin() {
    }

    private static void gotoSalesHistory() {
        Main.changeCard("SalesHistory");
    }

    public static void refresh() {
        dashboardPanel.refreshDashboard();
    }

    public void dashboardUI() {
        setLayout(null);


        inventoryButton = new JButton("Inventory");
        transactButton = new JButton("Transact");
        ordersButton = new JButton("Orders");
        accountButton = new JButton("Accounts");
        userButton = new JButton("UserAdmin");
        salesHistoryButton = new JButton("Sales History");

        dbSearchField = new JTextField(150);

        dateSetDropdown = new JComboBox<>(dateSet);
        tableFilterDropdown = new JComboBox<>(tableFilter);


        JLabel
                salesLabel = new JLabel("Sales Summary"),
                topSellingLabel = new JLabel("Top-Selling Items"),
                stockLabel = new JLabel("Items Stock-Level"),
                recentSalesLabel = new JLabel("Recent Sales");

        JPanel
                topBorder = new JPanel(null),
                bottomBorder = new JPanel(null),
                dbPanel = new JPanel(null),
                modulesPanel = new JPanel(null),
                dbCardPanel = new JPanel(null);

        JButton
                dbsearchButton = new JButton("Search"),
                dbrefreshButton = new JButton("Refresh"),
                sortButton = new JButton("↑↓"),
                exitButton = new JButton("X");

        JTable
                totalSummaryTable = new JTable(summaryTableModel),
                topSellingTable = new JTable(topSellingModel);

        JScrollPane
                summaryScroll = new JScrollPane(totalSummaryTable),
                dbtabScrollPane = new JScrollPane(salesTable),
                topSellingScroll = new JScrollPane(topSellingTable),
                stockScroll = new JScrollPane(stockLevelTable);

        JTableHeader
                salesTableHeader = salesTable.getTableHeader();


        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(salesTableModel);
        salesTable.setRowSorter(sorter);
        salesTable.setModel(salesTableModel);

        totalSummaryTable.getTableHeader().setPreferredSize(new Dimension(0, 50));
        topSellingTable.getTableHeader().setPreferredSize(new Dimension(0, 30));
        stockLevelTable.getTableHeader().setPreferredSize(new Dimension(0, 30));
        salesTableHeader.setPreferredSize(new Dimension(salesTableHeader.getPreferredSize().width, 50));

        salesTable.getTableHeader().setResizingAllowed(false);
        salesTable.getTableHeader().setReorderingAllowed(false);
        totalSummaryTable.getTableHeader().setResizingAllowed(false);
        totalSummaryTable.getTableHeader().setReorderingAllowed(false);
        topSellingTable.getTableHeader().setResizingAllowed(false);
        topSellingTable.getTableHeader().setReorderingAllowed(false);
        stockLevelTable.getTableHeader().setResizingAllowed(false);
        stockLevelTable.getTableHeader().setReorderingAllowed(false);

        salesLabel.setForeground(Color.decode("#ffffff"));
        salesLabel.setBackground(Color.decode("#014518"));
        topSellingLabel.setForeground(Color.decode("#ffffff"));
        topSellingLabel.setBackground(Color.decode("#014518"));
        stockLabel.setForeground(Color.decode("#ffffff"));
        stockLabel.setBackground(Color.decode("#014518"));
        recentSalesLabel.setForeground(Color.decode("#ffffff"));
        recentSalesLabel.setBackground(Color.decode("#014518"));
        dbCardPanel.setBackground(Color.decode("#014518"));
        modulesPanel.setBackground(Color.decode("#ff9933"));
        dbPanel.setBackground(Color.decode("#ff9933"));
        topBorder.setBackground(Color.decode("#ff9933"));
        bottomBorder.setBackground(Color.decode("#ff9933"));
        userButton.setForeground(Color.decode("#ffffff"));
        userButton.setBackground(Color.decode("#014518"));
        inventoryButton.setForeground(Color.decode("#ffffff"));
        inventoryButton.setBackground(Color.decode("#014518"));
        transactButton.setForeground(Color.decode("#ffffff"));
        transactButton.setBackground(Color.decode("#014518"));
        ordersButton.setForeground(Color.decode("#ffffff"));
        ordersButton.setBackground(Color.decode("#014518"));
        accountButton.setForeground(Color.decode("#ffffff"));
        accountButton.setBackground(Color.decode("#014518"));
        dbsearchButton.setForeground(Color.decode("#ffffff"));
        dbsearchButton.setBackground(Color.decode("#014518"));
        dbrefreshButton.setForeground(Color.decode("#ffffff"));
        dbrefreshButton.setBackground(Color.decode("#014518"));
        salesHistoryButton.setForeground(Color.decode("#ffffff"));
        salesHistoryButton.setBackground(Color.decode("#014518"));
        sortButton.setBackground(Color.decode("#014518"));
        sortButton.setForeground(Color.decode("#ffffff"));
        exitButton.setForeground(Color.decode("#ffffff"));
        exitButton.setBackground(Color.decode("#ff3131"));
        dbSearchField.setBackground(Color.decode("#ffffff"));
        dbSearchField.setForeground(Color.decode("#000000"));
        tableFilterDropdown.setBackground(Color.decode("#014518"));
        tableFilterDropdown.setForeground(Color.decode("#ffffff"));

        salesLabel.setBounds(635, 175, 100, 20);
        topSellingLabel.setBounds(635, 305, 150, 20);
        stockLabel.setBounds(635, 460, 150, 20);
        recentSalesLabel.setBounds(220, 175, 150, 20);
        modulesPanel.setBounds(0, 30, 880, 60);
        dbPanel.setBounds(15, 100, 850, 65);
        dbCardPanel.setBounds(0, 0, 880, 660);
        topBorder.setBounds(0, 0, 880, 30);
        bottomBorder.setBounds(0, 630, 880, 30);
        userButton.setBounds(20, 10, 120, 40);
        inventoryButton.setBounds(322, 10, 120, 40);
        transactButton.setBounds(462, 10, 120, 40);
        ordersButton.setBounds(602, 10, 120, 40);
        accountButton.setBounds(742, 10, 120, 40);
        dbsearchButton.setBounds(133, 13, 97, 40);
        dbrefreshButton.setBounds(586, 13, 120, 40);
        salesHistoryButton.setBounds(718, 13, 120, 40);
        exitButton.setBounds(835, 0, 45, 30);
        dbSearchField.setBounds(230, 13, 173, 40);
        dbtabScrollPane.setBounds(22, 200, 469, 430);
        topSellingScroll.setBounds(505, 330, 355, 125);
        stockScroll.setBounds(505, 485, 355, 135);
        summaryScroll.setBounds(505, 200, 355, 100);
        sortButton.setBounds(415, 13, 50, 40);
        dateSetDropdown.setBounds(12, 16, 109, 34);
        tableFilterDropdown.setBounds(465, 16, 109, 34);

        topBorder.add(exitButton);
        modulesPanel.add(userButton);
        modulesPanel.add(inventoryButton);
        modulesPanel.add(transactButton);
        modulesPanel.add(ordersButton);
        modulesPanel.add(accountButton);
        dbPanel.add(dbsearchButton);
        dbPanel.add(dbrefreshButton);
        dbPanel.add(salesHistoryButton);
        dbPanel.add(dbSearchField);
        dbPanel.add(dateSetDropdown);
        dbPanel.add(tableFilterDropdown);
        dbPanel.add(sortButton);

        add(dbCardPanel);
        dbCardPanel.add(modulesPanel, "Modules");
        dbCardPanel.add(dbPanel, "Dashboard");
        dbCardPanel.add(topBorder, "Top Border");
        dbCardPanel.add(bottomBorder, "Bottom Border");
        dbCardPanel.add(dbtabScrollPane, "salesTable");
        dbCardPanel.add(topSellingScroll);
        dbCardPanel.add(stockScroll);
        dbCardPanel.add(summaryScroll);
        dbCardPanel.add(salesLabel);
        dbCardPanel.add(topSellingLabel);
        dbCardPanel.add(stockLabel);
        dbCardPanel.add(recentSalesLabel);

        exitButton.addActionListener(e -> System.exit(0));
        salesHistoryButton.addActionListener(e -> Main.changeCard("SalesHistory"));
        dbsearchButton.addActionListener(e -> dashboardSearchDB());
        dbrefreshButton.addActionListener(e -> refreshDashboard());
        sortButton.addActionListener(e -> sortTable());
        dateSetDropdown.addActionListener(e -> dateTimeFilter((String) dateSetDropdown.getSelectedItem()));

        //dashboardPanel.getInventoryButton().addActionListener(e -> gotoInventory());
        //dashboardPanel.getTransactButton().addActionListener(e -> gotoTransact());
        //dashboardPanel.getOrdersButton().addActionListener(e -> gotoOrders());
        //dashboardPanel.getAccountButton().addActionListener(e -> gotoAccount());
        //dashboardPanel.getUserButton().addActionListener(e -> gotoUserAdmin());
        //dashboardPanel.getSalesHistoryButton().addActionListener(e -> gotoSalesHistory());
    }

    public void refreshDashboard() {
        dateSetDropdown.setSelectedIndex(0);
        dateTimeFilter("Today");
    }

    public void sortTable() {
        String selectedFilter = (String) tableFilterDropdown.getSelectedItem();

        if (selectedFilter != null) {
            TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) salesTable.getRowSorter();
            if (sorter == null) {
                sorter = new TableRowSorter<>(salesTableModel);
                salesTable.setRowSorter(sorter);
            }

            int columnIndex = switch (selectedFilter) {
                case "Sale ID" -> 0;
                case "Customer Name" -> 1;
                case "Quantity" -> 2;
                case "Total Price" -> 3;
                case "Order Status" -> 4;
                default -> -1;
            };

            if (columnIndex != -1) {
                ascending = !ascending;
                SortOrder order = ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING;
                RowSorter.SortKey sortKey = new RowSorter.SortKey(columnIndex, order);
                sorter.setSortKeys(Arrays.asList(sortKey));
                sorter.sort();
            }
        }
    }


    private void dashboardSearchDB() {
        String searchText = dbSearchField.getText().trim();
        String selectedFilter = tableFilterDropdown.getSelectedItem().toString();
        dashboardDB.searchDashboardDB(salesTableModel, searchText, selectedFilter);
    }

    private void dateTimeFilter(String periodSelector) {
        String condition = dashboardDB.getDateCondition(periodSelector);

        if (condition != null) {
            dashboardDB.loadSalesDataWithFilter(salesTableModel, condition);
            dashboardDB.loadSummaryData(summaryTableModel, condition);
            dashboardDB.loadTopSellingData(topSellingModel, condition);
        }

        stockLevelModel.setRowCount(0);
        dashboardDB.loadStockLevelData(stockLevelModel);

        salesTable.repaint();
        salesTable.revalidate();
        stockLevelTable.repaint();
        stockLevelTable.revalidate();
    }

    public JButton getInventoryButton() {
        return inventoryButton;
    }

    public JButton getTransactButton() {
        return transactButton;
    }

    public JButton getOrdersButton() {
        return ordersButton;
    }

    public JButton getAccountButton() {
        return accountButton;
    }

    public JButton getUserButton() {
        return userButton;
    }

    public JButton getSalesHistoryButton() {
        return salesHistoryButton;
    }
}