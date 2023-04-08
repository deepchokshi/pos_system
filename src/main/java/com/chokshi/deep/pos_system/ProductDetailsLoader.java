package com.chokshi.deep.pos_system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsLoader {

    static Map<String, ProductDetails> productDetailsMap = new HashMap<>();
    static Map<Integer, Machine> machineDetailsMap = new HashMap<>();

    private static Map<String, ProductDetails> loadProductDetails() throws SQLException {
        Connection connection = MySQLConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from product_details");

        while(resultSet.next()){
            productDetailsMap.put(resultSet.getString(2), new ProductDetails(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDouble(4), resultSet.getDouble(5) ));
        }

        return productDetailsMap;
    }

    public static Map<String, ProductDetails> getProductDetails() throws SQLException {
        if(productDetailsMap.isEmpty())
            productDetailsMap = loadProductDetails();

        return productDetailsMap;
    }

    private static Map<Integer, Machine> loadMachineDetails() throws SQLException {
        Connection connection = MySQLConnection.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from machine");

        while(resultSet.next()){
            machineDetailsMap.put(resultSet.getInt(1), new Machine(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3) ));
        }

        return machineDetailsMap;
    }

    public static Map<Integer, Machine> getMachineDetails() throws SQLException {
        if(machineDetailsMap.isEmpty())
            machineDetailsMap = loadMachineDetails();

        return machineDetailsMap;
    }
}
