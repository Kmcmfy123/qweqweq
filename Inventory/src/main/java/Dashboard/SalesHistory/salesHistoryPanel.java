package Dashboard.SalesHistory;

import main.Main;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import javax.swing.RowSorter.SortKey;

import java.sql.SQLException;

public class salesHistoryPanel extends JPanel {

    private static final String[] salesHistoryColumn = {"Date", "Sale ID", "Customer Name", "Products", "Qty", "Total", "Payment", "Sold By"};
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> tableSort;
    private JDateChooser startDateChooser, endDateChooser;

    private salesHistoryDB dbHandler;

    public salesHistoryPanel() {
        dbHandler = new salesHistoryDB();
        salesHistoryUI();
        loadData();
    }

    public void salesHistoryUI() {
        setLayout(null);

        model = new DefaultTableModel(salesHistoryColumn, 0) {
            @Override
            public Class getColumnClass(int columnIndex) {
                if (columnIndex == 1 || columnIndex == 4) {
                    return Integer.class;
                } else if (columnIndex == 5) {
                    return Double.class;
                } else {
                    return String.class;
                }
            }
        };

        table = new JTable(model);

        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();

        tableSort = new JComboBox<>(salesHistoryColumn);


        JPanel
                topBorder = new JPanel(null),
                bottomBorder = new JPanel(null),
                reportHistory = new JPanel(null);
        JButton
                exportButton = new JButton("Export Report"),
                backButton = new JButton("<"),
                searchButton = new JButton("Search"),
                refreshButton = new JButton("Refresh"),
                sortButton = new JButton("↑↓"),
                setDateButton = new JButton("Set Date");
        JTextField
                searchField = new JTextField();
        JLabel
                startDateLabel = new JLabel("Select Start Date"),
                endDateLabel = new JLabel("Select End Date");
        JScrollPane
                scrollPane = new JScrollPane(table);

        setBackground(Color.decode("#014518"));
        reportHistory.setBackground(Color.decode("#ff9933"));
        topBorder.setBackground(Color.decode("#ff9933"));
        bottomBorder.setBackground(Color.decode("#ff9933"));
        exportButton.setBackground(Color.decode("#014518"));
        exportButton.setForeground(Color.decode("#ffffff"));
        searchButton.setBackground(Color.decode("#014518"));
        searchButton.setForeground(Color.decode("#ffffff"));
        refreshButton.setBackground(Color.decode("#014518"));
        refreshButton.setForeground(Color.decode("#ffffff"));
        setDateButton.setBackground(Color.decode("#014518"));
        setDateButton.setForeground(Color.decode("#ffffff"));
        sortButton.setBackground(Color.decode("#014518"));
        sortButton.setForeground(Color.decode("#ffffff"));
        startDateLabel.setForeground(Color.decode("#ffffff"));
        endDateLabel.setForeground(Color.decode("#ffffff"));
        tableSort.setBackground(Color.decode("#014518"));
        tableSort.setForeground(Color.decode("#ffffff"));

        //salesHistoryPanel
        setBounds(0, 0, 880, 660);
        reportHistory.setBounds(40, 43, 800, 110);
        topBorder.setBounds(0, 0, 880, 30);
        bottomBorder.setBounds(0, 630, 880, 30);
        backButton.setBounds(0, 0, 45, 30);
        searchButton.setBounds(16, 10, 98, 40);
        refreshButton.setBounds(16, 60, 98, 40);
        exportButton.setBounds(671, 60, 113, 40);
        sortButton.setBounds(611, 10, 50, 40);
        setDateButton.setBounds(283, 10, 100, 40);
        searchField.setBounds(114, 10, 137, 40);
        startDateChooser.setBounds(388, 10, 100, 40);
        endDateChooser.setBounds(388, 55, 100, 40);
        startDateLabel.setBounds(490, 10, 120, 30);
        endDateLabel.setBounds(490, 55, 120, 30);
        tableSort.setBounds(661, 13, 122, 34);
        scrollPane.setBounds(30, 166, 820, 530);

        reportHistory.add(searchButton);
        reportHistory.add(refreshButton);
        reportHistory.add(exportButton);
        reportHistory.add(sortButton);
        reportHistory.add(searchField);
        reportHistory.add(startDateChooser);
        reportHistory.add(endDateChooser);
        reportHistory.add(startDateLabel);
        reportHistory.add(endDateLabel);
        reportHistory.add(tableSort);
        reportHistory.add(setDateButton);

        add(topBorder);
        add(bottomBorder);
        add(backButton);
        add(scrollPane);
        add(reportHistory);

        topBorder.add(backButton);

        backButton.addActionListener(e -> Main.changeCard("Dashboard"));
        exportButton.addActionListener(e -> exportToPDF());
        setDateButton.addActionListener(e -> setDate());
        refreshButton.addActionListener(e -> loadData());
        searchButton.addActionListener(e -> searchData(searchField.getText()));
        sortButton.addActionListener(e -> sortTable());
    }

    private void setDate() {
        model.setRowCount(0);

        java.util.Date startDate = startDateChooser.getDate();
        java.util.Date endDate = endDateChooser.getDate();

        java.sql.Date sqlStartDate = (startDate != null) ? new java.sql.Date(startDate.getTime()) : null;
        java.sql.Date sqlEndDate = (endDate != null) ? new java.sql.Date(endDate.getTime()) : null;

        try {
            Object[][] data = dbHandler.getDateFilteredSales(sqlStartDate, sqlEndDate);
            for (Object[] row : data) {
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadData() {
        model.setRowCount(0);

        try {
            Object[][] data = dbHandler.getAllSales();
            for (Object[] row : data) {
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void exportToPDF() {
        try {
            dbHandler.exportToPDF(table);
            JOptionPane.showMessageDialog(this, "PDF saved as Sales_Report.pdf");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "PDF Error: " + e.getMessage());
        }
    }

    private void searchData(String keyword) {
        keyword = keyword.trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.");
            return;
        }

        model.setRowCount(0);

        try {
            Object[][] data = dbHandler.searchSales(keyword);
            for (Object[] row : data) {
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Search Error: " + e.getMessage());
        }
    }

    private void sortTable() {
        String selectedColumn = (String) tableSort.getSelectedItem();
        if (selectedColumn == null) return;

        int columnIndex = -1;
        for (int i = 0; i < salesHistoryColumn.length; i++) {
            if (salesHistoryColumn[i].equals(selectedColumn)) {
                columnIndex = i;
                break;
            }
        }

        if (columnIndex == -1) return;


        boolean ascending = table.getRowSorter() == null ||
                !((DefaultRowSorter<?, ?>) table.getRowSorter()).getSortKeys().stream()
                        .anyMatch(key -> key.getSortOrder() == SortOrder.ASCENDING);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortKeys(java.util.List.of(new SortKey(columnIndex,
                ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING)));

        table.setRowSorter(sorter);
    }
}