package client;

import server.NetworkUtil;

import java.io.IOException;

public class Client {
    private NetworkUtil networkUtil;

    public Client(String serverName, int port) throws Exception{
        networkUtil=new NetworkUtil(serverName, port);
    }

    public void write(Object o) throws IOException {
        networkUtil.write(o);
    }

    public Object read() throws IOException, ClassNotFoundException {
        return networkUtil.read();
    }

    public boolean isOpen(){
        return networkUtil.isOpen();
    }

    public void kill() throws IOException {
        if(networkUtil.isOpen()) networkUtil.closeConnection();
    }

    public static void main(String[] args) throws Exception{
        new Client("localhost", 2222);
    }
}
