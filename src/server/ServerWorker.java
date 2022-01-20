package server;

import engine.Car;
import engine.Warehouse;

import java.io.IOException;
import java.util.List;

public class ServerWorker implements Runnable{
    private NetworkUtil networkUtil;
    private Thread t;
    private Server server;
    private UserData userData=null;
    private List<UserData> savedData;
    public ServerWorker(Server server, NetworkUtil networkUtil) {
        this.networkUtil=networkUtil;
        this.server=server;
        this.savedData= server.getSavedData();
        t=new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            while (networkUtil.isOpen()) {
                Object fetched = networkUtil.read();
                if(fetched.getClass()==UserData.class){
                    userData=(UserData) fetched;
                    int access=authenticate(userData);
                    if(access==-1){
                        networkUtil.write("denied");
                    }else{
                        System.out.println("Logged in: "+userData);
                        networkUtil.write(access==0?"viewer":"manufacturer");
                    }
                }else if(fetched.getClass()==String.class){
                    assert userData!=null;
                    String line=(String)fetched;
                    System.out.println(line);// debug purpose
                    String[] tokens=line.split("#");
                    String cmd=tokens[0];
                    if(cmd.equalsIgnoreCase("view")){
                        handleCarList();
                    }else if(cmd.equalsIgnoreCase("delete")){
                        assert tokens.length==2;
                        String reg=tokens[1];
                        Warehouse.getInstance().delCar(reg);
                    }else if(cmd.equalsIgnoreCase("deleteAndRefresh")){
                        assert tokens.length==2;
                        String reg=tokens[1];
                        Warehouse.getInstance().delCar(reg);
                        refresh();
                    }else if(cmd.equalsIgnoreCase("buy")){
                        assert tokens.length==2;
                        String reg=tokens[1];
                        handleBuy(reg);
                    }else if(cmd.equalsIgnoreCase("add")){
                        assert tokens.length==2;
                        handleCarAdd(tokens[1]);
                    }else if(cmd.equalsIgnoreCase("register")){
                        assert tokens.length==3;
                        String username=tokens[1];
                        String password=tokens[2];
                        UserData userData=new UserData(username, password, 1);
                        handleRegistration(userData);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Logged off: "+userData);
            handleQuit();
        }
    }

    private void handleRegistration(UserData userData) {
        for (UserData data : savedData) {
            if(userData.getName().equalsIgnoreCase(data.getName())){
                try {
                    networkUtil.write("duplicate");
                } catch (IOException e) {
                    System.out.println("Failed writing data to client");
                }
                return;
            }
        }
        savedData.add(userData);
        try {
            networkUtil.write("registered");
        } catch (IOException e) {
            System.out.println("Failed to notify client, but registered");
        }
    }


    private void handleCarAdd(String data) throws IOException {
        Warehouse.getInstance().addCar(data);
        networkUtil.write("Added car to list");
        refresh();
    }

    private void handleBuy(String reg) throws IOException {
        Car car=Warehouse.getInstance().buyCar(reg);
        networkUtil.write(car==null?"NA":"Bought car: "+car.getMake()+", "+car.getModel());
        if(car!=null) refresh();
    }

    private void handleQuit() {
        try {
            networkUtil.closeConnection();
            server.getNetworkUtils().remove(networkUtil);
        } catch (IOException e) {
            System.out.println("Failed to close socket: "+userData);
        }
    }

    private void handleCarList() throws IOException {
        networkUtil.write(Warehouse.getInstance().getCars());
    }

    private void refresh() throws IOException {
        for (NetworkUtil client : server.getNetworkUtils()) {
            client.write(Warehouse.getInstance().getCars());
        }
    }

    private int authenticate(UserData userData) {
        //-1 -->deny, 0-->viewer, 1-->manufacturer
        if(savedData.contains(userData)) return userData.getAccess();
        return -1;
    }
}
