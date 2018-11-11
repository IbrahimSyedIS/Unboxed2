package com.papayaman.unboxed;

import android.util.Log;

import java.io.DataOutputStream;
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
    private Double[] marker = new Double[5];
    private boolean lock = false;

    public void run() {
        try {
            socket = new Socket(host, port);
            ois = new ObjectInputStream(socket.getInputStream());
            markers = (ArrayList<Double[]>) ois.readObject();
            while (!lock) {}
            System.out.println("Working now");
            sendToServer(marker);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Client/run", "Could not Connect");
        }
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        markers = new ArrayList<>();
        start();
    }

    public ArrayList<Double[]> getMarkers() {
        return markers;
    }

    private void sendToServer(Double[] marker) {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeDouble(marker[0]);
            dos.writeDouble(marker[1]);
            dos.writeDouble(marker[2]);
            dos.writeDouble(marker[3]);
            dos.writeDouble(marker[4]);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            ois.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Double[] marker) {
        this.marker = marker;
        this.lock = true;
    }
}
