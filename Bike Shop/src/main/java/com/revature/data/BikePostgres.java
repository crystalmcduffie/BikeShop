package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Status;
import com.revature.controller.BikeShopController;
import com.revature.utilities.DAOUtilities;

public class BikePostgres implements BikeDAO {
	
	Connection connection = null;	// Our connection to the database
	PreparedStatement stmt = null;	// We use prepared statements to help protect against SQL injection
	
	/*------------------------------------------------------------------------------------------------*/
	private static Logger log = Logger.getLogger(BikePostgres.class);
	
	public Set<Bike> getAll() {
		
		return null;

	}
	

	public boolean add(Bike b) {
		try {
			connection = DAOUtilities.getConnection();
			connection.setAutoCommit(false);
			String sql = "insert into bikes(id, model, brand, color, status_id) "
					+ "values(default, ?, ?, ?, ?)";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, b.getModel());
			stmt.setString(2, b.getBrand());
			stmt.setString(3, b.getColor());
			stmt.setInt(4, b.getStatus().getId());
			
			if(stmt.executeUpdate() != 0) {
				connection.commit();
				return true;
			}else {
				connection.rollback();
				return false;
			}
		}catch(SQLException e) {
			//log.debug("Add was unsuccessful");
			e.printStackTrace();
			return false;
		}finally {
			closeResources();
		}
		
	}

	public Bike getById(Integer id) {
		Bike bike = null;
		
		try {
			connection = DAOUtilities.getConnection();
			String sql = "select * from\n"
					+ "(select bike_id, model, brand, color, status_id, status.\"name\" as status_name, offer_id, bid from\n"
					+ "(select bike_id, model, brand, color, status_id, offer_id, bid from \n"
					+ "(select bikes.id as bike_id, model, brand, color, status_id, offers_log.id as offer_id from bikes left join\n"
					+ "offers_log on bikes.id = bike_id) as bike_offer_log\n"
					+ "left join offers on offer_id = offers.id) as bike_offers\n"
					+ " join status on status_id = status.id) as full_bike\n"
					+ "where bike_id = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			bike = new Bike();
			Status status = new Status();
			Set<Offer> offers = new HashSet<>();
			Integer count = 0;
			while(rs.next()) {
				if(count == 0) {
					bike.setBikeId(rs.getInt("bike_id"));
					bike.setModel(rs.getString("model"));
					bike.setBrand(rs.getString("brand"));
					bike.setColor(rs.getString("color"));
					status.setId(rs.getInt("status_id"));
					status.setName(rs.getString("status_name"));
					bike.setStatus(status);
					Offer offer = new Offer();
					offer.setOfferId(rs.getInt("offer_id"));
					offer.setBid(rs.getDouble("bid"));
					offers.add(offer);
					count++;
				}
				else {
					Offer offer = new Offer();
					offer.setOfferId(rs.getInt("offer_id"));
					offer.setBid(rs.getDouble("bid"));
					offers.add(offer);
				}

			}
			bike.setOffers(offers);
			
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeResources();
		}
		
		return bike;
	}


	@Override
	public Set<Bike> getAvailableBikes() {

		Set<Bike> bikes = new HashSet<>();
		
		try {
			connection = DAOUtilities.getConnection();	// Get our database connection from the manager
			String sql = "select * from\n"
					+ "(select bike_offers.id, model, brand, color, status_id, bike_offers.\"name\" as status_name, \n"
					+ "bike_offers.offer_id as offer_id, user_id, bid from \n"
					+ "(select bikes_status.id, model, brand, color, status_id, bikes_status.\"name\", offers_log.id\n"
					+ "as offer_id, offers_log.user_id from\n"
					+ "(select bikes.id, model, brand, color, status_id, Status.\"name\" from bikes join Status on\n"
					+ "status_id = Status.id) as bikes_status left join offers_log on bikes_status.id = offers_log.bike_id) as bike_offers\n"
					+ "left join offers on offer_id = offers.id) as bike_offers\n"
					+ "where status_name = 'Available'";			// Our SQL query
			// "\"john\"" Example of using quotations in a string.
			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			
			ResultSet rs = stmt.executeQuery();			// Queries the database

			// So long as the ResultSet actually contains results...
			while (rs.next()) {
				Bike bike = new Bike();
				Set<Offer >offers = new HashSet<>();
				bike.setBikeId(rs.getInt("id"));
				bike.setModel(rs.getString("model"));
				bike.setBrand(rs.getString("brand"));
				bike.setColor(rs.getString("color"));
				Status status = new Status();
				status.setId(rs.getInt("status_id"));
				status.setName(rs.getString("status_name"));
				bike.setStatus(status);
				Offer offer = new Offer();
				offer.setOfferId(rs.getInt("offer_id"));
				offer.setBid(rs.getDouble("bid"));
				offers.add(offer);
				bike.setOffers(offers);
				bikes.add(bike);		
				
				//log.debug(bike);
			}
			
			rs.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed, 
			// or else we could wind up with a memory leak
			closeResources();
		}
		
		
//		Set<Bike> bikes2 = new HashSet<>();
//		Integer flag = 0;
//		while(flag == 0) {
//			Iterator<Bike> bItr = bikes.iterator();
//			Bike tempBike = bItr.next();
//			bikes.remove(tempBike);
//			flag = 1;
//			for(Bike bike : bikes) {
//				if(bike.getBikeId() != 0) {
//				flag = 0;
//				if(bike.getBikeId()== tempBike.getBikeId()) {
//						Set<Offer> bikeOffers = bike.getOffers();
//						Set<Offer> tempBikeOffers = tempBike.getOffers();
//						if(bikeOffers.isEmpty() == false) {
//							for(Offer offer :bikeOffers) {
//								tempBikeOffers.add(offer);
//								tempBike.setOffers(tempBikeOffers);
//								bike.setBikeId(0);
//							}
//						}
//					}
//				}
//			}
//			if(tempBike.getBikeId()!=0) {
//				bikes2.add(tempBike);
//			}
//		}
//		
//		return bikes2;
		return bikes;

	}

	@Override
	public Set<Bike> viewOwnedBikes(Integer userId) {

		Set<Bike> bikes = new HashSet<>();
		
		try {
			connection = DAOUtilities.getConnection();	// Get our database connection from the manager
			connection.setAutoCommit(false);
			String sql = "select * from\n"
					+ "(select user_id, users_bikes_payments.name, bike_id, model, brand, color, status_id, status.name as status_name,\n"
					+ "pay_id, total, paid from\n"
					+ "(select user_id, name, bike_id, model, brand, color, status_id, pay_id, total, paid from\n"
					+ "(select user_id, name, pay_id, bike_id, model, brand, color, status_id from \n"
					+ "(select registered_users.id as user_id, name, payments_log.id as pay_id, bike_id from\n"
					+ "registered_users join payments_log on registered_users.id = payments_log.user_id)\n"
					+ " as user_payment_log join bikes on bike_id = bikes.id) as user_bikes_pay_log\n"
					+ "join payments on pay_id = payments.id) as users_bikes_payments\n"
					+ "join status on status_id = status.id) user_fullbikes_payments\n"
					+ "where user_id = ?";			// Our SQL query

			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			
			stmt.setInt(1, userId);
			
			ResultSet rs = stmt.executeQuery();			// Queries the database

			//An owned bike shouldn't have offers
			while (rs.next()) {
				Bike bike = new Bike();
				bike.setBikeId(rs.getInt("bike_id"));
				bike.setModel(rs.getString("urname"));
				bike.setBrand(rs.getString("brand"));
				bike.setColor(rs.getString("color"));
				Status status = new Status();
				status.setId(rs.getInt("status_id"));
				status.setName(rs.getString("status_name"));
				bike.setStatus(status);
				bikes.add(bike);
			}
			
			rs.close();
			//connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed, 
			// or else we could wind up with a memory leak
			closeResources();
		}
		
		// return the list of Book objects populated by the DB.
		return bikes;

	}

	@Override
	public boolean update(Bike b) {
		try {
			connection = DAOUtilities.getConnection();
			connection.setAutoCommit(false);
			String sql = "update bikes set model = ?, brand = ?, color = ?, status_id = ? where id = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, b.getModel());
			stmt.setString(2, b.getBrand());
			stmt.setString(3, b.getColor());
			stmt.setInt(4, b.getStatus().getId());
			stmt.setInt(5, b.getBikeId());
			
			if(stmt.executeUpdate() != 0) {
				connection.commit();
				return true;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			closeResources();
		}
		return false;
		
	}

	@Override
	public boolean delete(Bike b) {
		try {
			connection = DAOUtilities.getConnection();
			connection.setAutoCommit(false);
			String sql = "delete from bikes where id = ?";
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, b.getBikeId());
			
			if(stmt.executeUpdate() != 0) {
				connection.commit();
				return true;
			}else {
				connection.rollback();
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			closeResources();
		}
		
	}

	@Override
	public Set<Offer> getByBikeId(Integer bikeId) {
		Set<Offer> offers = new HashSet<>();
		try {
			connection = DAOUtilities.getConnection();
			String sql = "select * from\n"
					+ "(select bike_offers.id, model, brand, color, status_id, bike_offers.\"name\" as status_name, \n"
					+ "bike_offers.offer_id as offer_id, user_id, bid from \n"
					+ "(select bikes_status.id, model, brand, color, status_id, bikes_status.\"name\", offers_log.id\n"
					+ "as offer_id, offers_log.user_id from\n"
					+ "(select bikes.id, model, brand, color, status_id, Status.\"name\" from bikes join Status on\n"
					+ "status_id = Status.id) as bikes_status join offers_log on bikes_status.id = offers_log.bike_id) as bike_offers\n"
					+ "join offers on offer_id = offers.id) as bike_offers\n"
					+ "where bike_offers.id = ?";
			
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, bikeId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Offer offer = new Offer();
				offer.setOfferId(rs.getInt("offer_id"));
				offer.setBid(rs.getDouble("bid"));
				offers.add(offer);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return offers;
	}

	private void closeResources() {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			System.out.println("Could not close statement!");
			e.printStackTrace();
		}
		
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close connection!");
			e.printStackTrace();
		}
	}
	
}
