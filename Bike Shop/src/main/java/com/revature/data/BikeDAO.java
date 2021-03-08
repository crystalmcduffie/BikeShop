package com.revature.data;

import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;

public interface BikeDAO extends GenericDAO<Bike> {
	
	public boolean add(Bike b);
	public Bike getById(Integer id);
	public Set<Offer> getByBikeId(Integer bikeId);
	public Set<Bike> getAll();
	public Set<Bike> getAvailableBikes();
	public boolean update(Bike b);
	public boolean delete(Bike b);
	public Set<Bike> viewOwnedBikes(Integer userId);
	
}
