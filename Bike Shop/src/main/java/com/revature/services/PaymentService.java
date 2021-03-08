package com.revature.services;

import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.User;

public interface PaymentService {
	//create
	//public boolean addPayment(Payment p); 
	public boolean createPayment(Offer o, Bike b, User u);
	//read
	public Payment getPaymentById(Integer id);
	public Set<Payment> getAllPayments();
	//update
	public boolean updatePayment(Payment p);
	//delete
	public boolean deletePayment(Payment p);
}
