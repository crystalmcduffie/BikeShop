package com.revature.data;

public class DAOFactory {
	    
   public static BikeDAO getBikeDAO() {
	        
	   //return new BikeCollections();
	   //return new BikeDAOImpl();
	   return new BikePostgres();
	   
	    }
	    
   public static OfferDAO getOfferDAO() {
	    	
	  //return new OfferCollections();
	   //return new OfferDAOImpl();
	   return new OfferPostgres();
	   
	    }
   public static UserDAO getUserDAO() {
	   
	  //return new UserCollections();
	   //return new UserDAOImpl();
	   return new UserPostgres();
	  
   }
   
   public static PaymentDAO getPaymentDAO() {
	  //return new PaymentCollections();
	   //return new PersonDAOImpl();
	  return new PaymentPostgres();
	   
   }
}
