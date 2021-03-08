package com.revature.services;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.Status;
import com.revature.beans.User;
import com.revature.data.BikeDAO;
import com.revature.data.DAOFactory;
import com.revature.data.OfferDAO;
import com.revature.data.PaymentDAO;
import com.revature.data.UserDAO;

public class OfferServiceImpl implements OfferService {
	private OfferDAO offerDAO;
	private UserDAO userDAO;
	private BikeDAO bikeDAO;
	private PaymentDAO paymentDAO;
	
	private static Logger log = Logger.getLogger(OfferServiceImpl.class);

	public OfferServiceImpl() {
		offerDAO = DAOFactory.getOfferDAO();
		userDAO = DAOFactory.getUserDAO();
		bikeDAO = DAOFactory.getBikeDAO();
		paymentDAO = DAOFactory.getPaymentDAO();
	}
	
	@Override
	public boolean addOffer(Offer o) {
		return offerDAO.add(o);
	}
	
	public boolean makeOffer(Offer o, Bike b, User u) {
		//bike cannot be owned
		//bike cannot have this offer on it already
		if(b.getStatus().getId() == 1) {
//			if(b.getOffers().contains(o)) {
//				return false;
//			}
//			//if the user has placed an offer on this bike already
//			//we will return false
//			else {
//				Set<Offer> userOffers = u.getOffers();
//				Set<Offer> bikeOffers = b.getOffers();
//				for(Offer offer : bikeOffers) {
//					if(userOffers.contains(offer)) {
//						return false;
//					}
//				}
//					offerDAO.add(o);
//					Set<Offer> offers = b.getOffers();
//					offers.add(o);
//					bikeDAO.update(b);
//					offers = u.getOffers();
//					userDAO.update(u);
//					return true;
//				
//			}
			offerDAO.makeOffer(o, b, u);
			return true;
		}
		return false;
	}

	public Offer getOfferById(Integer offerId) {
		return offerDAO.getById(offerId);
	}

	public Set<Offer> getAllOffers() {
		return offerDAO.getAll();
	}

	public boolean updateOffer(Offer o) {
		if (getOfferById(o.getOfferId()) != null) {
			return offerDAO.update(o);
		}
		else {
			log.debug("Offer didn't exist in the database.");
			return false;
		}
	}

	public boolean deleteOffer(Offer o) {
		if (getOfferById(o.getOfferId()) != null) {
			return offerDAO.delete(o);
		}
		else {
			log.debug("Offer didn't exist in the databse.");
			return false;
		}

	}
	
	public Integer getUserId(Offer o) {
		return offerDAO.getUserId(o);
	}

	public Integer getBikeId(Offer o) {
		return offerDAO.getBikeId(o);
	}

	@Override
	public boolean acceptOffer(Offer o, Bike b) {
		return offerDAO.acceptOffer(o,b);
	}
}
