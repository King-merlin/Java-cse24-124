package banking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Banking System");

        System.out.println("=== Application Starting ===");
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        // List what resources are available
        debugResourcePath();

        boolean loaded = loadScreen("/banking/view/WelcomeScreen.fxml");

        if (!loaded) {
            // Show error scene if FXML fails to load
            showErrorScene();
        }

        primaryStage.show();
    }

    private void debugResourcePath() {
        System.out.println("\n=== Checking Resource Paths ===");

        String[] pathsToCheck = {
                "/banking/view/WelcomeScreen.fxml",
                "/view/WelcomeScreen.fxml",
                "banking/view/WelcomeScreen.fxml",
                "/WelcomeScreen.fxml"
        };

        for (String path : pathsToCheck) {
            URL url = getClass().getResource(path);
            System.out.println("Path: " + path + " -> " + (url != null ? "FOUND" : "NOT FOUND"));
            if (url != null) {
                System.out.println("  Full URL: " + url);
            }
        }

        // Check if file exists in file system
        File resourceDir = new File("src/main/resources/banking/view");
        System.out.println("\nChecking file system:");
        System.out.println("Directory exists: " + resourceDir.exists());
        if (resourceDir.exists() && resourceDir.isDirectory()) {
            File[] files = resourceDir.listFiles();
            System.out.println("Files in directory:");
            if (files != null) {
                for (File file : files) {
                    System.out.println("  - " + file.getName());
                }
            }
        }
    }

    public static boolean loadScreen(String fxmlPath) {
        try {
            System.out.println("\n=== Attempting to load: " + fxmlPath + " ===");

            URL resourceUrl = MainApp.class.getResource(fxmlPath);
            System.out.println("Resource URL: " + resourceUrl);

            if (resourceUrl == null) {
                System.err.println("ERROR: FXML file not found at: " + fxmlPath);
                return false;
            }

            System.out.println("Loading FXML from: " + resourceUrl);
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            System.out.println("Successfully loaded: " + fxmlPath);
            return true;

        } catch (IOException e) {
            System.err.println("ERROR: Failed to load screen: " + fxmlPath);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("UNEXPECTED ERROR:");
            e.printStackTrace();
            return false;
        }
    }

    private void showErrorScene() {
        System.out.println("\n=== Showing Error Scene ===");

        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 40; -fx-alignment: center;");

        Label titleLabel = new Label("⚠️ FXML Files Not Found");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: red;");

        Label infoLabel = new Label(
                "The FXML resource files could not be loaded.\n\n" +
                        "Please check:\n" +
                        "1. FXML files exist in: src/main/resources/banking/view/\n" +
                        "2. Run 'mvn clean compile' to rebuild\n" +
                        "3. Check the console output for the exact error\n\n" +
                        "Required files:\n" +
                        "  • WelcomeScreen.fxml\n" +
                        "  • CustomerDashboard.fxml\n" +
                        "  • AdminDashboard.fxml\n" +
                        "  • CustomerView.fxml\n" +
                        "  • StaffView.fxml"
        );
        infoLabel.setStyle("-fx-font-size: 14px;");

        root.getChildren().addAll(titleLabel, infoLabel);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}