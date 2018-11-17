package com.papayaman.unboxed;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {

    private String host;
    private int port;

    private ArrayList<Sale> sales;
    private Sale sale;
    private final Object submitLock = new Object();
    private final Object markerLock = new Object();

    public void run() {
        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(new Message(Message.Type.GET_SALES));
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            Message message = (Message) ois.readObject();
            if (message.getType().equals(Message.Type.SEND_SALES))
                sales = message.getGarageSales();
            synchronized (markerLock) {
                markerLock.notify();
            }
            synchronized (submitLock) {
                submitLock.wait();
            }
            Message toSend = new Message(Message.Type.ADD_SALE);
            toSend.addNewSale(sale);
            oos.writeObject(toSend);
            oos.flush();
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e("Client/run", "Could not connect to: " + host + ":" + port);
        } catch (InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    Client(String host, int port) {
        this.host = host;
        this.port = port;
        sales = new ArrayList<>();
        start();
    }

    ArrayList<Sale> getSales() {
        synchronized (markerLock) {
            try {
                markerLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sales;
    }

    void send(Sale sale) {
        this.sale = sale;
        synchronized (submitLock) {
            submitLock.notify();
        }
    }
}
