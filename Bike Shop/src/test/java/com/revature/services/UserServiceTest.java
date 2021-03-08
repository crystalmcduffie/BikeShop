package com.revature.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

import com.revature.beans.*;
import com.revature.data.PaymentPostgres;
import com.revature.exceptions.NonUniqueUsernameException;

@TestMethodOrder(OrderAnnotation.class)
public class UserServiceTest {
	public static UserService userServ;
	public static BikeService bikeServ;
	public static PaymentService payServ;
	public static OfferService offerServ;
	private Logger log = Logger.getLogger(UserServiceTest.class);
	//private Logger log = Logger.getLogger(UserServiceTest.class);
	
	@BeforeAll
	public static void setup() {
		//System.out.println("This will happen once before any of the tests");
		
		
		userServ = new UserServiceImpl();
		bikeServ = new BikeServiceImpl();
		payServ = new PaymentServiceImpl();
		offerServ = new OfferServiceImpl();
		System.out.println("End of setup");
	}

	@Order(1)
	@Test
	public void testGetUserById() {
		User user = new User();
		user.setUserId(1);
		user.setUsername("crystal");
		user.setPassword("pass");
		Role role= new Role();
		role.setId(1);
		role.setName("manager");
		user.setRole(role);
		Bike bike = new Bike();
		bike.setBikeId(1);
		bike.setBrand("Nischi");
		bike.setColor("grey");
		bike.setModel("Edge");
		Status status = new Status ();
		status.setId(2);
		status.setName("Owned");
		bike.setStatus(status);
		Set <Bike>bikes = new HashSet<>();
		bikes.add(bike);
		Payment payment = new Payment();
		payment.setPaymentId(2);
		payment.setTotalCost(305.75);
		payment.setAmountPaid(200.00);
		payment.setBike(bike);
		Set <Payment> payments = new HashSet<>();
		payments.add(payment);
		bike = new Bike();
		bike.setBikeId(2);
		bike.setBrand("Mongoose");
		bike.setColor("black");
		bike.setModel("BMX");
		bike.setStatus(status);
		bikes.add(bike);
		payment = new Payment();
		payment.setPaymentId(1);
		payment.setTotalCost(240.10);
		payment.setAmountPaid(120.00);
		payment.setBike(bike);
		payments.add(payment);
		user.setBikes(bikes);
		user.setPayments(payments);
		Offer offer = new Offer();
		offer.setOfferId(1);
		offer.setBid(120.11);
		Set <Offer> offers = new HashSet<>();
		offers.add(offer);
		offer = new Offer();
		offer.setOfferId(4);
		offer.setBid(73.74);
		offers.add(offer);
		user.setOffers(offers);
		System.out.println("Test 1");
		User testUser = new User();
		log.debug(user);
		//System.out.println(user.toString());
		testUser = userServ.getUserById(1);
		log.debug(testUser);
		//System.out.println(testUser.toString());
		assertTrue(user.equals(testUser));
	}
	
	@Order(2)
	@Test
	public void testAddCustomer() throws NonUniqueUsernameException {
		//first we'll test the username not unique exception
		User testUser = new User();
		testUser.setUserId(10);
		testUser.setUsername("crystal");
		testUser.setPassword("pass");
		Role role = new Role ();
		role.setId(3);
		role.setName("customer");
		testUser.setRole(role);
		//we can assume that the user's bike, offers, and payments
		//sets will be empty
		assertThrows(NonUniqueUsernameException.class, () -> {
			userServ.addCustomer(testUser);
		});
		
		testUser.setUsername("christi");
		assertTrue(userServ.addCustomer(testUser));
	}
	
	@Order(4)
	@Test
	public void testUpdateUser() {
		//we're assuming that this user's roles can't change
		//through the controller
		User user = userServ.getUserById(1);
		user.setUsername("christi");
		user.setPassword("passypass");

		
		//we shouldn't be able to give this user a bike that 
		//still has a status of available
		Set<Bike> bikes = user.getBikes();
		Bike bike = bikeServ.getBikeById(5);
		Status status = new Status();
		status.setId(1);
		status.setName("Available");
		bike.setStatus(status);
		bikes.add(bike);
		user.setBikes(bikes);
		assertFalse(userServ.updateUser(user));
		
		bikes.remove(bike);
		status.setId(2);
		status.setName("Owned");;
		bikes.add(bike);
		user.setBikes(bikes);
		assertTrue(userServ.updateUser(user));

	}
	
	@Order(5)
	@Test
	public void testViewOwnedBikes() {
		User user = userServ.getUserById(1);
		Set<Bike> ownedBikes = userServ.viewOwnedBikes(user);
		//Known output
		Set<Bike> knownBikes = user.getBikes();

//		}
		for(Bike bike : knownBikes) {
			assertTrue(ownedBikes.contains(bike));
		}
		
	}
	
	@Order(6)
	@Test
	public void testViewRemainingPayments (){
		User user = userServ.getUserById(1);
		Set<Payment> userPayments = userServ.viewRemainingPayments(user);
		//Known output
		Set<Payment> knownPayments = user.getPayments();

		for(Payment payment : knownPayments) {
			assertTrue(userPayments.contains(payment));
		}
		
	}
	
//	@Order(7)
//	@Test
//	public void testGetOffersByUserId() {
//		User user = userServ.getUserById(3);
//		Set<Offer> testOffers = userServ.getOffersByUserId(3);
//		//Known output
//		Set <Offer> knownOffers = user.getOffers();
//		Iterator<Offer> testItr = testOffers.iterator();
//		Iterator<Offer> offersItr = knownOffers.iterator();
//		while(testItr.hasNext()) {
//			assertEquals(testItr.next(), offersItr.next());
//		}
//	}
	
	@Order(8)
	@Test
	public void testDeleteUser() {
		User user = userServ.getUserById(3);
		
		assertTrue(userServ.deleteUser(user));
	}
	
}
