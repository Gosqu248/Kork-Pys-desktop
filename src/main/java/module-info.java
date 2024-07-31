module pl.urban.korkpys_desktop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires okhttp3;
    requires dotenv.java;
    requires cloudinary.core;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens pl.urban.korkpys_desktop to javafx.fxml;
    exports pl.urban.korkpys_desktop;
    exports pl.urban.korkpys_desktop.controller;
    opens pl.urban.korkpys_desktop.controller to javafx.fxml;
}