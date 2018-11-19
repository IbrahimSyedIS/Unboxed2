package com.papayaman.unboxed;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client extends Thread {

    private static String host = "papayaman.com";
    private static int port = 8765;
    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    private static volatile Message message;
    private static final Object waitLock = new Object();
    private static final Object markerLock = new Object();

    private static boolean running = false;
    private static Client thread;

    static void init() {
        if (running)
            throw new IllegalStateException("Client has already started!");
        running = true;
        thread = new Client();
    }

    static void disconnect() {
        if (!running) {
            throw new IllegalStateException("Can't stop if not running, that just doesnt make sense, like come on man.  You can't stop a thread if you havent started the thread, right?  Like why would you try to do that? idk man, it just doesnt make sense");
        }
        message = new Message(Message.Type.DISCONNECT);
        synchronized (waitLock) {
            waitLock.notify();
        }
    }

    public void run() {
        try {
            socket = new Socket(host, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Client/run", "Could not connect to: " + host + ":" + port);
        }

        while (true) {
            synchronized (waitLock) {
                try {
                    waitLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                oos.writeObject(message);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (message.getType().equals(Message.Type.GET_SALES)) {
                try {
                    message = (Message) ois.readUnshared();
                    synchronized (markerLock){
                        markerLock.notify();
                    }
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
            if (message.getType().equals(Message.Type.DISCONNECT))
                break;
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Client() {
        super("NetworkingThread");
        start();
    }

    static ArrayList<Sale> getSales() {
        if (!running)
            throw new IllegalStateException("Not initialized!");

        message = new Message(Message.Type.GET_SALES);
        synchronized (waitLock) {
            waitLock.notify();
        }
        synchronized (markerLock) {
            try {
                markerLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return message.getGarageSales();
    }

    static void sendNewSale(Sale sale) {
        if (!running) throw new IllegalStateException("Cannot send if not initialized!");

        message = new Message(Message.Type.ADD_SALE);
        message.addNewSale(sale);

        synchronized (waitLock) {
            waitLock.notify();
        }
    }
}
