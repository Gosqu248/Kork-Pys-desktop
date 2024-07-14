package pl.urban.korkpys_desktop.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.urban.korkpys_desktop.db.Repository.CustomerRepository;
import pl.urban.korkpys_desktop.db.Repository.InvoiceRepository;
import pl.urban.korkpys_desktop.db.model.Customer;
import pl.urban.korkpys_desktop.db.model.Invoice;

import java.io.IOException;
import java.util.List;

public class CustomerViewController {

    @FXML
    private VBox customerDetailsVBox;

    @FXML
    private ListView<Invoice> invoicesListView;

    @FXML
    private Button addInvoiceButton;

    private Customer customer;

    private InvoiceRepository invoiceRepository = new InvoiceRepository();

    private ObservableList<Invoice> observableInvoices = FXCollections.observableArrayList();




    public void initialize() {
        // Tutaj można zainicjalizować widok, np. wczytać faktury
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        displayCustomerDetails();
        loadInvoices();
    }

    private void displayCustomerDetails() {
        customerDetailsVBox.getChildren().clear();

        if (customer != null) {
            Label dane = new Label("Dane klienta:");
            dane.setStyle("-fx-font-size: 30px;");

            // Tworzenie separatorów
            Pane spacer1 = new Pane();
            spacer1.setMinHeight(20); // Zwiększenie odstępu między 'dane' a 'nameLabel'

            Label nameLabel = new Label(customer.getName());
            nameLabel.setStyle("-fx-font-size: 25px;");

            Pane spacer2 = new Pane();
            spacer2.setMinHeight(10); // Zwiększenie odstępu między 'nameLabel' a 'addressLabel'

            Label addressLabel = new Label(customer.getStreet() + " " + customer.getBuildingNumber());
            addressLabel.setStyle("-fx-font-size: 25px;");

            Pane spacer3 = new Pane();
            spacer3.setMinHeight(10); // Zwiększenie odstępu między 'addressLabel' a 'zip'

            Label zip = new Label((customer.getPostalCode() + " " + customer.getCity()));
            zip.setStyle("-fx-font-size: 20px;");

            Pane spacer4 = new Pane();
            spacer4.setMinHeight(10); // Zwiększenie odstępu między 'zip' a 'mailLabel'

            Label mailLabel = new Label(customer.getMail());
            mailLabel.setStyle("-fx-font-size: 20px;");

            Pane spacer5 = new Pane();
            spacer5.setMinHeight(10);

            Label phoneLabel = new Label(customer.getPhoneNumber());
            phoneLabel.setStyle("-fx-font-size: 20px;");


            customerDetailsVBox.getChildren().addAll(dane, spacer1, nameLabel, spacer2, addressLabel, spacer3, zip, spacer4, mailLabel, spacer5, phoneLabel);
        }
    }

    private void loadInvoices() {
        List<Invoice> invoices = invoiceRepository.getAllInvoicesByCustomerId(customer.getId());
        observableInvoices.addAll(invoices);
    }

    @FXML
    private void handleAddInvoice() {
        // Obsługa dodawania nowej faktury
    }

    @FXML
private void handleReturnToMainView() {
    try {
        // Load the hello-view.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/urban/korkpys_desktop/hello-view.fxml"));
        VBox mainView = loader.load();

        // Get the current stage from any control, here using the addInvoiceButton
        Stage stage = (Stage) addInvoiceButton.getScene().getWindow();
        // Set the scene with hello-view
        stage.getScene().setRoot(mainView);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}