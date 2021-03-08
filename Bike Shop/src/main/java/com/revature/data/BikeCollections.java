package com.revature.data;

import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Status;
import com.revature.beans.User;
import com.revature.services.UserService;

public class BikeCollections implements BikeDAO{
	
	private static Set<Bike> bikes;
	private static OfferDAO offerDAO;
	private static UserService userServ;
	
	public BikeCollections() {
		
		offerDAO = new OfferCollections();
		
		bikes = new HashSet<Bike>();
		Bike b = new Bike();
		b.setBikeId(1);
		b.setBrand("Nischi");
		b.setColor("grey");
		b.setModel("Edge");
		Status s = new Status();
		s.setId(2);
		s.setName("Owned");
		b.setStatus(s);
		bikes.add(b);
		
		b = new Bike();
		b.setBikeId(2);
		b.setBrand("Mongoose");
		b.setColor("black");
		b.setModel("BMX");
		s = new Status();
		s.setId(2);
		s.setName("Owned");
		b.setStatus(s);
		bikes.add(b);
		
		b = new Bike();
		b.setBikeId(3);
		b.setBrand("Schwinn");
		b.setColor("white");
		b.setModel("Mountain");
		s = new Status();
		s.setId(2);
		s.setName("Owned");
		b.setStatus(s);
		bikes.add(b);
		
		b = new Bike();
		b.setBikeId(4);
		b.setBrand("Nischi");
		b.setColor("purple");
		b.setModel("BMX");
		s = new Status();
		s.setId(1);
		s.setName("Available");
		b.setStatus(s);
		Set<Offer> offers = new HashSet<>();
		Offer o = new Offer();
		offers.add(offerDAO.getById(1));
		offers.add(offerDAO.getById(2));
		b.setOffers(offers);
		bikes.add(b);
		
		//o.clear();
		 
		b = new Bike();
		b.setBikeId(5);
		b.setBrand("Schwinn");
		b.setColor("black");
		b.setModel("Mountain");
		s = new Status();
		s.setId(1);
		s.setName("Available");
		b.setStatus(s);
		offers = new HashSet<>();
		offers.add(offerDAO.getById(3));
		offers.add(offerDAO.getById(4));
		b.setOffers(offers);
		bikes.add(b);
		
		b = new Bike();
		b.setBikeId(6);
		b.setBrand("Mongoose");
		b.setColor("white");
		b.setModel("BMX");
		s = new Status();
		s.setId(2);
		s.setName("Owned");
		b.setStatus(s);
		bikes.add(b);
		
	}

	public boolean add(Bike b) {
		bikes.add(b);
		return true;
	}

	public Bike getById(Integer id) {
		for(Bike bike : bikes) {
			if(bike.getBikeId().equals(id)) {
				return bike;
			}
		}
		return null;
	}
	
	public Set<Bike> getAll() {
		return bikes;
	}

	public Set<Bike> getAvailableBikes() {
		Set<Bike> aBikes = new HashSet<>();
		for(Bike bike : bikes) {
			if(bike.getStatus().getId() == 1)
				aBikes.add(bike);
		}
		return aBikes;
	}



	public boolean update(Bike b) {
		Bike match = getById(b.getBikeId());
		if(match != null) {
			bikes.remove(match);
			match.setBikeId(b.getBikeId());
			match.setBrand(b.getBrand());
			match.setColor(b.getColor());
			match.setModel(b.getModel());
			match.setStatus(b.getStatus());
			match.setOffers(b.getOffers());
			bikes.add(match);
			return true;
		}
		return false;
	}

	public boolean delete(Bike b) {
		return bikes.remove(b);
	}

	@Override
	public Set<Offer> getByBikeId(Integer bikeId) {
		Bike bike = getById(bikeId);
		return bike.getOffers();
	}

	@Override
	public Set<Bike> viewOwnedBikes(Integer userId) {
		User user = userServ.getUserById(userId);
		return user.getBikes();
	}

}
