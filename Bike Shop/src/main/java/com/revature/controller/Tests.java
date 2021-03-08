package com.revature.controller;

import java.util.Set;

import com.revature.beans.User;
import com.revature.services.BikeService;
import com.revature.services.BikeServiceImpl;
import com.revature.services.OfferService;
import com.revature.services.OfferServiceImpl;
import com.revature.services.PaymentService;
import com.revature.services.PaymentServiceImpl;
import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;



public class Tests {
	private static BikeService bikeServ = new BikeServiceImpl();
	private static UserService userServ = new UserServiceImpl();
	private static OfferService offerServ = new OfferServiceImpl();
	private static PaymentService paymentServ = new PaymentServiceImpl();
	public static void main(String args []) {

		Set<User> allUsers = userServ.getAllUsers();
		System.out.println(allUsers.toString());
	}

}
