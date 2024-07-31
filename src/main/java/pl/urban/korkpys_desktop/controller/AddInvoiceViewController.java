package pl.urban.korkpys_desktop.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.urban.korkpys_desktop.db.Repository.InvoiceRepository;
import pl.urban.korkpys_desktop.db.model.Invoice;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.Map;
import java.util.stream.IntStream;

public class AddInvoiceViewController {

    @FXML private ComboBox<String> monthInput;
    @FXML private ComboBox<Integer> yearInput;
    @FXML private Button uploadImageButton;
    @FXML private Button saveInvoiceButton;
    @FXML private ImageView invoiceImageView;
    @FXML private Label imageInfoLabel;

    private File selectedFile;
    private final InvoiceRepository invoiceRepository = new InvoiceRepository();
    private Long customerId;

    private final Dotenv dotenv = Dotenv.load();
    private final Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @FXML
    public void initialize() {
        int currentYear = Year.now().getValue();

        yearInput.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(2022, 2040).boxed().toList()
        ));

        if(currentYear >= 2022 && currentYear <= 2040) {
            yearInput.setValue(currentYear);
        } else {
            yearInput.setValue(2022);
        }


        uploadImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz obraz faktury");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Downloads"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF files", "*.pdf"),
                    new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg"));
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
                String invoiceYear = String.valueOf(yearInput.getValue());

                if (selectedFile != null && !invoiceMonth.isEmpty() && !invoiceYear.isEmpty() && customerId != null) {
                    Map uploadResult = cloudinary.uploader().upload(selectedFile, ObjectUtils.asMap(
                            "transformation", new Transformation()
                                    .width("auto").crop("scale")
                                    .quality("auto")
                                    .fetchFormat("auto")
                    ));
                    String imageUrl = (String) uploadResult.get("url");

                    Invoice invoice = new Invoice();
                    invoice.setImage(imageUrl);
                    invoice.setInvoiceMonth(invoiceMonth);
                    invoice.setInvoiceYear(invoiceYear);
                    invoice.setCustomerId(customerId);

                    invoiceRepository.addInvoice(invoice);

                    imageInfoLabel.setText("Faktura dodana pomyślnie");
                    System.out.println("Faktura dodana pomyślnie");

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