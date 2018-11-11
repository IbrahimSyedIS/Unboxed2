package com.papayaman.unboxed;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private Socket socket;

    private ObjectInputStream ois;

    private ArrayList<Double[]> markers;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            ois = new ObjectInputStream(socket.getInputStream());
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getData() throws ClassNotFoundException, IOException {
        markers = (ArrayList<Double[]>)ois.readObject();
    }

    public ArrayList<Double[]> getMarkers() {
        return markers;
    }

    public static void sendToServer(double d, double m, double y, double lat, double lng) {

    }
}
