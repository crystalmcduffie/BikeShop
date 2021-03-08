package com.revature.services;

import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.User;
import com.revature.exceptions.NonUniqueUsernameException;

public interface UserService {
	// create
	public boolean addCustomer(User u) throws NonUniqueUsernameException;
	// read
	public User getUserById(Integer userId);
	public User getUserByUsername(String username);
	public Set<User> getAllUsers();
	public Set<Bike> viewOwnedBikes(User u);
	//returns a list sorted by bike model # and payment value
	public Set<Payment> viewRemainingPayments(User u);
	public Set<Offer> getUserOffers(User u);
	// update
	public boolean updateUser(User u);
	// delete
	public boolean deleteUser(User u);

}
