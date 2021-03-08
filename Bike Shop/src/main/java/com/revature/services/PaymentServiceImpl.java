package com.revature.services;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.User;
import com.revature.data.BikeDAO;
import com.revature.data.DAOFactory;
import com.revature.data.PaymentDAO;
import com.revature.data.UserDAO;

public class PaymentServiceImpl implements PaymentService{
	private PaymentDAO paymentDAO;
	private UserDAO userDAO;
	
	private static Logger log = Logger.getLogger(PaymentServiceImpl.class);
	
	public PaymentServiceImpl() {
		paymentDAO = DAOFactory.getPaymentDAO();
		userDAO = DAOFactory.getUserDAO();
	}
	

	public Payment getPaymentById(Integer id) {
		return paymentDAO.getById(id);
	}

	@Override
	public Set<Payment> getAllPayments() {
		return paymentDAO.getAll();
	}

	
	public boolean updatePayment(Payment p) {
		if(getPaymentById(p.getPaymentId()) != null) {
			return paymentDAO.update(p);
		}
			
		else
			log.debug("Payment didn't exist in the database");
			return false;
	}

	public boolean deletePayment(Payment p) {
		if(getPaymentById(p.getPaymentId()) != null) {
			return paymentDAO.delete(p);
		}
			
		else
			log.debug("Payment didn't exist in the database");
			return false;
	}

	@Override
	public boolean createPayment(Offer o, Bike b, User u) {
		return paymentDAO.createPayment(o,b,u);
	}

}
