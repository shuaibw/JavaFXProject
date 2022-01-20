package gui.manufacturer;

import client.Client;
import gui.util.CarBinder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AddCarController {
    @FXML
    private TextField makerBox;
    @FXML
    private TextField modBox;
    @FXML
    private TextField clr1Box;
    @FXML
    private TextField clr2Box;
    @FXML
    private TextField clr3Box;
    @FXML
    private TextField regBox;
    @FXML
    private TextField yearBox;
    @FXML
    private TextField priceBox;
    @FXML
    private TextField quantityBox;
    @FXML
    private AnchorPane anchorPane;
    private TableView<CarBinder> tableView;
    private Client client;
    private boolean edit;
    private CarBinder cb;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setEdit(boolean value){
        edit=value;
    }

    public void setTableView(TableView<CarBinder> tableView) {
        this.tableView = tableView;
    }

    public void loadDataToFields(){
        cb=tableView.getSelectionModel().getSelectedItem();
        regBox.setText(cb.getRegCol());
        yearBox.setText(cb.getYearCol());
        String[] colors=cb.getColorCol().split(",");
        int len=colors.length;
        clr1Box.setText(0<len?colors[0]:"");
        clr2Box.setText(1<len?colors[1]:"");
        clr3Box.setText(2<len?colors[2]:"");
        makerBox.setText(cb.getMakeCol());
        modBox.setText(cb.getModCol());
        priceBox.setText(cb.getPriceCol());
        quantityBox.setText(cb.getQuantityCol());
    }

    @FXML
    private void handleSave() {
        String carData=extractFromFields();
        if(carData==null) return;
        if(edit){
            try {
                client.write("delete#"+cb.getRegCol());
            } catch (IOException e) {
                System.out.println("Failed editing data: Could not notify server");
            }
        }
        try {
            client.write("add#"+carData);
            showAlert("Success", edit?"Car edited":"Car added to list");
            Stage stage=(Stage)anchorPane.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            if(!edit) System.out.println("Failed adding car: Could not connect to server");
        }
    }
    private String extractFromFields(){
        String[] data=new String[9];
        data[0]=regBox.getText().trim();
        data[1]=yearBox.getText().trim();
        data[2]=clr1Box.getText().trim();
        data[3]=clr2Box.getText().trim();
        data[4]=clr3Box.getText().trim();
        data[5]=makerBox.getText().trim();
        data[6]=modBox.getText().trim();
        data[7]=priceBox.getText().trim();
        data[8]=quantityBox.getText().trim();
        for(int i=0;i<9;i++){
            if(i==2 || i==3 || i==4) continue;
            if(data[i].isEmpty()){
                showAlert("Empty fields not allowed", "One or more fields are empty");
                return null;
            }
        }
        return String.join(",", data);
    }
    @FXML
    private void handleCancel() {
        Stage stage=(Stage)anchorPane.getScene().getWindow();
        stage.close();
    }
    private void showAlert(String header, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }
}
