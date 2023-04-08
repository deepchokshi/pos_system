package com.chokshi.deep.pos_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    FxmlLoaderUtil FXMLLoaderUtil = FxmlLoaderUtil.getInstance();

    @FXML
    Button historyDashboardButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        if(StaticDataUtil.getCurrentUser().getUserRole().equalsIgnoreCase("admin"))
            historyDashboardButton.setVisible(true);
        else
            historyDashboardButton.setVisible(false);
    }

    @FXML
    protected void onSalesButtonClick(ActionEvent event) throws IOException {
        FXMLLoaderUtil.loadFXML(event, "sales-view.fxml");
    }

    @FXML
    protected void onReportButtonClick(ActionEvent event) throws IOException {
        FXMLLoaderUtil.loadFXML(event, "report-view.fxml");
    }

    @FXML
    protected void onHistoryButtonClick(ActionEvent event) throws IOException {
        FXMLLoaderUtil.loadFXML(event, "history-view.fxml");
    }

    @FXML
    protected void onAdminButtonClick(ActionEvent event) throws IOException {
        FXMLLoaderUtil.loadFXML(event, "admin-view.fxml");
    }

    @FXML
    protected void onLogoutButtonClick(ActionEvent event) throws IOException {
        FXMLLoaderUtil.loadFXML(event, "login-view.fxml");
    }
}
