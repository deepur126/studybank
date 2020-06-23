<%@page import="com.banking.beans.CustomerStatus"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<style>
table, th, td {
  border: 1px solid black;
  border-collapse: collapse;
  
  
}
table
{
width: 750px}
</style>
<meta charset="ISO-8859-1">
<title>CustomerStatus</title>
<link href="resources/css/style.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">

</head>
<body>
<% if(session.getAttribute("TOKEN")==null || session.getAttribute("TOKEN")=="")
{
	response.sendRedirect("login.jsp");
}
response.setHeader("Cache-Control","no-cache , no-store,must-revalidate");%>
<% String userType = (String)session.getAttribute("USER_TYPE"); %>
<input type="hidden" id="user_Type" value="<%= userType %>">
	<%@ include file="header.jsp"%>

	<br><div>
	<div class="container" id="tab">
	

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>

			<h3>Customer Status</h3>
			<br>
		<table id="example" class="display" cellspacing="0" >
			
			
				<thead>
				<tr>
					<th class="label">Customer Id</th>
					<th class="label">Customer SSN Id</th>
					<th class="label">Customer Status</th>
					<th class="label">Customer Message</th>
					<th class="label">Customer Last Updated</th>
				</tr>
				</thead>

				<%
					List<CustomerStatus> custStatus = (List<CustomerStatus>) request.getAttribute("listOfCustomerStatus");
				%>

				<%
					for (CustomerStatus cust : custStatus) {
				%>
				<tr>
					<td><%=cust.getCustomerId()%></td>
					<td><%=cust.getSsnid()%></td>
					<td><%=cust.getStatus()%></td>
					<td><%=cust.getMessage()%></td>
					<td><%=cust.getLastUpdated()%></td>
				</tr>

				<%}%>
				<br>
				
</table>


</div><br><hr>
<script type="text/javascript">
$(document).ready(function() {
    $('#example').DataTable( {
        "pagingType": "full_numbers"
    } );
} );</script>

<p align="center">
        <input type="button" value="Download to PDF" 
            id="btPrint" class="btn btn-success" onclick="createPDF()"  />
    </p>   
    
 <script>
    function createPDF() {
        var sTable = document.getElementById('tab').innerHTML;

        var style = "<style>";
        style = style + "table {width: 100%;font: 17px Calibri;}";
        style = style + "table, th, td {border: solid 1px #DDD; border-collapse: collapse;";
        style = style + "padding: 2px 3px;text-align: center;}";
        style = style + "</style>";

        // CREATE A WINDOW OBJECT.
        var win = window.open('', '', 'height=700,width=700');

        win.document.write('<html><head>');
        win.document.write('<title>Profile</title>');   // <title> FOR PDF HEADER.
        win.document.write(style);          // ADD STYLE INSIDE THE HEAD TAG.
        win.document.write('</head>');
        win.document.write('<body>');
        win.document.write(sTable);         // THE TABLE CONTENTS INSIDE THE BODY TAG.
        win.document.write('</body></html>');

        win.document.close(); 	// CLOSE THE CURRENT WINDOW.

        win.print();    // PRINT THE CONTENTS.
    }
</script>  

<%@ include file="footer.jsp"%>
</div>				
</body>
</html>