package pl.urban.korkpys_desktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.urban.korkpys_desktop.db.Repository.CustomerRepository;
import pl.urban.korkpys_desktop.db.Repository.InvoiceRepository;
import pl.urban.korkpys_desktop.db.model.Customer;
import pl.urban.korkpys_desktop.db.model.Invoice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AddInvoiceViewController {

    @FXML
    private ComboBox<String> monthInput;
    @FXML
    private TextField yearInput;
    @FXML
    private Button uploadImageButton;
    @FXML
    private Button saveInvoiceButton;
    @FXML
    private ImageView invoiceImageView;
    @FXML
    private Label imageInfoLabel;

    private File selectedFile;
    private final InvoiceRepository invoiceRepository = new InvoiceRepository();
    private Long customerId;

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @FXML
    public void initialize() {
        uploadImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz obraz faktury");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                invoiceImageView.setImage(image);
                imageInfoLabel.setText(selectedFile.getName());
            } else {
                imageInfoLabel.setText("Nie wybrano pliku");
            }
        });

        saveInvoiceButton.setOnAction(event -> {
            try {
                String invoiceMonth = monthInput.getValue();
                String invoiceYear = yearInput.getText();

                if (selectedFile != null && !invoiceMonth.isEmpty() && !invoiceYear.isEmpty() && customerId != null) {
                    byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());

                    Invoice invoice = new Invoice();
                    invoice.setImage(imageBytes);
                    invoice.setInvoiceMonth(invoiceMonth);
                    invoice.setInvoiceYear(invoiceYear);
                    invoice.setCustomerId(customerId);

                    invoiceRepository.addInvoice(invoice);

                    imageInfoLabel.setText("Faktura dodana pomyślnie");

                    handleReturnToHelloView();
                } else {
                    imageInfoLabel.setText("Proszę wypełnić wszystkie pola, wybrać obraz i klienta");
                }
            } catch (IOException e) {
                e.printStackTrace();
                imageInfoLabel.setText("Błąd podczas dodawania faktury");
            }
        });
    }

    public void handleReturnToHelloView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/urban/korkpys_desktop/hello-view.fxml"));
            VBox mainView = loader.load();

            Stage stage = (Stage) saveInvoiceButton.getScene().getWindow();
            stage.getScene().setRoot(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}