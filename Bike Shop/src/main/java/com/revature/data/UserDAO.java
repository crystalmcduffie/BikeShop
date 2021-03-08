package com.revature.data;

import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.User;
import com.revature.exceptions.NonUniqueUsernameException;

public interface UserDAO extends GenericDAO<User> {
	public boolean add(User u) throws NonUniqueUsernameException;
	public User getById(Integer userId);
	public User getUserByUsername(String username);
	public Set<User> getAll();
	public boolean update(User u);
	public boolean delete(User u);
}