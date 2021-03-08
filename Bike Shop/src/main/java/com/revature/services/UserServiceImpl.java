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
import com.revature.exceptions.NonUniqueUsernameException;

public class UserServiceImpl implements UserService{
	
	private UserDAO userDAO;
	private BikeDAO bikeDAO;
	private PaymentDAO paymentDAO;
	private OfferDAO offerDAO;
	
	private static Logger log = Logger.getLogger(PaymentServiceImpl.class);
	
	public UserServiceImpl() {
		userDAO = DAOFactory.getUserDAO();
		bikeDAO = DAOFactory.getBikeDAO();
		paymentDAO = DAOFactory.getPaymentDAO();
		offerDAO = DAOFactory.getOfferDAO();
	}

	public boolean addCustomer(User u) throws NonUniqueUsernameException {
		User user = getUserByUsername(u.getUsername());
		if(user == null) {
			return userDAO.add(u);
		}else {
			throw new NonUniqueUsernameException();
			//return false;
		}
		
	}

	public User getUserById(Integer userId) {
		return userDAO.getById(userId);
	}

	public User getUserByUsername(String username){
		return userDAO.getUserByUsername(username);
	}
	
	@Override
	public Set<User> getAllUsers() {
		return userDAO.getAll();
	}
	
	public Set<Bike> viewOwnedBikes(User u) {
		return u.getBikes();
	}
	
	public Set<Offer> getUserOffers(User u) {
		return u.getOffers();
	}

	public Set<Payment> viewRemainingPayments(User u) {
		return u.getPayments();
	}
	
	public boolean updateUser(User u) {
		if(getUserById(u.getUserId()) != null) {
			//we shouldn't be able to give this user a bike that 
			//still has a status of available
			Set<Bike> userBikes = u.getBikes();
			for(Bike bike :userBikes) {
				if(bike.getStatus().getId() == 1) {
					return false;
				}
			}
			
			return userDAO.update(u);
		}
		else {
			log.debug("User doesn't exist in the database.");
			return false;
		}
		
	}

	public boolean deleteUser(User u) {
		if (getUserById(u.getUserId()) != null) {
			return userDAO.delete(u);
		}	
		else {
			log.debug("User didn't exist in the database.");
			return false;
		}
		
	}



}
