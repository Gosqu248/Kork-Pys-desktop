package pl.urban.korkpys_desktop.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import pl.urban.korkpys_desktop.db.Repository.CustomerRepository;
import pl.urban.korkpys_desktop.db.model.Customer;

import java.io.IOException;
import java.util.List;

public class HelloController {

    @FXML
    private TextField searchUserField;

    @FXML
    private ListView<Customer> searchResultsList;

    @FXML
    private VBox mainPane;

    private CustomerRepository customerRepository = new CustomerRepository();
    private ObservableList<Customer> observableCustomers = FXCollections.observableArrayList();

    public void initialize() {
        getAllCustomers();
        searchUserField.textProperty().addListener((observable, oldValue, newValue) -> searchCustomers(newValue));
        setupCustomerListView();
    }

    public void getAllCustomers() {
        List<Customer> customers = customerRepository.getAllCustomers();
        observableCustomers.addAll(customers);
        searchResultsList.setItems(observableCustomers);
    }

    private void searchCustomers(String searchQuery) {
        if (!searchQuery.isEmpty()) {
            List<Customer> matchingCustomers = customerRepository.searchCustomers(searchQuery);
            observableCustomers.clear();
            observableCustomers.addAll(matchingCustomers);
        } else {
            getAllCustomers();
        }
    }

    private void setupCustomerListView() {
        searchResultsList.setCellFactory(new Callback<ListView<Customer>, ListCell<Customer>>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> listView) {
                return new ListCell<Customer>() {
                    @Override
                    protected void updateItem(Customer customer, boolean empty) {
                        super.updateItem(customer, empty);
                        if (empty || customer == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox();
                            Text name = new Text(customer.getName());
                            Text address = new Text(customer.getStreet() + " " + customer.getBuildingNumber());
                            Text mail = new Text(customer.getMail());

                            name.setStyle("-fx-font-size: 27px;");
                            address.setStyle("-fx-font-size: 25px;");
                            mail.setStyle("-fx-font-size: 20px;");

                            StackPane namePane = new StackPane(name);
                            StackPane addressPane = new StackPane(address);
                            StackPane mailPane = new StackPane(mail);

                            namePane.setAlignment(Pos.CENTER);
                            addressPane.setAlignment(Pos.CENTER);
                            mailPane.setAlignment(Pos.CENTER);

                            // Set HBox children widths to percentages
                            HBox.setHgrow(name, Priority.ALWAYS);
                            HBox.setHgrow(address, Priority.ALWAYS);
                            HBox.setHgrow(mail, Priority.ALWAYS);

                            name.wrappingWidthProperty().bind(searchResultsList.widthProperty().multiply(0.4));
                            address.wrappingWidthProperty().bind(searchResultsList.widthProperty().multiply(0.25));
                            mail.wrappingWidthProperty().bind(searchResultsList.widthProperty().multiply(0.2));

                            hbox.getChildren().addAll(name, address, mail);
                            hbox.setSpacing(10);


                            // Increase the height of the container for each customer
                            hbox.setMinHeight(150); // Assuming the base height is 60, adjust as needed

                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
        searchResultsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !searchResultsList.getSelectionModel().isEmpty()) {
                navigateToCustomerView(searchResultsList.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void navigateToCustomerView(Customer customer) {
        try {
            // Update the resource path to match the new file name
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/urban/korkpys_desktop/customer-view.fxml"));
            mainPane.getChildren().setAll((Node) loader.load()); // Assuming `mainPane` is the container you're updating
            CustomerViewController controller = loader.getController();
            controller.setCustomer(customer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}