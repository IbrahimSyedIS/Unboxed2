package com.papayaman.unboxed;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {

    private String host;
    private int port;

    private Socket socket;

    private ObjectInputStream ois;

    private ArrayList<Double[]> markers;

    public void run() {
        try {
            socket = new Socket(host, port);
            ois = new ObjectInputStream(socket.getInputStream());
            getData();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Client/run", "Could not Connect");
        }
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        start();
    }

    private void getData() throws ClassNotFoundException, IOException {
        markers = (ArrayList<Double[]>)ois.readObject();
    }

    public ArrayList<Double[]> getMarkers() {
        return markers;
    }

    public static void sendToServer(double d, double m, double y, double lat, double lng) {

    }

    public void close() {
        try {
            ois.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
