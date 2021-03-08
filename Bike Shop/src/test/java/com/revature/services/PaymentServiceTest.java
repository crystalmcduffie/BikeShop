package com.revature.services;

import java.util.Set;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.Status;
import com.revature.beans.User;
import com.revature.data.PaymentPostgres;

import jdk.internal.org.jline.utils.Log;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class PaymentServiceTest {
	public static PaymentService payServ;
	public static UserService userServ;
	public static BikeService bikeServ;
	public static OfferService offerServ;
	private Logger log = Logger.getLogger(PaymentServiceTest.class);
	
	@BeforeAll
	public static void setup() {	

		userServ = new UserServiceImpl();
		bikeServ = new BikeServiceImpl();
		payServ = new PaymentServiceImpl();
		offerServ = new OfferServiceImpl();
		//System.out.println("This will happen once before any of the tests");
	}
	
	
	@Order(1)
	@Test
	public void testgetPaymentById() {
		Payment payment = new Payment();
		payment.setPaymentId(1);
		payment.setTotalCost(240.10);
		payment.setAmountPaid(120.00);
		Bike bike = new Bike();
		bike.setBikeId(2);
		bike.setBrand("Mongoose");
		bike.setColor("black");
		bike.setModel("BMX");
		Status status = new Status();
		status.setId(2);;
		status.setName("Owned");
		bike.setStatus(status);
		payment.setBike(bike);
		Payment testPayment = new Payment();
		testPayment = payServ.getPaymentById(1);
		log.debug(testPayment);
		log.debug(payment);
		//System.out.println(testPayment.toString());
		//System.out.println(payment.toString());
		assertTrue(payment.equals(testPayment));
	}
	
	@Order(2)
	@Test
	public void testAddPayment() {
		
		Offer offer = offerServ.getOfferById(5);
		Bike testBike = bikeServ.getBikeById(5);
		User user = new User();
		Payment payment = new Payment();
		payment.setPaymentId(5);
		payment.setTotalCost(62.50);
		payment.setAmountPaid(0.0);
		payment.setBike(testBike);
		assertTrue(payServ.createPayment(offer, testBike, user));
		Payment testPayment = payServ.getPaymentById(5);
		assertTrue(payment.equals(testPayment));
	}
	
//	@Order(2)
//	@Test
//	public void testSetPayment() {
//		//test needs to:
//		//make sure the payment was added to the User
//		user = userServ.getUserById(3);
//		bike = bikeServ.getBikeById(6);
//		payment.setPaymentId(7);
//		payment.setTotalCost(500.00);
//		payment.setAmountPaid(300.00);
//		payment.setBike(bike);
//		assertTrue(payServ.setPayment(payment, user));
//		Payment testPayment = payServ.getPaymentById(7);
//		assertTrue(payment.equals(testPayment));
//		assertTrue(user.getPayments().contains(payment));
//	}
	@Order(3)
	@Test
	public void testUpdatePayment() {
		//We shouldn't be able to change the bike
		//associated with the payment
		Payment payment = payServ.getPaymentById(3);
		payment.setTotalCost(450.70);
		payment.setAmountPaid(320.10);
		assertTrue(payServ.updatePayment(payment));
		Payment testPayment = payServ.getPaymentById(3);
		assertEquals(testPayment, payment);
		
	}
	
	@Order(4)
	@Test
	public void testDeletePayment() {
		Payment payment = payServ.getPaymentById(2);
		assertTrue(payServ.deletePayment(payment));
		Payment testPayment = payServ.getPaymentById(2);
		assertEquals(null, testPayment);
	}
	

//	@Order(5)
//	@Test
//	public void testGetAllPayments(){
//		PaymentService temptPayServ = new PaymentServiceImpl();
//		Set<Payment> allPayments = temptPayServ.getAllPayments();
//		for(Payment payment : allPayments) {
//			int i = 1;
//			assertEquals(temptPayServ.getPaymentById(i), payment);
//			i++;
//		}
//		
//	}

}
