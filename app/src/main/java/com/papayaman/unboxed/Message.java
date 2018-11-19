package com.papayaman.unboxed;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message objects are what are sent back and forth between server and client
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 10002L;

    public enum Type {
        ADD_SALE,
        GET_SALES,
        SENDING_SALES,
        DISCONNECT
    }

    private final Type type;
    private ArrayList<Sale> garageSales = new ArrayList<>();
    private Sale saleToAdd;

    public Message(Type type) {
        this.type = type;
    }

    public ArrayList<Sale> getGarageSales() {
        return garageSales;
    }

    public Type getType() {
        return type;
    }

    public void addSales(ArrayList<Sale> garageSales) {
        this.garageSales = garageSales;
    }

    public void addNewSale(Sale saleToAdd) {
        this.saleToAdd = saleToAdd;
    }

    public Sale getSaleToAdd() {
        return saleToAdd;
    }
}
