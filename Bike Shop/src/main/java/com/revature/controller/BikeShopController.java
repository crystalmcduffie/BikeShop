package com.revature.controller;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.Role;
import com.revature.beans.Status;
import com.revature.beans.User;
import com.revature.exceptions.NonUniqueUsernameException;
import com.revature.services.*;


public class BikeShopController {

	private static BikeService bikeServ = new BikeServiceImpl();
	private static UserService userServ = new UserServiceImpl();
	private static OfferService offerServ = new OfferServiceImpl();
	private static PaymentService paymentServ = new PaymentServiceImpl();
	private static Scanner scan;
	private static Logger log = Logger.getLogger(BikeShopController.class);
	
	public static void main(String[] args) {
		scan = new Scanner(System.in);
		boolean userActive = true;
		
		mainLoop: while (userActive) {
			System.out.println("Hello! Welcome to the Bike Shop!");
			User loggedInUser = null;
			
			while (loggedInUser == null) {
				System.out.println("What would you like to do?");
				System.out.println("1. Register\n" 
								+ "2. Log in\n"
								+ "Other. Exit");
				String userChoice = scan.nextLine();
				
				switch (userChoice) {
				case "1":
					//log.info("User is registering an account.");
					loggedInUser = registerUser();
					break;
				case "2":
					//log.info("User is logging in.");
					loggedInUser = logInUser();
					break;
				default:
					//log.info("User is exiting the application.");
					userActive = false;
					break mainLoop;
				}
			}
		menuLoop: while (true) {
			System.out.println("What would you like to do?");
			System.out.println("1. View available bikes\n2. View my bikes"
					+ "\n3. View my payments");
			if ("User".equals(loggedInUser.getRole().getName()))
				System.out.println("Other. Log out");
			else if ("Employee".equals(loggedInUser.getRole().getName()))
				System.out.println("4. View all payments\n5. Manage offers"
						+ "\n6. Manage bikes\nOther. Log out");
			
			String input = scan.nextLine();
			switch (input) {
			case "1":
				loggedInUser = viewAvailableBikes(loggedInUser);
				break;
			case "2":
				loggedInUser = viewOwnedBikes(loggedInUser);
				break;
			case "3":
				loggedInUser = viewMyPayments(loggedInUser);
				break;
			case "4":
				//place if statement to check the Role
				loggedInUser = viewAllPayments(loggedInUser);
				break;
			case "5":
				loggedInUser = manageOffers(loggedInUser);
				break;
			case "6":
				loggedInUser = manageBikes(loggedInUser);
				break;
			default:
				System.out.println("See you next time!");
				//log.info("User logged out.");
				loggedInUser = null;
				break menuLoop;
			}
			if (loggedInUser == null) {
				System.out.println("See you next time!");
				//log.info("User logged out.");
				break menuLoop;
			}
		}
		scan.close();}
	}


	// returns the person that registered.
	private static User registerUser() {
		while (true) {
			User newAccount = new User();
			System.out.println("Enter a username: ");
			newAccount.setUsername(scan.nextLine());
			System.out.println("Enter a password: ");
			newAccount.setPassword(scan.nextLine());
			Role userRole = new Role();
			// TODO get this from the database
			userRole.setId(3);
			userRole.setName("Customer");
			newAccount.setRole(userRole);
			newAccount.setUserId(0);
			
			System.out.println("Does this look good?");
			System.out.println("Username: " + newAccount.getUsername() + 
					" Password: " + newAccount.getPassword());
			System.out.println("1 to confirm, 2 to start over, other to cancel");
			String input = scan.nextLine();
			switch (input) {
			case "1":
				//log.debug("Submitting new user to the database...");
				try {
					userServ.addCustomer(newAccount);
					//log.debug(newAccount);
					System.out.println("Confirmed. Welcome to Bike Shop.");
					return newAccount;
				} catch (NonUniqueUsernameException e) {
					System.out.println("Sorry, that username is taken. Try again!");
					//log.warn("User tried to register with a non-unique username.");
				}
				break;
			case "2":
				System.out.println("Okay, let's try again.");
				break;
			default:
				System.out.println("Okay, let's go back.");
				return null;
			}
		}
	}
		
		// returns the person who logs in
		private static User logInUser() {
			while (true) {
				System.out.println("Enter username: ");
				String username = scan.nextLine();
				System.out.println("Enter password: ");
				String password = scan.nextLine();
				
				User user = userServ.getUserByUsername(username);
				if (user == null) {
					//log.debug("User entered a username that doesn't exist.");
					System.out.println("Nobody exists with that username.");
				} else if (user.getPassword().equals(password)) {
					//log.debug("User logged in successfully.");
					//log.debug(user);
					System.out.println("Welcome back!");
					return user;
				} else {
					//log.debug("User entered an incorrect password.");
					System.out.println("That password is incorrect.");
				}
				System.out.println("Do you want to try again? 1 for yes, other for no.");
				String input = scan.nextLine();
				if (!("1".equals(input)))
					return null;
			}
		}
		
		private static User viewAvailableBikes(User loggedInUser) {
			Set<Bike> availableBikes = bikeServ.getAvailableBikes();
			Set<Integer> bikeIds = new HashSet<>();
			for (Bike bike : availableBikes) {
				System.out.println(bike);
				bikeIds.add(bike.getBikeId());
			}
			
			System.out.println("Would you like to make an offer for a bike? 1 for yes, other for no");
			String input = scan.nextLine();
			if ("1".equals(input)) {
				while (true) {
					Integer flag = 0;
					while(flag == 0) {
						System.out.println("Which bike? Type its ID.");
						input = scan.nextLine();
						if(bikeIds.contains(Integer.valueOf(input))){
							flag = 1;
						}
						else {
							System.out.println("Sorry, that's not an available bike."
									+ " Let's try again.");
						}
					}
					Bike bike = bikeServ.getBikeById(Integer.valueOf(input));
					if (bike != null && bike.getStatus().getName().equals("Available")) {
						System.out.println(bike);
						System.out.println("You want to make an offer for the " + bike.getColor() + " "
								+ bike.getBrand() + " model " + bike.getModel()
								+"? 1 for yes, other for no");
						input = scan.nextLine();
						if ("1".equals(input)) {
							System.out.println("Please enter an amount: ");
							//input = scan.nextLine();
							//enter clause for invalid input
							Double bid = 0.0;
							flag = 0;
							while(flag == 0) {
								try{
									input = scan.nextLine();
									bid = Double.parseDouble(input);
									flag = 1;
								}catch(NumberFormatException e) {
									System.out.println("Incorrect format.");
								}
							}
							Offer offer = new Offer();
							offer.setBid(bid);
							offerServ.makeOffer(offer, bike, loggedInUser);
							System.out.println("Great! We hope you'll win the bid.");
							break;
						} else {
							System.out.println("Okay, did you want to bid on a different bike? 1 for yes, other for no");
							input = scan.nextLine();
							if (input != "1")
								break;
						}
					} else {
						System.out.println("Sorry, that's not an available bike. Do you want to try again?"
								+ " 1 for yes, other for no");
						input = scan.nextLine();
						if (input != "1") {
							System.out.println("Okay, no worries.");
							break;
						}
					}
				}
			} else {
				System.out.println("Okay, no worries.");
			}
			
			return loggedInUser;
		}
		
		private static User viewOwnedBikes(User loggedInUser) {
			//Refresh user object
			loggedInUser = userServ.getUserByUsername(loggedInUser.getUsername());
			Set<Bike> ownedBikes = userServ.viewOwnedBikes(loggedInUser);
			
			String input;
			if(ownedBikes.isEmpty()) {
				System.out.println("It looks like you don't have any bikes. Would you "
						+ "like to see what we have available?"
						+ "\n1. Yes"
						+ "\nOther. No");
				input = scan.nextLine();
				if("1".equals(input)) {
					viewAvailableBikes(loggedInUser);
					return loggedInUser;
				}
				else {
					System.out.println("Okie dokes.");
					return loggedInUser;
				}
			}else {
				System.out.println("These are your bikes: ");
				for(Bike bike : ownedBikes) {
					System.out.println(bike);
				}
				System.out.println("What would you like to do next?"
						+ "\n1. View my payments"
						+ "\n2. Look at available bikes"
						+ "\nOther. Return to menu");
				input = scan.nextLine();
				switch (input) {
				case "1":
					viewMyPayments(loggedInUser);
					return loggedInUser;
				case "2":
					viewAvailableBikes(loggedInUser);
					return loggedInUser;
				default:
					return loggedInUser;
				}
				
			}
			
		}

		
		private static User viewMyPayments(User loggedInUser) {
			//Refresh user object
			loggedInUser = userServ.getUserByUsername(loggedInUser.getUsername());
			Set<Payment> userPayments = userServ.viewRemainingPayments(loggedInUser);
			String input;
			if(userPayments.isEmpty()) {
				System.out.println("hmmm... you don't have a payment history. You must not have"
						+ " purchased a bike. Would you like to see what bikes we have available?"
						+ "\n1. Yes"
						+ "\nOther. No");
				input = scan.nextLine();
				if("1".equals(input)) {
					viewAvailableBikes(loggedInUser);
					return loggedInUser;
				}
				else {
					System.out.println("Okay, let's go back to the main menu.");
					return loggedInUser;
				}
			}
			else {
				Set<Integer> paymentIds = new HashSet<>();
				System.out.println("These are your balances: ");
				for(Payment payment : userPayments) {
					System.out.println(payment);
					System.out.println("Your weekly payment for your " + payment.getBike().getColor()
					+ " " + payment.getBike().getBrand() + " " + payment.getBike().getModel()
					+ " is " + payment.getTotalCost()/12);
					paymentIds.add(payment.getPaymentId());
					
				}
				System.out.println("Would you like to make a payment?"
						+ "\n1. Yes"
						+ "\nOther. No");
				input = scan.nextLine();
				if("1".equals(input)) {
					Integer flag = 0;
					Integer payId = 0;
					while(flag == 0) {
						System.out.println("Enter the id of the payment: ");
						input = scan.nextLine();
						if(paymentIds.contains(Integer.valueOf(input))){
							flag = 1;
							payId= Integer.valueOf(input);
						}
						else {
							System.out.println("Sorry, that's not one of your payment ids."
									+ " Let's try again.");
						}
					}
					Payment payment = paymentServ.getPaymentById(payId);
					System.out.println("Your weekly payment is " + payment.getTotalCost()/12
							+ "\n How much would you like to pay?");
					flag = 0;
					Double pay = 0.0;
					while(flag == 0) {
						try{
							input = scan.nextLine();
							pay = Double.valueOf(input);
							if(pay > 0) {
								flag = 1;
							}else {
								System.out.println("Payments cannot be equal to or less than zero.");
							}	
						}catch(NumberFormatException e) {
							System.out.println("Incorrect format.");
						}
					}
					payment.setPaymentId(payId);
					pay = payment.getAmountPaid() + pay;
					payment.setAmountPaid(pay);
					paymentServ.updatePayment(payment);
					System.out.println("Thank you. The amount you've paid on your bike is "
					+ payment.getAmountPaid() + " and the amount you have left is "
					+ (payment.getTotalCost()-payment.getAmountPaid()));
					System.out.println("What would you like to do?"
							+ "\n1. View payments"
							+ "\nOther. Go back to menu");
					input = scan.nextLine();
					if("1".equals(input)) {
						viewMyPayments(loggedInUser);
						return loggedInUser;
					}
					else {
						return loggedInUser;
					}
				}
				else {
					return loggedInUser;
				}
			}
		}
		
		private static User manageBikes(User loggedInUser) {
			System.out.println("What would you like to do?"
					+ "\n1. Add a bike to the shop."
					+ "\n2. Remove a bike from the shop."
					+ "\nOther. Go back to main menu.");
			String input = scan.nextLine();
			switch(input) {
			case "1":
				Bike bike = new Bike();
				Set<Integer> selections = new HashSet<>();
				selections.add(1);
				selections.add(2);
				selections.add(3);
				Integer flag = 0;
				Integer selection = 0;
				while(flag == 0) {
					System.out.println("Which model would you like the bike to be?"
							+ "\n1. Edge"
							+ "\n2. BMX"
							+ "\n3. Mountain");
					input = scan.nextLine();
					if(selections.contains(Integer.valueOf(input))){
						selection = Integer.valueOf(input);
						switch(selection) {
						case 1:
							bike.setModel("Edge");
							break;
						case 2:
					
							bike.setModel("BMX");
							break;
						case 3:
							bike.setModel("Mountain");
							break;
						}
						flag = 1;
					}
					else {
						System.out.println("Sorry, that's not a valid selection."
								+ " Let's try again.");
					}
				}
				flag = 0;
				while(flag == 0) {
					System.out.println("Which brand would you like the bike to be?"
							+ "\n1. Schwinn"
							+ "\n2. Mongoose"
							+ "\n3. Nischi");
					input = scan.nextLine();
					if(selections.contains(Integer.valueOf(input))){
						selection = Integer.valueOf(input);
						switch(selection) {
						case 1:
							bike.setBrand("Schwinn");
							break;
						case 2:
							bike.setBrand("Mongoose");
							break;
						case 3:
							bike.setBrand("Nischi");
							break;
						}
						flag = 1;
					}
					else {
						System.out.println("Sorry, that's not a valid selection."
								+ " Let's try again.");
					}
				}
				flag = 0;
				while(flag == 0) {
					System.out.println("Which color would you like the bike to be?"
							+ "\n1. white"
							+ "\n2. red"
							+ "\n3. black");
					input = scan.nextLine();
					if(selections.contains(Integer.valueOf(input))){
						selection = Integer.valueOf(input);
						switch(selection) {
						case 1:
							bike.setColor("white");
							break;
						case 2:
							bike.setColor("red");
							break;
						case 3:
							bike.setColor("bike");
							break;
						}
						flag = 1;
					}
					else {
						System.out.println("Sorry, that's not a valid selection."
								+ " Let's try again.");
					}
				}
				Status status = new Status();
				status.setId(1);
				status.setName("Available");
				bike.setStatus(status);
				bikeServ.addBike(bike);
				//log.debug(bike);
				System.out.println("Awesome! You added a new bike to the shop."
						+ "\nWould you like to add/remove another bike or return to the main menu?"
						+ "\n1. Manage bikes"
						+ "\nOther. Return to main menu");
				input = scan.nextLine();
				if("1".equals(input)) {
					manageBikes(loggedInUser);
					return loggedInUser;
				}else {
					return loggedInUser;
				}
			case "2":
				Set<Bike> availableBikes = bikeServ.getAvailableBikes();
				Set<Integer> bikeIds = new HashSet<>();
				System.out.println("Here is a list of bikes that are for sale.");
				for(Bike bke : availableBikes) {
					System.out.println(bke);
					bikeIds.add(bke.getBikeId());
				}
				flag = 0;
				Integer bikeId= 0;
				while(flag == 0) {
					System.out.println("Enter the ID of the bike that you want to remove.");
					input = scan.nextLine();
					if(bikeIds.contains(Integer.valueOf(input))){
						bikeId = Integer.valueOf(input);
						flag = 1;
					}
					else {
						System.out.println("Sorry, that's not a valid selection."
								+ " Let's try again.");
					}
				}
				//log.debug(bikeId);
				bike = bikeServ.getBikeById(bikeId);
				bikeServ.removeBike(bike);
				System.out.println("Alrighty, a bike has been removed from the shop."
						+ "\nWould you like to add/remove another bike or return to the main menu?"
						+ "\n1. Manage bikes"
						+ "\nOther. Return to main menu");
				input = scan.nextLine();
				if("1".equals(input)) {
					manageBikes(loggedInUser);
					return loggedInUser;
				}else {
					return loggedInUser;
				}
			default:
				return loggedInUser;
			}
		}
		
		private static User viewAllPayments(User loggedInUser) {
			Set<Payment> allPayments = paymentServ.getAllPayments();
			System.out.println("This is a list of all the payments: ");
			for(Payment payment : allPayments) {
				System.out.println(payment);
			}
			System.out.println("Press any key to go back to the main menu.");
			String input = scan.nextLine();
			return loggedInUser;
		}
		
		private static User manageOffers(User loggedInUser) {
			Set<Bike> availableBikes = bikeServ.getAvailableBikes();
			Set<Integer> offerIds = new HashSet<>();
			System.out.println("Here are a list of the available bikes with their offers.");
			for(Bike bike : availableBikes) {
				Set<Offer> offers = bike.getOffers();
				System.out.println(bike.toStringBike());
				for(Offer offer : offers) {
					offerIds.add(offer.getOfferId());
					System.out.println(offer);
				}
			}
			System.out.println("Would you like to accept or reject an offer, or go back to the menu?"
					+ "\n1. Accept"
					+ "\n2. Reject"
					+ "\n.Other Go back to the menu");
			String input = scan.nextLine();
			if("1".equals(input)) {
				Integer flag = 0;
				Integer offerId = 0;
				while(flag == 0) {
					System.out.println("Enter the ID of the offer that you want to accept.");
					input = scan.nextLine();
					if(offerIds.contains(Integer.valueOf(input))){
						offerId = Integer.valueOf(input);
						flag = 1;
					}
					else {
						System.out.println("Sorry, that's not a valid offer ID."
								+ " Let's try again.");
					}
				}
				AcceptOffer(offerId);
				System.out.println("Someone is going to be really happy to know that they have a new bike!"
						+ "\nWhat would you like to do now?"
						+ "\n1. Manage offers"
						+ "\nOther. Go back to the menu");

			}else if("2".equals(input)){
				Integer flag = 0;
				Integer offerId = 0;
				while(flag == 0) {
					System.out.println("Enter the ID of the offer that you want to reject.");
					input = scan.nextLine();
					if(offerIds.contains(Integer.valueOf(input))){
						offerId = Integer.valueOf(input);
						flag = 1;
					}
					else {
						System.out.println("Sorry, that's not a valid offer ID."
								+ " Let's try again.");
					}
				}
				Offer offer = new Offer();
				offer.setOfferId(offerId);
				if(offerServ.deleteOffer(offer)) {
					System.out.println("The offer was deleted. Would you like to manage more offers"
							+ " or go back to the menu?"
							+ "\n1. Manage offers"
							+ "\nOther. Go back to the menu");
					input = scan.nextLine();
					if("1".equals(input)) {
						manageOffers(loggedInUser);
						return loggedInUser;
					}else {
						return loggedInUser;
					}
				}else {
					//log.debug("Offer" + offer +" failed to delete");
					System.out.println("Sorry, your offer couldn't be deleted. Try again later.");
				}
				
			}else {
				return loggedInUser;
			}
			return loggedInUser;
		}
		
		static void AcceptOffer(Integer offerId){
			Offer offer = offerServ.getOfferById(offerId);
			Integer userId = offerServ.getUserId(offer);
			User user = new User();
			user.setUserId(userId);
			Integer bikeId = offerServ.getBikeId(offer);
			Bike bike = bikeServ.getBikeById(bikeId);
			paymentServ.createPayment(offer, bike, user);
			Status status = new Status();
			status.setId(2);
			status.setName("Owned");
			bike.setStatus(status);
			bikeServ.updateBike(bike);
			offerServ.acceptOffer(offer, bike);
		}
	
}