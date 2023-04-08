package com.chokshi.deep.pos_system;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import javafx.print.PrinterJob;
import javafx.scene.web.WebView;

import static java.lang.Integer.parseInt;

public class SalesController {

    @FXML
    TextField quantityText;
    @FXML
    TableView salesTable;
    @FXML
    AnchorPane numpadAnchorPane;
    @FXML
    Label errorLabel;
    @FXML
    ToggleGroup productToggleGroup;
    @FXML
    Label totalAmountLabel;

    Map<String, ProductDetails> productDetailsMap = ProductDetailsLoader.getProductDetails();

    FxmlLoaderUtil fxmlLoaderUtil = FxmlLoaderUtil.getInstance();
    int totalPrice = 0;

    public SalesController() throws SQLException {
    }

    public void initialize() {
        productToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Handle button click event here
                System.out.println("Button " + ((ToggleButton) newValue).getText() + " clicked");
                numpadAnchorPane.setDisable(Boolean.FALSE);
            }
        });
    }

    @FXML
    protected void onMainMenuButtonClick(ActionEvent event) throws IOException {
        fxmlLoaderUtil.loadFXML(event, "dashboard-view.fxml");
    }

    @FXML
    protected void onInputButtonClick(ActionEvent event) {
        fxmlLoaderUtil.setValueToTextField(quantityText, event);
    }

    @FXML
    protected void onClearButtonClick() {
        if (quantityText.getLength() > 0)
            quantityText.deleteText(0, quantityText.getLength());
    }

    @FXML
    protected void onEnterButtonClick() {
        if (Objects.isNull(quantityText.getText()) || quantityText.getLength() == 0) {
            errorLabel.setText("Please select number of tickets");
        } else {
            int quantity = parseInt(quantityText.getText());
            totalPrice = parseInt(totalAmountLabel.getText());
            String[] tickets = ((ToggleButton) productToggleGroup.getSelectedToggle()).getText().split("-");
            String productName = tickets[0];
            int productPrice = parseInt(tickets[1]);

            // generate products based on quantity
            List<Ticket> ticketList = new ArrayList<>();
            IntStream.range(0, quantity).mapToObj(i -> new Ticket(1, productName, productPrice)).forEach(ticket -> {
                ticketList.add(ticket);
                totalPrice += productPrice;
            });
            salesTable.getItems().addAll(ticketList);
            totalAmountLabel.setText(String.valueOf(totalPrice));

            onClearButtonClick();
            numpadAnchorPane.setDisable(Boolean.TRUE);
        }
    }

    @FXML
    protected void onPrintButtonClick() throws SQLException, InterruptedException {
        ObservableList<Ticket> tickets = salesTable.getItems();

        // insert into database
        Connection connection = MySQLConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ticket(product_name, price, quantity, machine_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        for (Ticket ticket : tickets) {
            preparedStatement.setString(1, ticket.getProduct());
            preparedStatement.setDouble(2, ticket.getPrice());
            preparedStatement.setInt(3, ticket.getQuantity());
            preparedStatement.setInt(4, StaticDataUtil.getCurrentMachine().getMachineId());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();

        // connect printer to get print
        ResultSet rs = preparedStatement.getGeneratedKeys();
       // PrinterJob job = PrinterJob.createPrinterJob();

        while(rs.next()){
            PrinterJob job = PrinterJob.createPrinterJob();
            PageLayout pageLayout = job.getJobSettings().getPageLayout();
            PageLayout newPageLayout = PrinterJob.createPrinterJob().getPrinter().createPageLayout(
                    pageLayout.getPaper(), pageLayout.getPageOrientation(), 0, 0,
                    0,
                    0);

            job.getJobSettings().setPageLayout(newPageLayout);
            int ticketNumber = rs.getInt(1);
            ResultSet selectResultSet = connection.createStatement().executeQuery("Select * from ticket where ticket_number="+ticketNumber);

            Ticket ticket = null;
            while(selectResultSet.next()){
                ticket =  new Ticket(ticketNumber, selectResultSet.getInt(4), selectResultSet.getString(2), selectResultSet.getInt(3));
            }

           WebView webView = new WebView();
           webView.getEngine().loadContent(TicketTemplate.getTicketTemplate(ticket, productDetailsMap.get(ticket.getProduct())));

           if (job != null) {

                boolean success = job.printPage(webView);
               /*Thread.sleep(2000);*/
               if (success) {
                   System.out.println("job successful");
                   job.endJob();
               } else
                   System.out.println("job unsuccessful");
            } else
                System.out.println("error printing");

        }

          /* if(job.getJobStatus() != PrinterJob.JobStatus.DONE){
                System.out.println("printing");
                *//*Thread.sleep(5000);*//*
            }
            else {
                job.endJob();
            }
            System.out.println(job);
            job.endJob();*/

        // reset values
        totalAmountLabel.setText("0");
        salesTable.getItems().clear();
        salesTable.refresh();
        errorLabel.setText("");
    }



    @FXML
    protected void onDeleteButtonClick() {
        if (salesTable.getSelectionModel().isEmpty()) {
            errorLabel.setText("Please select ticket to delete");
        } else {
            Ticket deleteTicket = (Ticket) salesTable.getSelectionModel().getSelectedItem();
            totalPrice -= deleteTicket.getPrice();
            totalAmountLabel.setText(String.valueOf(totalPrice));
            salesTable.getItems().remove(salesTable.getSelectionModel().getSelectedIndex());
        }
    }

}


