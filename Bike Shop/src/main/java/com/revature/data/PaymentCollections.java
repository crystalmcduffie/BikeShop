package com.revature.data;

import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.User;
import com.revature.services.BikeService;

public class PaymentCollections implements PaymentDAO{
	
	private static Set<Payment> payments;
	private static BikeDAO bikeDAO;
	
	public PaymentCollections() {
		bikeDAO = new BikeCollections();
		payments = new HashSet<Payment>();
		Payment p = new Payment();
		p.setPaymentId(1);
		p.setTotalCost(240.10);
		p.setAmountPaid(120.00);
		p.setBike(bikeDAO.getById(2));
		payments.add(p);
		
		p = new Payment();
		p.setPaymentId(2);
		p.setTotalCost(305.75);
		p.setAmountPaid(200.00);
		p.setBike(bikeDAO.getById(1));
		payments.add(p);
		
		p = new Payment();
		p.setPaymentId(3);
		p.setTotalCost(102.25);
		p.setAmountPaid(52.25);
		p.setBike(bikeDAO.getById(3));
		payments.add(p);
		
		p = new Payment();
		p.setPaymentId(4);
		p.setTotalCost(500.50);
		p.setAmountPaid(100.00);
		p.setBike(bikeDAO.getById(6));
		payments.add(p);
	}

	public boolean add(Payment p) {
		return payments.add(p);
	}

	@Override
	public Payment getById(Integer paymentId) {
		for(Payment payment : payments) {
			if(payment.getPaymentId().equals(paymentId)) {
				return payment;
			}
		}
		return null;
	}

	public Set<Payment> getAll() {
		return payments;
	}

	public boolean update(Payment p) {
		Payment match = getById(p.getPaymentId());
		if(match != null) {
			match.setPaymentId(p.getPaymentId());
			match.setTotalCost(p.getTotalCost());
			match.setAmountPaid(p.getAmountPaid());
			return true;
		}
		return false;
	}

	public boolean delete(Payment p) {
		return payments.remove(p);
	}

	@Override
	public boolean createPayment(Offer o, Bike b, User u) {
		Integer payId = 0;
		for(Payment payment : payments) {
			if(payId <= payment.getPaymentId()) {
				payId = payment.getPaymentId() + 1;
			}
		}
		Payment payment = new Payment();
		payment.setPaymentId(payId);
		payment.setTotalCost(o.getBid());
		payment.setAmountPaid(0.0);
		payment.setBike(b);
		payments.add(payment);
		return true;
	}



}
