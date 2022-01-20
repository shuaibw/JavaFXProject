package gui.viewer;

import client.Client;
import engine.Car;
import gui.util.CarBinder;
import gui.login.LoginController;
import gui.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ViewerController {
    private static Main main;
    private ArrayList<Car> cars;
    private static Client client;
    @FXML
    private TableColumn<CarBinder, String> regCol;
    @FXML
    private TableColumn<CarBinder, String> yearCol;
    @FXML
    private TableColumn<CarBinder, String> colorCol;
    @FXML
    private TableColumn<CarBinder, String> makeCol;
    @FXML
    private TableColumn<CarBinder, String> modCol;
    @FXML
    private TableColumn<CarBinder, String> priceCol;
    @FXML
    private TableColumn<CarBinder, String> quantityCol;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<CarBinder> tableView;
    private ObservableList<CarBinder> carList;

    @FXML
    public void initialize() {
        client = LoginController.getClient();
        carList = FXCollections.observableArrayList();
        bindTable();
        tableUpdater();
    }

    public TableView<CarBinder> getTableView() {
        return tableView;
    }

    public static void setMain(Main main) {
        ViewerController.main = main;
    }

    @FXML
    private void handleView(ActionEvent actionEvent) {
        try {
            client.write("view");
        } catch (IOException e) {
            System.out.println("Failed connecting to server");
        }
    }

    private void tableUpdater() {
        Thread t = new Thread(() -> {
            try {
                while (client.isOpen()) {
                    Object fetched = client.read();
                    if (fetched.getClass() == ArrayList.class) {
                        populateTable((ArrayList<Car>) fetched);
                    } else if (fetched.getClass() == String.class) {
                        String res=(String) fetched;
                        if(res.equalsIgnoreCase("NA")){
                            Platform.runLater(()->{
                                Alert a=new Alert(Alert.AlertType.INFORMATION);
                                a.setHeaderText("Not available");
                                a.setContentText("Poor bastard. You were too slow");
                                a.showAndWait();
                            });
                        }
                        System.out.println(res);
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to read server data");
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void populateTable(ArrayList<Car> fetched) {
        cars = fetched;
        carList.clear();
        for (Car car : cars) {
            carList.add(new CarBinder(car));
        }
        Platform.runLater(() -> tableView.setItems(carList));
    }

    private void bindTable() {
        bindTable(regCol, yearCol, colorCol, makeCol, modCol, priceCol, quantityCol);
    }

    public static void bindTable(TableColumn<CarBinder, String> regCol, TableColumn<CarBinder, String> yearCol, TableColumn<CarBinder, String> colorCol, TableColumn<CarBinder, String> makeCol, TableColumn<CarBinder, String> modCol, TableColumn<CarBinder, String> priceCol, TableColumn<CarBinder, String> quantityCol) {
        regCol.setCellValueFactory(new PropertyValueFactory<>("regCol"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("yearCol"));
        colorCol.setCellValueFactory(new PropertyValueFactory<>("colorCol"));
        makeCol.setCellValueFactory(new PropertyValueFactory<>("makeCol"));
        modCol.setCellValueFactory(new PropertyValueFactory<>("modCol"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("priceCol"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityCol"));
    }

    @FXML
    private void buyCar() {
        CarBinder selectedCar = tableView.getSelectionModel().getSelectedItem();
        if(selectedCar==null){
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("No car selected");
            a.setContentText("Please select a car first");
            a.showAndWait();
            return;
        }
        String reg = selectedCar.getRegCol();
        try {
            client.write("buy#" + reg);
        } catch (IOException e) {
            System.out.println("Failed writing data to server");
        }
    }

    @FXML
    private void showSearchWindow() {
        if(tableView.getItems().size()==0){
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("No car in tableview");
            a.setContentText("Please load some cars");
            a.showAndWait();
            return;
        }
        Stage searchWindow=new Stage();
        searchWindow.initModality(Modality.APPLICATION_MODAL);
        searchWindow.setTitle("Search");
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("searchWindow.fxml"));
            searchWindow.setScene(new Scene(loader.load(), 600, 400));
            SearchController sc=loader.getController();
            sc.setTableView(tableView);
            searchWindow.initOwner(borderPane.getScene().getWindow());
            searchWindow.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        try {
            client.kill();
        } catch (IOException e) {
            System.out.println("Server already down");
        }finally {
            try {
                main.showLoginPage();
            } catch (Exception e) {
                System.out.println("Error loading login page");
            }
        }
    }
}
