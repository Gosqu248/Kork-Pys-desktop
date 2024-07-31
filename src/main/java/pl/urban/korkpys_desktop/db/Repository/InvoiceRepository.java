package pl.urban.korkpys_desktop.db.Repository;

import okhttp3.*;
import pl.urban.korkpys_desktop.db.connect.DatabaseConnection;
import pl.urban.korkpys_desktop.db.model.Invoice;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository {

    private static final String SERVER_URL = "http://localhost:8080/api/invoices";
    private List<Invoice> allInvoices;

    public List<Invoice> getAllInvoicesByCustomerId(Long id) {
        allInvoices = new ArrayList<>();
        String sql = "SELECT * FROM kork.invoices WHERE customer_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setId(rs.getLong("id"));
                    invoice.setImage(rs.getString("image"));
                    invoice.setInvoiceMonth(rs.getString("invoice_month"));
                    invoice.setInvoiceYear(rs.getString("invoice_year"));
                    invoice.setCustomerId(rs.getLong("customer_id"));
                    allInvoices.add(0, invoice);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allInvoices;
    }

    public void deleteInvoiceById(Long id) {
        String sql = "DELETE FROM kork.invoices WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addInvoice(Invoice invoice) {
        String sql = "INSERT INTO kork.invoices (image, invoice_month, invoice_year, customer_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, invoice.getImage());
            pstmt.setString(2, invoice.getInvoiceMonth());
            pstmt.setString(3, invoice.getInvoiceYear());
            pstmt.setLong(4, invoice.getCustomerId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

