package com.revature.data;

import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.Role;
import com.revature.beans.Status;
import com.revature.beans.User;
import com.revature.exceptions.NonUniqueUsernameException;

public class UserCollections implements UserDAO{
	
	private static Set<User> users;
	private static BikeDAO bikeDAO;
	private static PaymentDAO paymentDAO;
	private static OfferDAO offerDAO;
	
	public UserCollections() {
		
		bikeDAO = new BikeCollections();
		offerDAO = new OfferCollections();
		paymentDAO = new PaymentCollections();
		
		users = new HashSet<User>();
		User u = new User();
		u.setUserId(1);
		u.setUsername("crystal");
		u.setPassword("pass");
		Role r = new Role();
		r.setId(1);
		r.setName("manager");
		u.setRole(r);
		Set<Bike> bikes = new HashSet<>();
		Bike b = new Bike();
		b = bikeDAO.getById(1);
		bikes.add(b);
		b = new Bike();
		b = bikeDAO.getById(2);
		bikes.add(b);
		u.setBikes(bikes);
		Set<Payment> payments = new HashSet<>();
		Payment p = new Payment();
		p = paymentDAO.getById(1);
		payments.add(p);
		p = new Payment();
		p = paymentDAO.getById(2);
		payments.add(p);
		u.setPayments(payments);
		Set<Offer> offers = new HashSet<>();
		Offer o = new Offer();
		o = offerDAO.getById(1);
		offers.add(o);
		o = new Offer();
		o = offerDAO.getById(4);
		offers.add(o);
		u.setOffers(offers);
		users.add(u);
		//System.out.println(users.toString());
		
//		bikes.clear();
//		payments.clear();
//		offers.clear();
		
		u = new User();
		u.setUserId(2);
		u.setUsername("tyler");
		u.setPassword("pass");
		r = new Role();
		r.setId(2);
		r.setName("employee");
		u.setRole(r);
		b = new Bike();
		b = bikeDAO.getById(3);
		bikes = new HashSet<>();
		bikes.add(b);
		u.setBikes(bikes);
		p = new Payment();
		p = paymentDAO.getById(3);
		payments = new HashSet<>();
		payments.add(p);
		u.setPayments(payments);
		o = new Offer();
		o = offerDAO.getById(2);
		offers = new HashSet<>();
		offers.add(o);
		u.setOffers(offers);
		users.add(u);
		
//		bikes.clear();
//		payments.clear();
//		offers.clear();
		
		u = new User();
		u.setUserId(3);
		u.setUsername("ashley");
		u.setPassword("pass");
		r = new Role();
		r.setId(3);
		r.setName("customer");
		u.setRole(r);
		o = new Offer();
		o = offerDAO.getById(3);
		offers = new HashSet<>();
		offers.add(o);
		u.setOffers(offers);
		users.add(u);
	}

	public boolean add(User u) throws NonUniqueUsernameException {
		for(User user : users) {
			if (user.getUsername().equals(u.getUsername())) {
				throw new NonUniqueUsernameException();
			}
		}
		u.setUserId(users.size()+1);
		users.add(u);
		return true;
	
	}

	public User getById(Integer userId) {
		for(User user : users) {
			if(user.getUserId().equals(userId)) {
				return user;
			}
		}
		return null;
	}

	public Set<User> getAll() {
		return users;
	}

	public boolean update(User u) {
		User match = getById(u.getUserId());
		if(match != null) {
			users.remove(match);
			match.setUsername(u.getUsername());
			match.setPassword(u.getPassword());
			match.setRole(u.getRole());
			match.setBikes(u.getBikes());
			match.setOffers(u.getOffers());
			match.setPayments(u.getPayments());
			users.add(match);
			return true;
		}
		return false;
	}

	public boolean delete(User u) {
		return users.remove(u);
	}

	@Override
	public User getUserByUsername(String username) {
		for(User user : users) {
			if(user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}
	


}
