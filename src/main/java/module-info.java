module pl.urban.korkpys_desktop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens pl.urban.korkpys_desktop to javafx.fxml;
    exports pl.urban.korkpys_desktop;
}