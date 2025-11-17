package banking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle("Banking System");

        loadScreen("/banking/view/WelcomeScreen.fxml");

        primaryStage.show();
    }

    public static void loadScreen(String fxmlPath) {
        try {
            System.out.println("Loading FXML file from: " + MainApp.class.getResource(fxmlPath));
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Error loading screen: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}