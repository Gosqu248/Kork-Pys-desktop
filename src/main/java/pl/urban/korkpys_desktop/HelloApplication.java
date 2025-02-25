package pl.urban.korkpys_desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
public void start(Stage stage) {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Eco Kork-Pyś App");

        javafx.geometry.Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());

        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace(); // Handle the exception as appropriate for your application
    }
}

    public static void main(String[] args) {
        launch(args);

    }
}