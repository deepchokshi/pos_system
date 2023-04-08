package com.chokshi.deep.pos_system;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginController {

    @FXML
    PasswordField pinInput;

    @FXML
    Label errorLabel;

    FxmlLoaderUtil fxmlLoaderUtil = FxmlLoaderUtil.getInstance();

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        int password = Integer.parseInt(pinInput.getText());

        try {
            Connection connection = MySQLConnection.getConnection();
            ResultSet userResultSet = connection.createStatement().executeQuery("SELECT * FROM user_info where user_pin=" + password);

            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            ResultSet machineResultSet = connection.createStatement().executeQuery("SELECT * FROM machine where machine_ip='" + ipAddress+"'");
            if (userResultSet.next()) {
                if (machineResultSet.next()){
                    Machine machine = new Machine(machineResultSet.getInt(1), machineResultSet.getString(2), machineResultSet.getString(3) );
                    StaticDataUtil.setCurrentMachine(machine);
                    User user = new User(userResultSet.getInt(1), userResultSet.getInt(2), userResultSet.getString(3));
                    StaticDataUtil.setCurrentUser(user);
                    fxmlLoaderUtil.loadFXML(event, "dashboard-view.fxml");
                }
                else{
                    errorLabel.setText("Invalid machine setup");
                }
            } else {
                onClearButtonClick();
                errorLabel.setText("Invalid pin");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    protected void onInputButtonClick(ActionEvent event) {
        if (errorLabel.getText().isBlank())
            errorLabel.setText(" ");

        fxmlLoaderUtil.setValueToTextField(pinInput, event);
    }

    @FXML
    protected void onClearButtonClick() {
        if (pinInput.getLength() > 0)
            pinInput.deleteText(0, pinInput.getLength());
    }

    @FXML
    protected void onShutdownButtonClick() {
        Platform.exit();
    }
}

