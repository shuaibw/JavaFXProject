package gui.manufacturer;

import client.Client;
import engine.Car;
import gui.util.CarBinder;
import gui.login.LoginController;
import gui.Main;
import gui.viewer.ViewerController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ManufacturerController {
    private static Main main;
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
    private static Client client;
    private ObservableList<CarBinder> carList;
    private ArrayList<Car> cars;

    public static void setMain(Main main) {
        ManufacturerController.main = main;
    }

    public TableView<CarBinder> getTableView() {
        return tableView;
    }

    @FXML
    public void initialize(){
        client= LoginController.getClient();
        carList= FXCollections.observableArrayList();
        bindTable();
        tableUpdater();
    }

    private void tableUpdater() {
        Thread t = new Thread(() -> {
            try {
                while (client.isOpen()) {
                    Object fetched = client.read();
                    if (fetched.getClass() == ArrayList.class) {
                        populateTable((ArrayList<Car>) fetched);
                    } else if (fetched.getClass() == String.class) {
                        System.out.println((String) fetched);
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
        ViewerController.bindTable(regCol, yearCol, colorCol, makeCol, modCol, priceCol, quantityCol);
    }

    @FXML
    private void handleView() {
        try {
            client.write("view");
        } catch (IOException e) {
            System.out.println("Failed connecting to server");
        }
    }

    @FXML
    private void handleAdd() {
        Stage addWindow=new Stage();
        addWindow.initModality(Modality.APPLICATION_MODAL);
        addWindow.setTitle("Add New Car");
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("addCar.fxml"));
            addWindow.setScene(new Scene(loader.load(), 600, 400));
            AddCarController acc=loader.getController();
            acc.setClient(client);
            addWindow.initOwner(borderPane.getScene().getWindow());
            addWindow.showAndWait();
        } catch (Exception e) {
            System.out.println("Failed loading addCar.fxml");
        }
    }

    @FXML
    private void handleEdit() {
        Stage addWindow=new Stage();
        addWindow.initModality(Modality.APPLICATION_MODAL);
        addWindow.setTitle("Edit Car");
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("addCar.fxml"));
            addWindow.setScene(new Scene(loader.load(), 600, 400));
            AddCarController acc=loader.getController();
            acc.setClient(client);
            acc.setEdit(true);
            acc.setTableView(tableView);
            acc.loadDataToFields();
            addWindow.initOwner(borderPane.getScene().getWindow());
            addWindow.showAndWait();
        } catch (Exception e) {
            System.out.println("Failed loading addCar.fxml");
        }
    }

    @FXML
    private void handleDelete() {
        CarBinder cb=tableView.getSelectionModel().getSelectedItem();
        if(cb==null){
            Alert a=new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("No car selected");
            a.setContentText("Please select a car first");
            a.showAndWait();
            return;
        }
        try {
            client.write("deleteAndRefresh#"+cb.getRegCol());
        } catch (IOException e) {
            System.out.println("Failed deleting car: Could not connect to server");
        }
    }
}
