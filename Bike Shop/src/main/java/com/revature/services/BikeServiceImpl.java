package com.revature.services;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.data.BikeDAO;
import com.revature.data.DAOFactory;
import com.revature.data.OfferDAO;

public class BikeServiceImpl implements BikeService {
	
	private BikeDAO bikeDAO;
	
	private static Logger log = Logger.getLogger(BikeServiceImpl.class);
	
	public BikeServiceImpl() {
		bikeDAO = DAOFactory.getBikeDAO();
	}

	public boolean addBike(Bike b) {
		return bikeDAO.add(b);

	}

	public Bike getBikeById(Integer bikeId) {
		return bikeDAO.getById(bikeId);
	}

	public Set<Bike> getAllBikes() {
		return bikeDAO.getAll();
	}

	public Set<Bike> getAvailableBikes() {
		return bikeDAO.getAvailableBikes();
	}

	public Set<Offer> getOffersByBikeId(Integer bikeId) {
		return bikeDAO.getByBikeId(bikeId);
	}
	
	public Set<Bike> viewOwnedBikes(Integer userId){
		return bikeDAO.viewOwnedBikes(userId);
	}

	public boolean updateBike(Bike b) {
		if (getBikeById(b.getBikeId()) != null) {
			return bikeDAO.update(b);
		}
		else
			log.debug("Bike didn't exist in the database.");
			return false;
	}

	public boolean removeBike(Bike b) {
		if (getBikeById(b.getBikeId()) != null) {
			return bikeDAO.delete(b);
		}	
		else {
			log.debug("Bike didn't exist in the database.");
			return false;
		}

	}

}
