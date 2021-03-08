package com.revature.services;

import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;

public interface BikeService {
	//create
	public boolean addBike(Bike b);
	//read
	public Bike getBikeById(Integer bikeId);
	public Set<Bike> getAllBikes();
	public Set<Bike> getAvailableBikes();	
	public Set<Offer> getOffersByBikeId(Integer bikeId);
	public Set<Bike> viewOwnedBikes(Integer userId);
	//update
	public boolean updateBike(Bike b);	
	//delete
	//removeBike should not be able to happen if the bike is owned.
	public boolean removeBike(Bike b);
	
}
