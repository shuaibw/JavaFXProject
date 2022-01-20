package gui.login;

import client.Client;
import gui.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import server.UserData;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginButton;

    @FXML
    private Button reconnectButton;
    @FXML
    private Label serverStat;
    private static final String serverName = "localhost";
    private static final int port = 2222;
    private static Client client;
    private static Main main;

    public static void setMain(Main main) {
        LoginController.main = main;
    }

    public static Client getClient() {
        return client;
    }

    @FXML
    public void initialize() {
        loginButton.setDisable(true);
        registerButton.setDisable(true);
        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            if(client==null || !client.isOpen()) {
                try {
                    client = new Client(serverName, port);
                    System.out.println("Connected to server");
                    Platform.runLater(() -> {
                        serverStat.setText("Connected");
                        serverStat.setTextFill(Color.GREEN);
                    });
                } catch (Exception e) {
                    System.out.println("Failed to connect with server");
                    Platform.runLater(() -> {
                        serverStat.setText("Server down");
                        serverStat.setTextFill(Color.RED);
                    });
                }
            }
        }).start();
    }

    @FXML
    private void handleButtonActivation() {
        String userName = nameField.getText().strip();
        String password = passwordField.getText();
        boolean disabled = userName.isEmpty() || password.isEmpty();
        if (userName.equalsIgnoreCase("viewer")) {
            passwordField.clear();
            passwordField.setDisable(true);
            loginButton.setDisable(false);
            registerButton.setDisable(true);
        } else {
            passwordField.setDisable(false);
            loginButton.setDisable(disabled);
            registerButton.setDisable(disabled);
        }
    }

    @FXML
    private void handleRegistration() {
        String username=nameField.getText().strip();
        String password=passwordField.getText();
        if(client==null || !client.isOpen()){
            displayAlert("Not connected with server", "Please connect to server or try again later");
        }
        try {
            client.write("register#"+username+"#"+password);
        } catch (IOException e) {
            System.out.println("Failed writing data to server");
            return;
        }
        try {
            String response=(String) client.read();
            if(response.equalsIgnoreCase("duplicate")) displayAlert("Name already exists", "Please try a different name");
            else if(response.equalsIgnoreCase("registered")) displayAlert("Successful", "You have been added as a manufacturer");
        } catch (Exception e) {
            System.out.println("Server down: Failed reading data from server");
        }
    }

    private void displayAlert(String header, String content){
        Alert a=new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }

    @FXML
    private void handleLogin() {
        String userName = nameField.getText().strip();
        String password = passwordField.isDisabled() ? "" : passwordField.getText();
        int access = userName.equalsIgnoreCase("viewer") ? 0 : 1;
        UserData userData = new UserData(userName, password, access);
        try {
            client.write(userData);
            String response = (String) client.read();

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Login successful");
            if (response.equalsIgnoreCase("denied")) {
                a.setHeaderText("Login unsuccessful");
                a.setContentText("Username or password does not match");
                a.showAndWait();
            } else if (response.equalsIgnoreCase("viewer")) {
                a.setContentText("Logged in as viewer");
                a.showAndWait();
                main.showViewerPage();
            } else if (response.equalsIgnoreCase("manufacturer")) {
                a.setContentText("Logged in as manufacturer");
                a.showAndWait();
                main.showManufacturerPage();
            }

        } catch (Exception e) {
            System.out.println("Could not login: Server connection failed");
        }

    }

    @FXML
    private void handleReconnect() {
        if(client==null || !client.isOpen()) connectToServer();
    }
}
