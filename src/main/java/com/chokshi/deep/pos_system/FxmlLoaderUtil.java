package com.chokshi.deep.pos_system;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class FxmlLoaderUtil {

    private static FxmlLoaderUtil fxmlLoaderUtil;

    private FxmlLoaderUtil() {
    }

    public static FxmlLoaderUtil getInstance() {
        if (fxmlLoaderUtil == null)
            fxmlLoaderUtil = new FxmlLoaderUtil();
        return fxmlLoaderUtil;
    }

    public void loadFXML(Event event, String fxmlFileName) throws IOException {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent pane = javafx.fxml.FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
        primaryStage.getScene().setRoot(pane);
    }

    public void setValueToTextField(TextInputControl inputControl, ActionEvent event) {
        inputControl.appendText(((Button) event.getTarget()).getText());
    }
}
