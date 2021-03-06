package com.banking.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.banking.beans.Customer;
import com.banking.beans.CustomerStatus;
import com.banking.services.UserService;

@WebServlet("/UserController")
public class UserController extends HttpServlet {
	Customer customer = null;
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UserService service = new UserService();

		String action = request.getParameter("action");
		
		
		/*
		 * code for redirecting to page based on user type
		 */
		if (action.equalsIgnoreCase("success")) {
			String loginMessage = request.getParameter("login");
			String role = request.getParameter("role");
			if (loginMessage.equalsIgnoreCase("success") && role.equalsIgnoreCase("executive")) {
				response.sendRedirect("home.jsp");
			}
			if (loginMessage.equalsIgnoreCase("success") && role.equalsIgnoreCase("cashier")) {
				response.sendRedirect("home.jsp");
			}
		}
		
		
		
		/*
		 * code for customer status
		 */

		if (action.equalsIgnoreCase("customerstatus")) {

			try {
				List<CustomerStatus> ListOfCustomerStatus = service.findCustomerStatus();

				if (ListOfCustomerStatus != null) {

					request.setAttribute("listOfCustomerStatus", ListOfCustomerStatus);
					RequestDispatcher rd = request.getRequestDispatcher("customer_status.jsp");
					rd.forward(request, response);
					System.out.println("customers found");

				} else {
					request.setAttribute("message", "No customers found");
					RequestDispatcher rd = request.getRequestDispatcher("executive.jsp");
					rd.forward(request, response);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		UserService service = new UserService();
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		
		
		/*
		 * checking token
		 */
		
		if (session.getAttribute("TOKEN") == null || session.getAttribute("TOKEN") == "") {
			response.sendRedirect("login.jsp");
		}
		response.setHeader("Cache-Control", "no-cache , no-store,must-revalidate");
		
		

		/*
		 * code for customer creation
		 */
		if (action.equalsIgnoreCase("createcustomer")) {

			String token = (String) session.getAttribute("TOKEN");

			String customername = request.getParameter("customername");

			String ssnid = request.getParameter("ssnid");
			Integer age = Integer.parseInt(request.getParameter("age"));
			String city = request.getParameter("city");

			String state = request.getParameter("state");
			String address = request.getParameter("address");

			System.out.println(customername);
			System.out.println(ssnid);
			System.out.println(age);
			System.out.println(address);
			System.out.println(state);
			System.out.println(city);
			System.out.println(token);
			Customer newCustomer = new Customer(ssnid, customername, age, address, state, city);
			try {

				String customerId = service.addCustomer(newCustomer, token);

				if (customerId == "" || customerId.isEmpty()) {
					System.out.println("Sorry customer not created please try again");
					request.setAttribute("message",
							"Sorry Customer Not Created please try again | SNNID should be unique");
					RequestDispatcher rd = request.getRequestDispatcher("executive.jsp");
					rd.forward(request, response);
				} else {
					System.out.println(customerId);
					request.setAttribute("message", "Customer Created with ID : " + customerId);
					RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
					rd.forward(request, response);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (action.equalsIgnoreCase("delete")) {
			String customerId = request.getParameter("customerId");
			boolean deleted = false;
			try {
				deleted = service.deleteCustomer(customerId);
				if (!deleted) {
					RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
					request.setAttribute("message", "Customer Deleted SuccessFully");
					rd.forward(request, response);

				} else {
					RequestDispatcher rd = request.getRequestDispatcher("executive.jsp");

					request.setAttribute("message", "Customer Deletion failed or Customer doesn't exists");
					rd.forward(request, response);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		
		/*
		 * 
		 * code for customer update
		 */

		if (action.equalsIgnoreCase("update")) {
			Customer cust = new Customer();
			String customerId = request.getParameter("customerId");
			boolean updated;
			try {
				cust.setCustomerId(customerId);
				cust.setAddress(request.getParameter("address"));
				cust.setAge(Integer.parseInt(request.getParameter("age")));
				cust.setCustomername(request.getParameter("customername"));
				System.out.println(cust);
				updated = service.updateCustomer(cust);
				if (updated) {
					RequestDispatcher rd = request.getRequestDispatcher("home.jsp");
					request.setAttribute("message", "Customer Updated SuccessFully");
					rd.forward(request, response);

				} else {
					RequestDispatcher rd = request.getRequestDispatcher("executive.jsp");
					request.setAttribute("message", "Customer Updation Failed");
					rd.forward(request, response);

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

}
