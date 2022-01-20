package engine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Warehouse implements Serializable{
    private static final String INP = "src/engine/in.txt";
    private static Scanner scn;
    private List<Car> cars;
    private static Warehouse warehouse=new Warehouse();

    public static Warehouse getInstance() {
        return warehouse;
    }

    public List<Car> getCars() {
        return cars;
    }

    private static void printMenuChoice() {
        System.out.println("(1) Search Cars");
        System.out.println("(2) Add Car");
        System.out.println("(3) Delete Car");
        System.out.println("(4) Exit System");
    }

    private static void printSearchChoice() {
        System.out.println("(1) By Registration Number");
        System.out.println("(2) By Car Make and Car Model");
        System.out.println("(3) Back to Main Menu");
    }

    public void loadCars() {
        cars=new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(INP));
            String line;
            String[] details;
            while (true) {
                line = br.readLine();
                if (line == null) break;
                details = line.split(",");
                cars.add(new Car(details));
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Failed to load data file!");
        }
    }

    public void updateWarehouse() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(INP));
            for (Car car : cars) {
                bw.write(car.toString()+"\n");
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to update data file!");
        }
    }

    public void delCar(String reg) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getRegNum().equalsIgnoreCase(reg)) {
                cars.remove(i);
                return;
            }
        }
    }

    public void addCar(String data) {
        String[] parsed=data.split(",");
        Car existing=byRegNum(parsed[0]);
        if (existing == null) {
            cars.add(new Car(parsed));
        } else {
            existing.setQuantity(existing.getQuantity() + Integer.parseInt(parsed[8]));
        }
    }

    public Car byMakeMod(String make, String model) {
        for (Car car : cars) {
            if ((car.getMake().equalsIgnoreCase(make) && (model.equalsIgnoreCase("any") || model.equalsIgnoreCase(car.getModel())))) {
                return car;
            }
        }
        return null;
    }

    public Car byRegNum(String reg) {
        for (Car car : cars) {
            if (car.getRegNum().equalsIgnoreCase(reg)) {
                return car;
            }
        }
        return null;
    }

    private static int getSearchChoice() {
        int choice;
        do {
            while (!scn.hasNextInt()) {
                System.out.println("Error: Please enter an integer");
                scn.next();
                printSearchChoice();
            }
            choice = scn.nextInt();
            if (choice < 1 || choice > 3) {
                System.out.println("Error: Search choice must be within 1-4");
                printSearchChoice();
            }
        } while (choice < 1 || choice > 3);
        scn.nextLine();
        return choice;
    }

    private int getMenuChoice() {
        int choice;
        do {
            while (!scn.hasNextInt()) {
                System.out.println("Error: Please enter an integer");
                scn.next();
                printMenuChoice();
            }
            choice = scn.nextInt();
            if (choice < 1 || choice > 4) {
                System.out.println("Error: Menu choice must be within 1-4");
                printMenuChoice();
            }
        } while (choice < 1 || choice > 4);
        scn.nextLine();
        return choice;
    }

    public Car buyCar(String reg) {
        Car car=byRegNum(reg);
        if(car==null || car.getQuantity()==0) return null;
        car.setQuantity(car.getQuantity()-1);
        return car;
    }
}
