package gui;

import gui.login.LoginController;
import gui.manufacturer.ManufacturerController;
import gui.viewer.ViewerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage=primaryStage;
        showLoginPage();
    }

    public void showLoginPage() throws Exception {
        LoginController.setMain(this);
        stage.setTitle("Login");
        stage.setScene(new Scene(getRoot("login/login.fxml"), 600, 400));
        stage.show();
    }

    public void showViewerPage() throws Exception{
        ViewerController.setMain(this);
        stage.setTitle("Viewer");
        stage.setScene(new Scene(getRoot("viewer/viewer.fxml"), 1024, 600));
        stage.show();
    }

    public void showManufacturerPage() throws Exception{
        ManufacturerController.setMain(this);
        stage.setTitle("Manufacturer");
        stage.setScene(new Scene(getRoot("manufacturer/manufacturer.fxml"), 1024, 600));
        stage.show();
    }

    private Parent getRoot(String fxml) throws Exception{
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        return loader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
