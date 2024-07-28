package pl.urban.korkpys_desktop.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
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

    private final InvoiceRepository invoiceRepository = new InvoiceRepository();

    private final ObservableList<Invoice> observableInvoices = FXCollections.observableArrayList();


    public void initialize() {
        invoicesListView.setCellFactory(listView -> new ListCell<Invoice>() {
            @Override
            protected void updateItem(Invoice invoice, boolean empty) {
                super.updateItem(invoice, empty);
                if (empty || invoice == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10); // Increase spacing to add more space around the labels
                    hbox.setPrefHeight(50); // Increase the height of the container
                    hbox.setAlignment(Pos.CENTER_LEFT); // Ensure content is aligned to the left and centered vertically

                    Label monthLabel = new Label(invoice.getInvoiceMonth());
                    monthLabel.setPrefWidth(400); // Maintain the month label width
                    monthLabel.setStyle("-fx-font-size: 35px; -fx-alignment: center-left;"); // Center text vertically and align left

                    Label yearLabel = new Label(invoice.getInvoiceYear());
                    yearLabel.setStyle("-fx-font-size: 35px; -fx-alignment: center-left;"); // Center text vertically and align left

                    hbox.getChildren().addAll(monthLabel, yearLabel);
                    setGraphic(hbox); // Use HBox as the graphic for the cell
                }
            }
        });
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
        observableInvoices.clear(); // Clear existing items before adding new ones
        observableInvoices.addAll(invoices);
        invoicesListView.setItems(observableInvoices); // Ensure this line is present
    }

    @FXML
    private void handleAddInvoice(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/urban/korkpys_desktop/add-invoice-view.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the customerId
            AddInvoiceViewController controller = loader.getController();
            controller.setCustomerId(customer.getId()); // Assuming 'customer' is the current customer object

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @FXML
    private void handleDeleteInvoice() {
        Invoice selectedInvoice = invoicesListView.getSelectionModel().getSelectedItem();
        if (selectedInvoice != null) {
            invoiceRepository.deleteInvoiceById(selectedInvoice.getId());
            loadInvoices(); // Refresh the invoice list
        } else {
            // Optionally, show an alert to select an invoice
        }
    }
}