package com.revature.data;

import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.User;

public interface OfferDAO extends GenericDAO<Offer> {
	public boolean add(Offer o);
	public boolean makeOffer(Offer o, Bike b, User u);
	public Offer getById(Integer offerId);
	public Set<Offer> getAll();
	public boolean update(Offer o);
	public Integer getUserId(Offer o);
	public Integer getBikeId(Offer o);
	public boolean acceptOffer(Offer o, Bike b);
	public boolean delete(Offer o);
}
