package com.chokshi.deep.pos_system;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class TicketTemplate {

    static DecimalFormat decimalFormat = new DecimalFormat("#, ##0.00");

    public static String getTicketTemplate(Ticket ticket, ProductDetails productDetails) {
        String template = "<!DOCTYPE html>" +
                "<html>"+
                "<head>" +
                "<style>" +
                ".layout {" +
                "width: 50mm; /*3 inches = 216 pixels at 72 pixels per inch*/ "+
                "height: 69mm; /*4 inches = 288 pixels at 72 pixels per inch*/ " +
                /*border: 1px solid black; /* add a border to the layout */
                "display: flex;" +
                "flex-direction: column;"+
                "align-items: center;"+
                "justify-content: center;"+
                "text-align: center;"+
                "font-family: Arial, sans-serif;"+
                "font-weight: bold;"+
                "}"+

                "h1 {"+
                "font-size: 10px;"+
                "margin: 0px;"+
                "}"+
                "h4 {"+
                "font-size: 8px;"+
                "margin: 1px;"+
                "}"+
                "p {"+
                "font-size: 9px;"+
                "text-align: left;"+
                "margin: 0;"+
                "border-bottom: 3px dotted black;"+
                "padding-bottom: 3px;"+
                "width: 100%;"+
                "}"+
                "p1{"+
                "font-size: 6px;"+
                "text-align: left;"+
                "margin: 0;"+
                "padding-bottom: 3px;"+
                "width: 100%;"+
                "}"+
                ".ticket {"+
                "display: flex;"+
                "flex-direction: row;"+
                "align-items: left;"+
                "justify-content: space-between;"+
                "margin-top: 5px;"+
                "width: 100%;"+
                "border-bottom: 3px dotted black;"+
                "padding-bottom: 3px;"+
                "}"+
                ".label {"+
                "font-size: 9px;"+
                "font-weight: bold;"+
                "}"+
                ".value {"+
                "font-size: 9px;"+
                "font-weight: normal;"+
                "border-bottom: 1px solid black;"+
                "padding-bottom: 1px;"+
                "}"+
                ".left {"+
                "display: flex;"+
                "flex-direction: row;"+
                "align-items: left;"+
                "}"+
                ".right {"+
                "display: flex;"+
                "flex-direction: row;"+
                "align-items: right;"+
                "}"+
                ".right .label {"+
                "margin-left: 0px;"+
                "}"+
                "table {"+
                "width: 100%;"+
                "border-collapse: collapse;"+
                "margin-top: 10px;"+
                "font-size: 10px;"+
                "text-align: left;"+
                "}"+
                "th, td {"+
                "border: 0px solid black;"+
                "padding: 5px;"+
                "}"+
                "th {"+
                "font-weight: bold;"+
                "background-color: #ccc;"+
                "}"+
                ".total {"+
                "margin-top: 10px;"+
                "display: flex;"+
                "flex-direction: row;"+
                "align-items: center;"+
                "justify-content: space-between;"+
                "}"+
                ".tabel-label {"+
                "font-weight: bold;"+
                "font-size: 9px;"+
                "}"+
                ".table-value {"+
                "font-size: 8px;"+
                "border-bottom: 1px solid black;"+
                "padding-bottom: 1px;"+
                "}"+
                ".right-align{"+
                "text-align: right;"+
                "}"+
                " .border-bottom{"+
                "border-bottom: 1px dotted black;"+
                "}"+
                ".border-bottom-right{"+
                "text-align: right;"+
                "font-weight: bold;"+
                "border-bottom: 1px dotted black;"+
                "}"+
                ".header {"+
                "display: flex;"+
                "flex-direction: row;"+
                "align-items: left;"+
                "justify-content: space-between;"+
                "margin-top: 5px;"+
                "width: 100%;"+
                "padding-bottom: 3px;"+
                "}"+
                ".logo{"+
                "align-items: left;"+
                "max-width: 100%;"+
                "height: 25px;"+
                "margin: 0px;"+
                "}"+
                ".brand {"+
                "display: flex;"+
                "flex-direction: row;"+
                "align-items: left;"+
                "}"+
                "</style>"+
                "</head>"+
                "<body>"+
                "<div class='layout'>"+
                "<h1 >ADVENTURE WORLD</h1>"+
                "<h4>A JOINT VENTURE OF A.M.C & </h4>"+
                "<h4>SUPERSTAR AMUSEMENT PVT. LTD.</h4>"+
                "<h4>Kankaria Gate No.5, Kankaria Lake Front, Ahmedabad-380008</h4>"+
                "<p>GSTIN: 24AADCS0299R2ZQ</p>"+
                "<div class='ticket'>"+
                "<div class='left'>"+
                "<span class='label'>Ticket Number: </span>"+
                "<span class='value'>"+ ticket.getTicketNumber() + "</span>"+
                "</div>"+
                "<div class='right'>"+
                "<span class='label'>Date: </span>"+
                "<span class='value'>"+ LocalDate.now() +"</span>"+
                "</div>"+
                "</div>"+
                "<table>"+
                "<thead>"+
                "<tr>"+
                "<th>Description</th>"+
                "<th>Qty</th>"+
                "<th>Amount</th>"+
                "</tr>"+
                "</thead>"+
                "<tbody>"+
                "<tr>"+
                "<td>"+ ticket.getProduct()+"</td>"+
                "<td class='border-bottom'>"+ ticket.getQuantity()+"</td>"+
                "<td class='border-bottom'>"+decimalFormat.format(productDetails.getPrice())+"</td>"+
                "</tr>"+
                "<tr>"+
                "<td style='font-size: 9px;' rowspan=2>"+ productDetails.getProductDescription()+"</td>"+
                "<td class='right-align' style='font-size: 8px;'>CGST (9%)</td>"+
                "<td>"+decimalFormat.format((productDetails.getTax())/2)+"</td>"+
                "</tr>"+
                "<tr>"+
                "<td class='right-align' style='font-size: 8px;'>SGST (9%)</td>"+
                "<td>"+decimalFormat.format((productDetails.getTax())/2)+"</td>"+
                "</tr>"+
                "<tr>"+
                "<td></td>"+
                "<td class='border-bottom-right' style='border-top: 1px dotted;'>Total Amount</td>"+
                "<td class='border-bottom' style='font-weight: bold; border-top: 1px dotted; font-size: 18px;'>" + ticket.getPrice()+"</td>"+
                "</tr>"+
                "</tbody>"+
                "</table>"+
                "<h4 style='padding: 4px; margin-top: 4px;'>THANK YOU VISIT AGAIN</h4>"+
                "<p1>T&C*</p1>"+
                "</div>"+
                "</body>"+
                "</html>";

        return template;
    }

}
