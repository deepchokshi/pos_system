package com.chokshi.deep.pos_system;

import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DataLoadUtil {

    public static ResultSet getTicketsByDateRange(DatePicker startDatePicker, DatePicker endDatePicker, String query) throws SQLException {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalTime startTime = LocalTime.of(0, 0, 0);
            LocalTime endTime = LocalTime.of(23, 59, 59);
            LocalDateTime startDate = LocalDateTime.of(startDatePicker.getValue(), startTime);
            LocalDateTime endDate = LocalDateTime.of(endDatePicker.getValue(), endTime);
            String start = startDate.format(formatter);
            String end = endDate.format(formatter);

            Connection connection = MySQLConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, start);
            preparedStatement.setString(2, end);

        return preparedStatement.executeQuery();

    }

    public static boolean validateDatePicker(DatePicker startDatePicker, DatePicker endDatePicker,Label errorLabel){
        boolean isDateValid = false;
        if (null == startDatePicker.getValue()) {
            errorLabel.setText("Select Start date");
        } else if (null == endDatePicker.getValue()) {
            errorLabel.setText("Select End date");
        } else if(startDatePicker.getValue().isAfter(endDatePicker.getValue()))
            errorLabel.setText("Start Date cannot be greater than end date");
            // TODO: check if start date and end date is not greater than current date
        else
            isDateValid = true;

        return isDateValid;
    }
}
