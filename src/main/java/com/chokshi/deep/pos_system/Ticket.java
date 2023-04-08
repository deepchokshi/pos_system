package com.chokshi.deep.pos_system;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Ticket {

    int ticketNumber;
    int quantity = 0;
    String product;
    int price = 0;
    int machineId;
    BooleanProperty selected = new SimpleBooleanProperty(false);

    public Ticket() {
    }

    public Ticket(int quantity, String product, int price) {
        this.quantity = quantity;
        this.product = product;
        this.price = price;
        this.selected = new SimpleBooleanProperty(false);
    }

    public Ticket(int ticketNumber, int quantity, String product, int price) {
        this.ticketNumber = ticketNumber;
        this.quantity = quantity;
        this.product = product;
        this.price = price;
        this.selected = new SimpleBooleanProperty(false);

    }

    public Ticket(int ticketNumber, String product, int price, int machineId) {
        this.ticketNumber = ticketNumber;
        this.product = product;
        this.price = price;
        this.machineId = machineId;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public void appendPrice(int price){
        this.price += price;
    }

    public void appendQuantity(int quantity){
        this.quantity += quantity;
    }
}
