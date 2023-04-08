package com.chokshi.deep.pos_system;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.control.DatePicker;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.sql.*;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryController {

    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;
    @FXML
    Label errorLabel;

    @FXML
    TableView historyTable;

    @FXML
    TableColumn checkBoxColumn;

    FxmlLoaderUtil fxmlLoaderUtil = FxmlLoaderUtil.getInstance();

    @FXML
    protected void onMainMenuButtonClick(ActionEvent event ) throws IOException {
        fxmlLoaderUtil.loadFXML(event, "dashboard-view.fxml");
    }

    @FXML
    protected void onSearchButtonClick() throws SQLException {
        historyTable.getItems().clear();
        historyTable.refresh();
        if(DataLoadUtil.validateDatePicker(startDatePicker, endDatePicker, errorLabel)) {
            ResultSet resultSet = DataLoadUtil.getTicketsByDateRange(startDatePicker, endDatePicker, "SELECT * FROM ticket WHERE date BETWEEN ? AND ?");
            if (resultSet != null) {
                errorLabel.setText(" ");
                List<Ticket> data = new ArrayList<>();
                LocalDate selectedDate = startDatePicker.getValue();
                checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));
                checkBoxColumn.setVisible(selectedDate.equals(LocalDate.now()));

                checkBoxColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Ticket, Boolean>, ObservableValue>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Ticket, Boolean> cellDataFeatures) {
                        Ticket ticket = cellDataFeatures.getValue();
                        BooleanProperty selected = ticket.selectedProperty();
                        selected.addListener((observable, oldValue, newValue) -> ticket.setSelected(newValue));
                        return selected;
                    }
                });

                // Retrieve data from the result set
                while (resultSet.next())
                    data.add(new Ticket(resultSet.getInt("ticket_number"), resultSet.getInt("quantity"), resultSet.getString("product_name"), resultSet.getInt("price")));
                ObservableList<Ticket> observableData = FXCollections.observableArrayList(data);
                historyTable.setItems(observableData);
                historyTable.refresh();
                resultSet.close();
            } else {
                errorLabel.setText("No records found for selected date range");
            }
        }

    }

    @FXML
    protected  void onPrintButtonClick() throws SQLException {
        ObservableList<Ticket> selectedTickets = FXCollections.observableArrayList();
        ObservableList<Ticket> tableItems = historyTable.getItems();
        for(Ticket ticket: tableItems){
           if(ticket.isSelected()){
               selectedTickets.add(ticket);
           }
        }

        // TODO: refactor based on sales controller
        PrinterJob job = PrinterJob.createPrinterJob();
        for(Ticket ticket : selectedTickets){
            PageLayout pageLayout = job.getJobSettings().getPageLayout();
            PageLayout newPageLayout = PrinterJob.createPrinterJob().getPrinter().createPageLayout(
                    pageLayout.getPaper(), pageLayout.getPageOrientation(), 0, 0,
                    0,
                    0);

            job.getJobSettings().setPageLayout(newPageLayout);

            WebView webView = new WebView();
            webView.getEngine().loadContent(TicketTemplate.getTicketTemplate(ticket, ProductDetailsLoader.getProductDetails().get(ticket.getProduct())));

            if (job != null) {
                /*Thread.sleep(2000);*/
                boolean success = job.printPage(webView);
                if (success) {
                    System.out.println("job successful");
                    job.endJob();

                } else
                    System.out.println("job unsuccessful");
            } else
                System.out.println("error printing");


            if(job.getJobStatus() != PrinterJob.JobStatus.DONE){
                System.out.println("printing");
            }
            else {
                job.endJob();
            }

        }
        System.out.println(job);
        job.endJob();

    }
}
