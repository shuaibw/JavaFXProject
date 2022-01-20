package engine;

import java.io.Serializable;

public class Car implements Serializable {
    private final String regNum;
    private final int year;
    private final String[] colors;
    private final String color;
    private final String make;
    private final String model;
    private final int price;
    private int quantity;

    public Car(String[] details) {
        colors = new String[3];
        this.regNum = details[0];
        this.year = Integer.parseInt(details[1]);
        this.colors[0] = details[2];
        this.colors[1] = details[3];
        this.colors[2] = details[4];
        this.make = details[5];
        this.model = details[6];
        this.price = Integer.parseInt(details[7]);
        this.quantity=Integer.parseInt(details[8]);
        this.color=colorParser();
    }

    public String getRegNum() {
        return regNum;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

    public String getYear() {
        return String.valueOf(year);
    }

    public String getColor() {
        return color;
    }

    public String getPrice() {
        return String.valueOf(price);
    }

    public void printDetails() {
        System.out.printf("|Make: %-10s | Model: %-10s | MFG: %-5d | Registration: %-7s | Price: $%-6d | Colors: %s | Quantity: %s\n", make, model, year, regNum, price, color, quantity);
    }

    private String colorParser() {
        StringBuilder uniColor = new StringBuilder();
        for (String color : colors) {
            if (!color.isEmpty()) uniColor.append(color).append(", ");
        }
        uniColor.setLength(uniColor.length() - 2);
        return uniColor.toString();
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", regNum, year, colors[0], colors[1], colors[2], make, model, price, quantity);
    }

}
