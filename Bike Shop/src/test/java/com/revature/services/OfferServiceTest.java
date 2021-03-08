package com.revature.services;

import org.junit.jupiter.api.TestMethodOrder;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.Role;
import com.revature.beans.Status;
import com.revature.beans.User;
import com.revature.data.BikeDAO;
import com.revature.data.OfferPostgres;
import com.revature.data.UserDAO;

import jdk.internal.org.jline.utils.Log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@TestMethodOrder(OrderAnnotation.class)
public class OfferServiceTest {
	public static OfferService offerServ;
	public static UserService userServ;
	public static BikeService bikeServ;
	private Logger log = Logger.getLogger(OfferPostgres.class);
	
	@BeforeAll
	public static void setup() {

		offerServ = new OfferServiceImpl();
		userServ = new UserServiceImpl();
		bikeServ = new BikeServiceImpl();
		//System.out.println("This will happen once before any of the tests");
	}
	
	@Order(1)
	@Test
	public void testGetOfferById() {
		Offer offer = new Offer();
		offer.setOfferId(1);
		offer.setBid(120.11);
		//offer currently contains id = 1, bid = 120.11
		Offer testOffer = new Offer();
		testOffer = offerServ.getOfferById(1);
		assertTrue(offer.equals(testOffer));
	}
	
	@Order(2)
	@Test
	public void testMakeOffer() {
		User user = new User();
		user = userServ.getUserById(3);
		log.debug(user);
		//System.out.println(user.toString());
		//this bike is owned already, so the test should fail
		Bike bike = new Bike();
		bike = bikeServ.getBikeById(3);
		Offer offer = new Offer();
		offer.setOfferId(20);
		offer.setBid(200.10);
		assertFalse(offerServ.makeOffer(offer, bike, user));
		//it should pass this time
		bike = bikeServ.getBikeById(4);
		assertTrue(offerServ.makeOffer(offer, bike, user));
		Offer testOffer = offerServ.getOfferById(20);
		assertTrue(offer.equals(testOffer));
		
	}
	
	@Order(3)
	@Test
	public void testUpdateOffer() {
		Offer offer = new Offer();
		offer.setOfferId(2);
		offer.setBid(53.72);
		assertTrue(offerServ.updateOffer(offer));
		Offer testOffer = offerServ.getOfferById(2);
		assertTrue(offer.equals(testOffer));
	}
	
	@Order(4)
	@Test
	public void testDeleteOffer() {
		//test to see if offer was deleted 
		Offer offer = offerServ.getOfferById(4);
		assertTrue(offerServ.deleteOffer(offer));
		Offer testOffer = offerServ.getOfferById(4);
		assertEquals(null, testOffer);
	}
	
//	@Order(5)
//	@Test
//	public void testAcceptOffer() {
//		//using an offer from the collections
//		Offer offer = offerServ.getOfferById(1);
//		Bike bike = bikeServ.getBikeById(4);
//		User user = userServ.getUserById(1);
//		Set<Offer> bikeoffers = bike.getOffers();
//		//bike's status should be changed
//		Status status = new Status();
//		status = bike.getStatus();
//		
//		assertTrue(offerServ.acceptOffer(offer, bike, user));
//		assertEquals("Owned", status.getName());
//		assertEquals(2, status.getId());
//		//there shouldn't be any offers on the bike's offer list
//		assertTrue(bike.getOffers().isEmpty());
//		//the user's bike list should be updated
//		assertTrue(user.getBikes().contains(bike));
//		//the offer should be deleted from the user's offer list
//		assertFalse(user.getOffers().contains(offer));
//		//the offer should be deleted from the offer collections
//		Offer testOffer = offerServ.getOfferById(1);
//		assertEquals(null, testOffer);
//		//the user should have a payment that corresponds to the offer made
//		Set<Payment> payments = user.getPayments();
//		boolean test = false;
//		for(Payment payment : payments) {
//			if(payment.getBike().equals(bike)) {
//				test = true;
//			}
//		}
//		assertTrue(test);
//		//all offers for that bike should be deleted from the bike
//		assertTrue(bike.getOffers().isEmpty());
//		//all offers for that bike should be deleted from the users collection
//		Set<User> users = userServ.getAllUsers();
//		test = false;
//		for(User usr : users) {
//			Set<Offer> useroffers = usr.getOffers();
//			for(Offer bikeoffer : bikeoffers) {
//				if(useroffers.contains(bikeoffer)) {
//					test = true;
//				}
//			}
//		}
//		assertFalse(test);
//		//all offers for that bike should be deleted from the offer collections
//		Set<Offer> alloffers = offerServ.getAllOffers();
//		assertFalse(alloffers.contains(offer));
//		
//	}
	
//	@Order(6)
//	@Test
//	public void testGetAllOffers() {
//		OfferService temptOfferServ = new OfferServiceImpl();
//		//there should be four offers
//		Set<Offer> allOffers = temptOfferServ.getAllOffers();
//		for(Offer offer : allOffers) {
//			int i = 1;
//			assertEquals(temptOfferServ.getOfferById(i), offer);
//			i++;
//		}
//	}
	
}
