package pl.urban.korkpys_desktop.db.Repository;

import pl.urban.korkpys_desktop.db.connect.DatabaseConnection;
import pl.urban.korkpys_desktop.db.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerRepository {
    private List<Customer> allCustomers;

    public List<Customer> searchCustomers(String searchQuery) {
        return allCustomers.stream()
                .filter(customer -> customer.getName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        (customer.getStreet() + " " + customer.getBuildingNumber()).toLowerCase().contains(searchQuery.toLowerCase()) ||
                        customer.getMail().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Customer> getAllCustomers() {
        if (allCustomers == null) {
            loadAllCustomersFromDatabase();
        }
        return allCustomers;
    }

    private void loadAllCustomersFromDatabase() {
        allCustomers = new ArrayList<>();
        String sql = "SELECT * FROM kork.customers";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("id"));
                customer.setName(rs.getString("name"));
                customer.setMail(rs.getString("mail"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                customer.setStreet(rs.getString("street"));
                customer.setBuildingNumber(rs.getString("building_number"));
                customer.setCity(rs.getString("city"));
                customer.setPostalCode(rs.getString("postal_code"));
                customer.setCustomerCode(rs.getString("customer_code"));

                allCustomers.add(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerById(Long id) {
    return allCustomers.stream()
            .filter(customer -> customer.getId().equals(id))
            .findFirst()
            .orElse(null);
}
}
