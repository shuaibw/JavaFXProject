package gui.util;

import engine.Car;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class CarBinder {
    private final SimpleStringProperty regCol;
    private final SimpleStringProperty yearCol;
    private final SimpleStringProperty colorCol;
    private final SimpleStringProperty makeCol;
    private final SimpleStringProperty modCol;
    private final SimpleStringProperty priceCol;
    private final SimpleStringProperty quantityCol;
    public CarBinder(Car car){
        regCol=new SimpleStringProperty(car.getRegNum());
        yearCol=new SimpleStringProperty(car.getYear());
        colorCol=new SimpleStringProperty(car.getColor());
        makeCol=new SimpleStringProperty(car.getMake());
        modCol=new SimpleStringProperty(car.getModel());
        priceCol=new SimpleStringProperty(car.getPrice());
        quantityCol=new SimpleStringProperty(String.valueOf(car.getQuantity()));
    }

    public String getRegCol() {
        return regCol.get();
    }

    public SimpleStringProperty regColProperty() {
        return regCol;
    }

    public String getYearCol() {
        return yearCol.get();
    }

    public SimpleStringProperty yearColProperty() {
        return yearCol;
    }

    public String getColorCol() {
        return colorCol.get();
    }

    public SimpleStringProperty colorColProperty() {
        return colorCol;
    }

    public String getMakeCol() {
        return makeCol.get();
    }

    public SimpleStringProperty makeColProperty() {
        return makeCol;
    }

    public String getModCol() {
        return modCol.get();
    }

    public SimpleStringProperty modColProperty() {
        return modCol;
    }

    public String getPriceCol() {
        return priceCol.get();
    }

    public SimpleStringProperty priceColProperty() {
        return priceCol;
    }

    public String getQuantityCol() {
        return quantityCol.get();
    }

    public SimpleStringProperty quantityColProperty() {
        return quantityCol;
    }
}
