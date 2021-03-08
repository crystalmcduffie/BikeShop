package com.revature.services;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Status;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class BikeServiceTest {
	public static BikeService bikeServ;
	public static OfferService offerServ;
	
	@BeforeAll
	public static void setup() {

		bikeServ = new BikeServiceImpl();
		offerServ = new OfferServiceImpl();
		//System.out.println("This will happen once before any of the tests");
	}
	@Order(1)
	@Test
	//We need this test to work first so we can use it in other tests.
	public void testGetBikebyId() {
		//this is a bike that is in the bike collection
		Bike bike = new Bike();
		bike.setBikeId(4);
		bike.setBrand("Nischi");
		bike.setColor("purple");
		bike.setModel("BMX");
		Status status = new Status();
		status.setId(1);
		status.setName("Available");
		bike.setStatus(status);
		//these are the offers associated with bike #3
		//in the bike collections
		Offer offer = new Offer();
		offer.setOfferId(1);
		offer.setBid(120.11);
		Set<Offer> offers = new HashSet<>();
		offers.add(offer);
		offer = new Offer();
		offer.setOfferId(2);
		offer.setBid(105.40);
		offers.add(offer);
		bike.setOffers(offers);
	//	System.out.println(bike.toString());
		Bike testBike = new Bike();
		testBike = bikeServ.getBikeById(4);
	//	System.out.println(testBike.toString());
		assertTrue(bike.equals(testBike));
	}
	
	@Order(2)
	@Test
	public void testAddBike() {
		//test should fail if status is Owned
		Status status = new Status();
//		status.setId(2);
//		status.setName("Owned");
		Bike bike = new Bike();
		bike.setBikeId(20);
		bike.setBrand("Mongoose");
		bike.setColor("blue");
		bike.setModel("BMX");
//		bike.setStatus(status);
//		assertFalse(bikeServ.addBike(bike));
		
		//The bike should not already have offers
		status.setId(1);
		status.setName("Available");
		bike.setStatus(status);
//		Offer offer = new Offer();
//		offer.setOfferId(5);
//		offer.setBid(200.00);
//		Set<Offer> offers = new HashSet<>();
//		offers.clear();
//		offers.add(offer);
//		bike.setOffers(offers);
//		assertFalse(bikeServ.addBike(bike));
//		
//		offers.clear();
//		bike.setOffers(offers);
		assertTrue(bikeServ.addBike(bike));
		
		Bike testBike = bikeServ.getBikeById(20);
		assertTrue(bike.equals(testBike));
		
//
	}
	@Order(3)
	@Test
	public void testUpdateBike() {
//		//We shouldn't be able to change a bike
//		//that is owned
		Bike testBike = new Bike();
//		testBike = bikeServ.getBikeById(2);
//		testBike.setBrand("Schwinn");
//		testBike.setModel("Mountain");
//		testBike.setColor("red");
//		
//		assertFalse(bikeServ.updateBike(testBike));
		
		//Bike 5 is available, we're just adding
		//an existing offer to it
		testBike = bikeServ.getBikeById(5);
		testBike.setBrand("Mongoose");
		testBike.setModel("Mountain");
		testBike.setColor("red");
		
		Set<Offer> offers = testBike.getOffers();
		Offer offer = offerServ.getOfferById(5);
		offers.add(offer);
		testBike.setOffers(offers);
		assertTrue(bikeServ.updateBike(testBike));
		Bike testBike2 = bikeServ.getBikeById(5);
		assertTrue(testBike.equals(testBike2));
	}

	@Order(4)
	@Test
	public void testRemoveBike() {		
		Bike bike = bikeServ.getBikeById(4);
		assertTrue(bikeServ.removeBike(bike));
		Bike testBike = bikeServ.getBikeById(4);
		assertEquals(null, testBike);
	}

	@Test
	public void testGetAvailableBikes(){
		Set<Bike> availableBikes =  bikeServ.getAvailableBikes();
		for(Bike bike : availableBikes) {
			Status status = bike.getStatus();
			assertEquals("Available", status.getName());
		}
	}
	
	@Test
	public void testGetOffersByBikeId() {
		Set<Offer> testOffers = new HashSet<Offer>();
		testOffers = bikeServ.getOffersByBikeId(5);
		Set<Offer> knownOffers = new HashSet<Offer>();
		knownOffers.add(offerServ.getOfferById(3));
		knownOffers.add(offerServ.getOfferById(4));
		for(Offer offer : knownOffers) {
			assertTrue(testOffers.contains(offer));
		}

	}

}
