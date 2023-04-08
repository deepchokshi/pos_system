module com.chokshi.deep.pos_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.web;

    opens com.chokshi.deep.pos_system to javafx.fxml;
    exports com.chokshi.deep.pos_system;
}