package com.papayaman.unboxed;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

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
            getData();
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

    private void getData() throws ClassNotFoundException, IOException {
        markers = (ArrayList<Double[]>)ois.readObject();
    }

    public ArrayList<Double[]> getMarkers() {
        return markers;
    }

    private void sendToServer(Double[] marker) {
        try {
            System.out.println("SENDING MARKER NOW MIKE");
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
        this.marker[0] = marker[0];
        this.marker[1] = marker[1];
        this.marker[2] = marker[2];
        this.marker[3] = marker[3];
        this.marker[4] = marker[4];
        System.out.println(Arrays.toString(marker));
        this.lock = true;
    }
}
