package server;

import engine.Warehouse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server extends Thread{
    private final int port;
    private final List<NetworkUtil> networkUtils;
    private final List<UserData> savedData;
    public Server(int port){
        this.port=port;
        networkUtils=new ArrayList<>();
        savedData=new ArrayList<>();
        savedData.add(new UserData("shuaib", "1234", 1));
        savedData.add(new UserData("viewer", "", 0));
        savedData.add(new UserData("knuth", "8838", 1));
        savedData.add(new UserData("turing", "9982", 1));
        saveToDisk();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Warehouse.getInstance().loadCars();
            while(true) {
                Socket clientSocket= serverSocket.accept();
                NetworkUtil networkUtil = new NetworkUtil(clientSocket);
                networkUtils.add(networkUtil);
                new ServerWorker(this, networkUtil);
            }
        } catch (IOException e) {
            System.out.println("A client has disconnected");
        }finally {
            System.out.println("Add code to stop method in javaFX");
        }

    }

    public List<NetworkUtil> getNetworkUtils() {
        return networkUtils;
    }

    public List<UserData> getSavedData() {
        return savedData;
    }

    public void saveToDisk(){
        new Thread(()->{
            Scanner scanner=new Scanner(System.in);
            while(true) {
                String cmd = scanner.nextLine();
                if (cmd.equalsIgnoreCase("save")) {
                    Warehouse.getInstance().updateWarehouse();
                    System.out.println("Changes saved to disk");
                    break;
                }else{
                    System.out.println("Command not recognized");
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        new Server(2222).start();
    }
}
