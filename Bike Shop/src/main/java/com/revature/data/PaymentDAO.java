package com.revature.data;

import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.User;

public interface PaymentDAO extends GenericDAO<Payment> {
	//create
	public boolean createPayment(Offer o, Bike b, User u);
	//read
	//the employee will use the viewAllPayments method
	public Set<Payment> getAll();
	public Payment getById(Integer paymentId);
	//update
	public boolean update(Payment p);
	public boolean delete(Payment p);
}
