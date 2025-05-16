package Dashboard;

import main.DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class dashboardDB {

    public static void loadSalesData(DefaultTableModel salesTableModel, JTable salesTable) {
        salesTableModel.setRowCount(0);

        String query = """
                    SELECT s.sale_id, s.customer_name, 
                           SUM(si.quantity) AS total_quantity, 
                           s.total_price, 
                           'Completed' AS order_status
                    FROM sales s
                    JOIN sale_items si ON s.sale_id = si.sale_id
                    GROUP BY s.sale_id
                    ORDER BY s.sale_id DESC
                    """;

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = "SI" + rs.getInt("sale_id");
                row[1] = rs.getString("customer_name");
                row[2] = rs.getInt("total_quantity");
                row[3] = rs.getBigDecimal("total_price");
                row[4] = rs.getString("order_status");

                salesTableModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading sales data: " + e.getMessage());
        }
    }

    public static void loadSummaryData(DefaultTableModel summaryTableModel, String whereCondition) {
        summaryTableModel.setRowCount(0);

        String query = """
                    SELECT 
                        IFNULL(SUM(s.total_price), 0) AS total_revenue,
                        COUNT(DISTINCT s.sale_id) AS total_orders,
                        IFNULL(SUM(si.quantity), 0) AS total_quantity
                    FROM sales s
                    JOIN sale_items si ON s.sale_id = si.sale_id
                    %s
                """.formatted(whereCondition != null ? "WHERE " + whereCondition : "");

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Object[] summaryRow = {
                        rs.getBigDecimal("total_revenue"),
                        rs.getInt("total_orders"),
                        rs.getInt("total_quantity")
                };
                summaryTableModel.addRow(summaryRow);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading summary: " + e.getMessage());
        }
    }

    public static void loadTopSellingData(DefaultTableModel topSellingModel, String whereCondition) {
        topSellingModel.setRowCount(0);
        String query = """
                    SELECT i.item_name, SUM(si.quantity) AS total_quantity, 
                           SUM(si.price_sold * si.quantity) AS total_price
                    FROM sale_items si
                    JOIN inventory i ON si.item_id = i.item_id
                    JOIN sales s ON s.sale_id = si.sale_id
                    %s
                    GROUP BY si.item_id
                    ORDER BY total_quantity DESC
                    LIMIT 15
                """.formatted(whereCondition != null ? "WHERE " + whereCondition : "");

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getString("item_name"),
                        rs.getInt("total_quantity"),
                        rs.getBigDecimal("total_price")
                };
                topSellingModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading top-selling data: " + e.getMessage());
        }
    }

    public static void loadSalesDataWithFilter(DefaultTableModel salesTableModel, String condition) {
        salesTableModel.setRowCount(0);

        String query = """
                    SELECT s.sale_id, s.customer_name, 
                           SUM(si.quantity) AS total_quantity, 
                           s.total_price, 
                           'Completed' AS order_status
                    FROM sales s
                    JOIN sale_items si ON s.sale_id = si.sale_id
                    WHERE %s
                    GROUP BY s.sale_id
                    ORDER BY s.sale_datetime DESC
                """.formatted(condition);

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = "SI" + rs.getInt("sale_id");
                row[1] = rs.getString("customer_name");
                row[2] = rs.getInt("total_quantity");
                row[3] = rs.getBigDecimal("total_price");
                row[4] = rs.getString("order_status");

                salesTableModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading filtered sales data: " + e.getMessage());
        }
    }

    public static void searchDashboardDB(DefaultTableModel salesTableModel, String searchText, String selectedFilter) {
        String column = switch (selectedFilter) {
            case "Sale ID" -> "s.sale_id";
            case "Customer Name" -> "s.customer_name";
            case "Quantity" -> "total_quantity";
            case "Total Price" -> "s.total_price";
            case "Order Status" -> "order_status";
            default -> "";
        };

        if (searchText.isEmpty() || column.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a search term.");
            return;
        }

        String query = """
                    SELECT s.sale_id, s.customer_name, 
                           SUM(si.quantity) AS total_quantity, 
                           s.total_price, 
                           'Completed' AS order_status
                    FROM sales s
                    JOIN sale_items si ON s.sale_id = si.sale_id
                    GROUP BY s.sale_id
                    HAVING %s LIKE ?
                    ORDER BY s.sale_id DESC
                """.formatted(column);

        salesTableModel.setRowCount(0);
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = "SI" + rs.getInt("sale_id");
                row[1] = rs.getString("customer_name");
                row[2] = rs.getInt("total_quantity");
                row[3] = rs.getBigDecimal("total_price");
                row[4] = rs.getString("order_status");

                salesTableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error during search: " + e.getMessage());
        }
    }

    public static String getDateCondition(String periodSelector) {
        if (periodSelector == null) {
            periodSelector = "Today";
        }
        return switch (periodSelector) {
            case "Today" -> "date(s.sale_datetime) = curdate()";
            case "This Week" -> "YEARWEEK(s.sale_datetime, 1) = YEARWEEK(CURDATE(), 1)";
            case "This Month" -> "MONTH(s.sale_datetime) = MONTH(CURDATE()) AND YEAR(s.sale_datetime) = YEAR(CURDATE())";
            default -> null;
        };
    }

    public static void loadStockLevelData(DefaultTableModel stockLevelModel) {
        stockLevelModel.setRowCount(0);

        String query = """
                SELECT item_name, item_type, location, stock
                FROM inventory
                WHERE stock <= 15
                ORDER BY stock ASC
                """;

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            int rowCount = 0;
            while (rs.next()) {
                Object[] row = {
                        rs.getString("item_name"),
                        rs.getString("item_type"),
                        rs.getString("location"),
                        rs.getInt("stock")
                };
                stockLevelModel.addRow(row);
                rowCount++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading stock level data: " + e.getMessage());
        }
    }
}
