package com.chokshi.deep.pos_system;

import java.util.Map;

public class MachineTotalSales {

    String machineName;
    Map<String, Ticket> productWiseTotalSales;
    Ticket machineTotalSale;

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Map<String, Ticket> getProductWiseTotalSales() {
        return productWiseTotalSales;
    }

    public void setProductWiseTotalSales(Map<String, Ticket> productWiseTotalSales) {
        this.productWiseTotalSales = productWiseTotalSales;
    }

    public Ticket getMachineTotalSale() {
        return machineTotalSale;
    }

    public void setMachineTotalSale(Ticket machineTotalSale) {
        this.machineTotalSale = machineTotalSale;
    }
}
