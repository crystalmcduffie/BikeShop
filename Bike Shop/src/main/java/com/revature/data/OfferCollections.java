package com.revature.data;

import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.User;

public class OfferCollections implements OfferDAO{
	
	private static Set<Offer> offers;
	
	public OfferCollections() {
		offers = new HashSet<Offer>();
		Offer o = new Offer();
		o.setOfferId(1);
		o.setBid(120.11);
		offers.add(o);
		
		o = new Offer();
		o.setOfferId(2);
		o.setBid(105.40);
		offers.add(o);
		
		o = new Offer();
		o.setOfferId(3);
		o.setBid(152.50);
		offers.add(o);
		
		o = new Offer();
		o.setOfferId(4);
		o.setBid(73.74);
		offers.add(o);
		
		o = new Offer();
		o.setOfferId(5);
		o.setBid(62.50);
		offers.add(o);
	
	}

	public boolean add(Offer o) {
		offers.add(o);
		return true;
	}

	public Set<Offer> getAll() {
		return offers;
	}

	public boolean update(Offer o) {
		Offer match = getById(o.getOfferId());
		if(match != null) {
			match.setOfferId(o.getOfferId());
			match.setBid(o.getBid());
			return true;
		}
		return false;
	}

	public boolean delete(Offer o) {
		if(offers.contains(o)) {
			offers.remove(o);
			return true;
		}
		return false;
	}

	@Override
	public Offer getById(Integer offerId) {
		for(Offer offer : offers) {
			if(offer.getOfferId().equals(offerId)) {
				return offer;
			}
		}
		return null;
	}

	@Override
	public boolean makeOffer(Offer o, Bike b, User u) {
		Set<Offer> bOffers = b.getOffers();
		Set<Offer> uOffers = u.getOffers();
		bOffers.add(o);
		uOffers.add(o);
		offers.add(o);
		return true;
	}

	@Override
	public Integer getUserId(Offer o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getBikeId(Offer o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean acceptOffer(Offer o, Bike b) {
		// TODO Auto-generated method stub
		return false;
	}

}
