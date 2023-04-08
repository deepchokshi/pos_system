package com.chokshi.deep.pos_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ReportController {

    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;
    @FXML
    Label errorLabel;
	@FXML
	WebView reportWebView;

    FxmlLoaderUtil fxmlLoaderUtil = FxmlLoaderUtil.getInstance();

    @FXML
    protected void onMainMenuButtonClick(ActionEvent event) throws IOException {
        fxmlLoaderUtil.loadFXML(event, "dashboard-view.fxml");
    }
    @FXML
    protected void onSearchButtonClick() throws SQLException {
		String query = "SELECT machine_id, product_name, Sum(price), count(*) as total_quantity FROM pos.ticket where date between ? and ? group by machine_id, product_name";
		// get data from database
		if (DataLoadUtil.validateDatePicker(startDatePicker, endDatePicker, errorLabel)) {
			ResultSet resultSet = DataLoadUtil.getTicketsByDateRange(startDatePicker, endDatePicker, query);
			if (resultSet != null) {
				errorLabel.setText(" ");
				TotalSales totalSales = new TotalSales();

				Map<Integer, MachineTotalSales> machineWiseSales = new HashMap<>();

				// load default ticket list
				for (Machine machine : ProductDetailsLoader.getMachineDetails().values()) {
					MachineTotalSales machineTotalSales = new MachineTotalSales();
					Map<String, Ticket> productWiseTotalSales = new HashMap<>();
					for (String productName : ProductDetailsLoader.getProductDetails().keySet()) {
						productWiseTotalSales.put(productName, new Ticket(0, productName, 0));
					}
					machineTotalSales.setProductWiseTotalSales(productWiseTotalSales);
					machineTotalSales.setMachineName(machine.getMachineName());
					machineTotalSales.setMachineTotalSale(new Ticket());
					machineWiseSales.put(machine.getMachineId(), machineTotalSales);
				}

				Ticket grandTotal = new Ticket();

				while (resultSet.next()) {
					// 1 - machineId, 2-> productName, 3 -> sum, 4 -> total_quantity
					int machineId = resultSet.getInt(1);
					String productName = resultSet.getString(2);
					int sum = resultSet.getInt(3);
					int quantity = resultSet.getInt(4);

					MachineTotalSales machineTotalSales = machineWiseSales.get(machineId);
					machineTotalSales.getMachineTotalSale().appendQuantity(quantity);
					machineTotalSales.getMachineTotalSale().appendPrice(sum);
					machineWiseSales.put(machineId, machineTotalSales);

					Ticket ticket = machineWiseSales.get(machineId).getProductWiseTotalSales().get(productName);
					ticket.setPrice(sum);
					ticket.setQuantity(quantity);
					ticket.setMachineId(machineId);
					machineWiseSales.get(machineId).getProductWiseTotalSales().put(productName, ticket);

					grandTotal.appendPrice(sum);
					grandTotal.appendQuantity(quantity);

				}
				totalSales.setMachineWiseTotalSales(machineWiseSales);
				totalSales.setGrandTotalSales(grandTotal);
				resultSet.close();

				ResultSet IdRangeResultSet = DataLoadUtil.getTicketsByDateRange(startDatePicker, endDatePicker, "select min(ticket_number)as firstId, max(ticket_number) as lastId from ticket where date between ? and ?");
				while (IdRangeResultSet.next()) {
					totalSales.setStartTicketNumber(IdRangeResultSet.getInt(1));
					totalSales.setEndTicketNumber(IdRangeResultSet.getInt(2));
				}

				// call web view
				reportWebView.getEngine().loadContent(getReportTemplate(totalSales, startDatePicker.getValue().toString(), endDatePicker.getValue().toString()));

			} else {
				errorLabel.setText("No records found for selected date range");
			}
		}
	}


	@FXML
	protected  void onPrintButtonClick() {
		PrinterJob job = PrinterJob.createPrinterJob();
		PageLayout pageLayout = job.getJobSettings().getPageLayout();
		PageLayout newPageLayout = PrinterJob.createPrinterJob().getPrinter().createPageLayout(pageLayout.getPaper(), pageLayout.getPageOrientation(), 0, 0,0,0);
		job.getJobSettings().setPageLayout(newPageLayout);
		job.printPage(reportWebView);
		job.endJob();
	}
        // load html template
		private String getReportTemplate(TotalSales totalSales, String startDate, String endDate)  {
			return

					"<!DOCTYPE html>" +
							"<html>" +
							"<head>" +
							"<title>My Layout</title>" +
							"<style>" +

							".layout {" +
							"width: 60mm; /* 3 inches = 216 pixels at 72 pixels per inch */" +
							"height: 120mm; /* 4 inches = 288 pixels at 72 pixels per inch */" +
							"/*border: 1px solid black; /* add a border to the layout */" +
							"display: flex;" +
							"flex-direction: column;" +
							"align-items: center;" +
							"justify-content: center;" +
							"text-align: center;" +
							"font-family: Arial, sans-serif;" +
							"font-weight: bold;" +
							"}" +
							".layout img {" +
							"align-items: left;" +
							"max-width: 60%;" +
							"height: 60px;" +
							"margin: 0px;" +
							"}" +
							"h1 {" +
							"font-size: 12px;" +
							"margin: 0px;" +

							"}" +
							"h4 {" +
							"font-size: 8px;" +
							"margin: 1px;" +

							"}" +
							"p {" +
							"font-size: 9px;" +
							"text-align: left;" +
							"margin: 0;" +
							"border-bottom: 3px dotted black;" +
							"padding-bottom: 3px;" +
							"width: 92%;" +
							"}" +
							"p1{" +
							"font-size: 6px;" +
							"text-align: left;" +
							"margin: 0;" +
							"padding-bottom: 3px;" +
							"width: 92%;" +
							"}" +
							".ticket {" +
							"display: flex;" +
							"flex-direction: row;" +
							"align-items: left;" +
							"justify-content: space-between;" +
							"margin-top: 5px;" +
							"width: 92%;" +
							"border-bottom: 3px dotted black;" +
							"padding-bottom: 3px;" +
							"}" +
							".label {" +
							"font-size: 9px;" +
							"font-weight: bold;" +
							"}" +
							".value {" +
							"font-size: 9px;" +
							"font-weight: normal;" +
							"border-bottom: 1px solid black;" +
							"padding-bottom: 1px;" +
							"}" +
							".left {" +
							"display: flex;" +
							"flex-direction: row;" +
							"align-items: left;" +
							"}" +
							".right {" +
							"display: flex;" +
							"flex-direction: row;" +
							"align-items: right;" +
							"}" +
							".right .label {" +
							"margin-left: 0px;" +
							"}" +
							"table {" +
							"width: 92%;" +
							"border-collapse: collapse;" +
							"margin-top: 10px;" +
							"font-size: 12px;" +
							"text-align: left;" +
							"}" +
							"th, td {" +
							"border: 0px solid black;" +
							"padding: 5px;" +
							"}" +
							"th {" +
							" font-weight: bold;" +
							"background-color: #ccc;" +
							"}" +
							".total {" +
							"margin-top: 10px;" +
							"display: flex;" +
							"flex-direction: row;" +
							"align-items: center;" +
							"justify-content: space-between;" +
							"}" +
							".tabel-label {" +
							"font-weight: bold;" +
							"font-size: 12px;" +
							"}" +
							".table-value {" +
							"font-size: 12px;" +
							"border-bottom: 1px solid black;" +
							"padding-bottom: 1px;" +
							"}" +
							".right-align{" +
							"text-align: right;" +
							"}" +
							".border-bottom{" +
							"/*border-bottom: 1px dotted black;*/" +
							"}" +
							".border-bottom-right{" +
							"text-align: right;" +
							"font-weight: bold;" +
							"border-bottom: 1px dotted black;" +
							"}" +
							".header {" +
							"display: flex;" +
							"flex-direction: row;" +
							"align-items: left;" +
							"justify-content: space-between;" +
							"margin-top: 5px;" +
							"width: 92%;" +
							"padding-bottom: 3px;" +
							"}" +
							".logo{" +
							"align-items: left;" +
							"max-width: 100%;" +
							"height: 25px;" +
							"margin: 0px;" +
							"}" +
							".brand {" +
							"display: flex;" +
							"flex-direction: row;" +
							"align-items: left;" +
							"}" +
							"</style>" +
							"</head>" +
							"<body>" +
							"<div class='layout'>" +
							"<h1 >ADVENTURE WORLD</h1>" +
							"<h4>A JOINT VENTURE OF A.M.C & </h4>" +
							"<h4>SUPERSTAR AMUSEMENT PVT. LTD.</h4>" +
							"<h4>Kankaria Gate No.5, Kankaria Lake Front, Ahmedabad-380008</h4>" +
							"<p>GSTIN: 24AADCS0299R2ZQ</p>" +
							"<div class='ticket'>" +
							"<div class='left'>" +
							"<span class='label'>Date </span>" +
							"<span class='value'>"+startDate+" to " +endDate+"</span>" +
							"</div>" +

							"</div>" +
							"<h4>Ticket Number:" +totalSales.getStartTicketNumber() +" to "+totalSales.endTicketNumber+"</h4>" +
							"<table>" +
							"<thead>" +
							"<tr>" +
							"<th>"+totalSales.getMachineWiseTotalSales().get(1).getMachineName()+"</th>" +
							"<th>Qty</th>" +
							"<th>Amount</th>" +
							"</tr>" +
							"</thead>" +
							"<tbody>" +
							"<tr>" +
							"<td>Big Ride</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).productWiseTotalSales.get("Big Ride").getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).productWiseTotalSales.get("Big Ride").getPrice()+"</td>" +
							"</tr>" +
							"<tr>" +
							"<td>Small Ride</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).productWiseTotalSales.get("Small Ride").getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).productWiseTotalSales.get("Small Ride").getPrice()+"</td>" +
							"</tr>" +
							"<tr>" +
							"<td>Unlimited Adult</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).productWiseTotalSales.get("Unlimited Adult").getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).productWiseTotalSales.get("Unlimited Adult").getPrice()+"</td>" +
							"</tr>" +
							"<tr>" +
							"<td>Unlimited Child</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).productWiseTotalSales.get("Unlimited Child").getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).productWiseTotalSales.get("Unlimited Child").getPrice()+"</td>" +
							"</tr>" +
							" <tr style='border-top: 1px dotted black;'>" +
							"<td>Total</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).getMachineTotalSale().getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(1).getMachineTotalSale().getPrice()+"</td>" +
							"</tr>" +
							"</tbody>" +
							"</table>" +
							"<table>" +
							"<thead>" +
							"<tr>" +
							"<th>"+totalSales.getMachineWiseTotalSales().get(2).getMachineName()+"</th>" +
							"<th>Qty</th>" +
							"<th>Amount</th>" +
							"</tr>" +
							"</thead>" +
							"<tbody>" +
							"<tr>" +
							"<td>Big Ride</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).productWiseTotalSales.get("Big Ride").getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).productWiseTotalSales.get("Big Ride").getPrice()+"</td>" +
							"</tr>" +
							"<tr>" +
							"<td>Small Ride</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).productWiseTotalSales.get("Small Ride").getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).productWiseTotalSales.get("Small Ride").getPrice()+"</td>" +
							"</tr>" +
							"<tr>" +
							"<td>Unlimited Adult</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).productWiseTotalSales.get("Unlimited Adult").getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).productWiseTotalSales.get("Unlimited Adult").getPrice()+"</td>" +
							"</tr>" +
							"<tr>" +
							"<td>Unlimited Child</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).productWiseTotalSales.get("Unlimited Child").getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).productWiseTotalSales.get("Unlimited Child").getPrice()+"</td>" +
							"</tr>" +
							" <tr style='border-top: 1px dotted black;'>" +
							"<td>Total</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).getMachineTotalSale().getQuantity()+"</td>" +
							"<td class='border-bottom'>"+totalSales.getMachineWiseTotalSales().get(2).getMachineTotalSale().getPrice()+"</td>" +
							"</tr>" +
							"</tbody>" +
							"</table>" +
							"<table>" +
							"<thead>" +
							"<tr style='border-top: 1px dotted black; border-bottom: 1px dotted black;'>" +
							"<th>Grand Total</th>" +
							"<th>"+totalSales.grandTotalSales.getQuantity()+"</th>" +
							"<th>"+totalSales.grandTotalSales.getPrice()+"</th>" +
							"</tr>" +
							"</thead>" +
							"</tbody>" +
							"</table>" +
							"</div>" +
							"</body>" +
							"</html>";
		}

}

