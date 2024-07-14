package pl.urban.korkpys_desktop.db.Repository;

import pl.urban.korkpys_desktop.db.connect.DatabaseConnection;
import pl.urban.korkpys_desktop.db.model.Invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository {

    private List<Invoice> allInvoices;

    public List<Invoice> getAllInvoicesByCustomerId(Long id) {
        allInvoices = new ArrayList<>();
        String sql = "SELECT * FROM kork.invoices WHERE customer_id = id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setId(rs.getLong("id"));
                invoice.setImage(rs.getString("image"));
                invoice.setInvoiceMonth(rs.getString("invoice_month"));
                invoice.setInvoiceYear(rs.getString("invoice_year"));
                invoice.setCustomerId(rs.getLong("customer_id"));

                allInvoices.add(invoice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allInvoices;
    }

}
