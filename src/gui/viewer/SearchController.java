package gui.viewer;

import gui.util.CarBinder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SearchController {
    @FXML
    private RadioButton byReg;
    @FXML
    private TextField regBox;
    @FXML
    private TextField makeBox;
    @FXML
    private TextField modBox;
    @FXML
    private AnchorPane searchPane;
    private TableView<CarBinder> tableView;

    @FXML
    public void initialize() {
        byReg.setSelected(true);
        activateBox();
    }

    public void setTableView(TableView<CarBinder> tableView){
        this.tableView=tableView;
    }

    @FXML
    private void activateBox() {
        boolean disableOther = byReg.isSelected();
        regBox.setDisable(!disableOther);
        makeBox.setDisable(disableOther);
        modBox.setDisable(disableOther);
    }

    @FXML
    private void processSearchResult(ActionEvent actionEvent) {
        CarBinder result = null;
        if (byReg.isSelected()) {
            String reg=regBox.getText();
            if (reg.isBlank()) {
                showAlert("No value given", "Please enter a value");
                return;
            }
            result=searchByReg(reg);
        }else{
            String make=makeBox.getText().trim();
            String mod=modBox.getText().trim();
            if(make.isBlank() || mod.isBlank()){
                showAlert("No value given", "Please enter a value");
                return;
            }
            result=searchByMod(make, mod);
        }
        if(result==null) showAlert("No such car", "Sorry, your search result did not find anything");
        else {
            Stage stage=(Stage)searchPane.getScene().getWindow();
            stage.close();
        }
    }

    private void showAlert(String header, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }

    private CarBinder searchByMod(String make, String mod) {
        for (CarBinder car : tableView.getItems()) {
            if(car.getMakeCol().equalsIgnoreCase(make) && car.getModCol().equalsIgnoreCase(mod)){
                tableView.getSelectionModel().select(car);
                return car;
            }
        }
        return null;
    }

    private CarBinder searchByReg(String reg) {
        for (CarBinder car : tableView.getItems()) {
            if(car.getRegCol().equalsIgnoreCase(reg)){
                tableView.getSelectionModel().select(car);
                return car;
            }
        }
        return null;
    }
}
