package Dashboard.SalesHistory;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.DB;

import javax.swing.*;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class salesHistoryDB {

    public Object[][] getAllSales() throws SQLException {
        String query = """
                SELECT s.sale_datetime, s.sale_id, s.customer_name, i.item_name, si.quantity,
                       si.price_sold, s.payment_method, s.employee_name
                FROM sales s
                JOIN sale_items si ON s.sale_id = si.sale_id
                JOIN inventory i ON si.item_id = i.item_id
                WHERE s.payment_method IS NOT NULL
                ORDER BY s.sale_datetime DESC
                """;
        return fetchSalesData(query);
    }

    public Object[][] getDateFilteredSales(java.sql.Date startDate, java.sql.Date endDate) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("""
                SELECT s.sale_datetime, s.sale_id, s.customer_name, i.item_name, si.quantity,
                       si.price_sold, s.payment_method, s.employee_name
                FROM sales s
                JOIN sale_items si ON s.sale_id = si.sale_id
                JOIN inventory i ON si.item_id = i.item_id
                WHERE s.payment_method IS NOT NULL
                """);

        if (startDate != null && endDate != null) {
            queryBuilder.append(" AND s.sale_datetime BETWEEN ? AND ?");
        } else if (startDate != null) {
            queryBuilder.append(" AND s.sale_datetime >= ?");
        } else if (endDate != null) {
            queryBuilder.append(" AND s.sale_datetime <= ?");
        }
        queryBuilder.append(" ORDER BY s.sale_datetime DESC");

        String query = queryBuilder.toString();

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int index = 1;
            if (startDate != null) stmt.setDate(index++, startDate);
            if (endDate != null) stmt.setDate(index++, endDate);

            return extractSalesData(stmt);
        }
    }

    public Object[][] searchSales(String keyword) throws SQLException {
        String query = """
                SELECT s.sale_datetime, s.sale_id, s.customer_name, i.item_name, si.quantity,
                       si.price_sold, s.payment_method, s.employee_name
                FROM sales s
                JOIN sale_items si ON s.sale_id = si.sale_id
                JOIN inventory i ON si.item_id = i.item_id
                WHERE s.payment_method IS NOT NULL
                  AND (
                        s.customer_name LIKE ?
                        OR i.item_name LIKE ?
                        OR CAST(s.sale_id AS CHAR) LIKE ?
                        OR s.employee_name LIKE ?
                      )
                ORDER BY s.sale_datetime DESC
                """;

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String searchTerm = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) stmt.setString(i, searchTerm);

            return extractSalesData(stmt);
        }
    }

    public void exportToPDF(JTable table) throws Exception {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream("Sales_Report.pdf"));
        doc.open();

        Image logo = Image.getInstance("C:/Users/Admin/IdeaProjects/Inventory/src/main/resources/Atlas-Feeds-Logo.png");
        logo.setAbsolutePosition(365, 725);
        logo.scaleToFit(204, 83);
        doc.add(logo);

        doc.add(new Paragraph("\n\n\n"));
        doc.add(new Paragraph("Sales Report\n\n ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        doc.add(new Paragraph(" "));

        PdfPTable pdfTable = new PdfPTable(8);
        pdfTable.setWidths(new int[]{4, 2, 4, 4, 2, 2, 2, 3});
        pdfTable.setTotalWidth(500);
        pdfTable.setLockedWidth(true);


        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                Object value = table.getValueAt(i, j);
                pdfTable.addCell(value != null ? value.toString() : "");
            }
        }

        doc.add(pdfTable);
        doc.close();
    }

    private Object[][] fetchSalesData(String query) throws SQLException {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            return processResultSet(rs);
        }
    }

    private Object[][] extractSalesData(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            return processResultSet(rs);
        }
    }

    private Object[][] processResultSet(ResultSet rs) throws SQLException {
        List<Object[]> dataList = new ArrayList<>();
        while (rs.next()) {
            double total = rs.getInt("quantity") * rs.getDouble("price_sold");
            dataList.add(new Object[]{
                    rs.getString("sale_datetime"),
                    rs.getInt("sale_id"),
                    rs.getString("customer_name"),
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    total,
                    rs.getString("payment_method"),
                    rs.getString("employee_name")
            });
        }
        return dataList.toArray(new Object[0][]);
    }
}
