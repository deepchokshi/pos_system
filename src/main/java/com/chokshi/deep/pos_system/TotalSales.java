package com.chokshi.deep.pos_system;

import java.util.Map;

public class TotalSales {

    Map<Integer, MachineTotalSales> machineWiseTotalSales;
    Ticket grandTotalSales;
    int startTicketNumber;
    int endTicketNumber;

    public Map<Integer, MachineTotalSales> getMachineWiseTotalSales() {
        return machineWiseTotalSales;
    }

    public void setMachineWiseTotalSales(Map<Integer,MachineTotalSales> machineWiseTotalSales) {
        this.machineWiseTotalSales = machineWiseTotalSales;
    }

    public Ticket getGrandTotalSales() {
        return grandTotalSales;
    }

    public void setGrandTotalSales(Ticket grandTotalSales) {
        this.grandTotalSales = grandTotalSales;
    }

    public int getStartTicketNumber() {
        return startTicketNumber;
    }

    public void setStartTicketNumber(int startTicketNumber) {
        this.startTicketNumber = startTicketNumber;
    }

    public int getEndTicketNumber() {
        return endTicketNumber;
    }

    public void setEndTicketNumber(int endTicketNumber) {
        this.endTicketNumber = endTicketNumber;
    }
}
