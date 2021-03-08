package com.revature.services;

import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.User;

public interface OfferService {
	//create
	public boolean addOffer(Offer o);
	public boolean acceptOffer(Offer o, Bike b);
	public boolean makeOffer(Offer o, Bike b, User u);
	//read
	public Offer getOfferById(Integer offerId);
	//the getOffer method will make it easier to accept/reject an offer because
	//it will return the Offer object which needs to be passed 
	//into acceptOffer/rejectOffer
	//public Offer getOffer(Integer userId, Integer bikeId);
	public Set<Offer> getAllOffers();
	public Integer getUserId(Offer o);
	public Integer getBikeId(Offer o);
	//update
	//this will allow a user to change their bid price
	//method should not accept a $0 bid
	public boolean updateOffer(Offer o);
	//delete
	//acceptOffer will delete all pending offers after an offer is accepted.
	//acceptOffer will also update bike to owned status
	//public boolean acceptOffer(Offer o, Bike b, User u);
	public boolean deleteOffer(Offer o);
	
}
