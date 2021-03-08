package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.beans.Bike;
import com.revature.beans.Offer;
import com.revature.beans.Payment;
import com.revature.beans.Role;
import com.revature.beans.Status;
import com.revature.beans.User;
import com.revature.exceptions.NonUniqueUsernameException;
//import com.revature.utilities.ConnectionUtil;
import com.revature.utilities.DAOUtilities;

public class UserPostgres implements UserDAO {
	private Connection connection = null;
	PreparedStatement stmt = null;
	//private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	private Logger log = Logger.getLogger(UserPostgres.class);


	@Override
	public boolean add(User u) throws NonUniqueUsernameException {
		try {
			connection = DAOUtilities.getConnection();
			connection.setAutoCommit(false);
			String sql = "insert into registered_users(id,"
					+ " name, passwd, rle) values(default, ?, ?, ?)";
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, u.getUsername());
			stmt.setString(2, u.getPassword());
			stmt.setInt(3, u.getRole().getId());
			
			if(stmt.executeUpdate() != 0) {
				//connection.commit();
				sql = "select max(id) from registered_users";
				stmt = connection.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				
				Integer id;
				if(rs.next()) {
					id = rs.getInt("max");
					u.setUserId(id);
				}
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
	public User getById(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByUsername(String username){
		User user = new User();
		try {
			//first we'll get the user's id, password, and role
			connection = DAOUtilities.getConnection();
			connection.setAutoCommit(false);	// Get our database connection from the manager
			String sql = "select * from "
					+"(select registered_users.id , registered_users.name, registered_users.passwd, " 
							+"registered_users.rle as role_id, user_role.\"name\" as role_name from registered_users "
							+"join user_role on rle = user_role.id) as userrole "
							+"where userrole.name = ?";
					//+ " where userrole.id = ? ";			// Our SQL query
			// "\"john\"" Example of using quotations in a string.
			PreparedStatement stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();			// Queries the database

			// So long as the ResultSet actually contains results...
			while (rs.next()) {
				// We need to populate a Book object with info for each row from our query result

				user.setUserId(rs.getInt("id"));
				user.setUsername(rs.getString("name"));
				user.setPassword(rs.getString("passwd"));
				Role role = new Role();
				role.setId(rs.getInt("role_id"));
				role.setName(rs.getString("role_name"));
				user.setRole(role);
				//log.debug(user.toString());
			}
			
			rs.close();
			
			//next we'll grab the user's payments
			sql = "select * from\n"
					+ "(select user_id, pay_id, total, paid, bike_id, model, brand, color, status_id, status.\"name\" as status_name from\n"
					+ "(select user_id, pay_id, total, paid, bike_id, model, brand, color, status_id from\n"
					+ "	(select payments.id as pay_id, total, paid, payments_log.user_id, bike_id from payments join payments_log\n"
					+ "	on payments.id = payments_log.id) as userpayments\n"
					+ "	join bikes on userpayments.bike_id = bikes.id) as bike_payments\n"
					+ "	join status on status_id = status.id) as fullbike_payments\n"
					+ "where fullbike_payments.user_id = ?";

			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			if(user.getUserId() == null) {
				return null;
			}
			stmt.setInt(1, user.getUserId());
			rs = stmt.executeQuery();			// Queries the database
			Set<Payment> payments = new HashSet<>();
			// So long as the ResultSet actually contains results...
			while (rs.next()) {
				
				Payment payment = new Payment();
				payment.setPaymentId(rs.getInt("pay_id"));
				payment.setTotalCost(rs.getDouble("total"));
				payment.setAmountPaid(rs.getDouble("paid"));
				Bike bike = new Bike();
				bike.setBikeId(rs.getInt("bike_id"));
				bike.setModel(rs.getString("model"));
				bike.setBrand(rs.getString("brand"));
				bike.setColor(rs.getString("color"));
				Status status = new Status();
				status.setId(rs.getInt("status_id"));
				status.setName(rs.getString("status_name"));
				bike.setStatus(status);
				payment.setBike(bike);
				payments.add(payment);
				
			}
			user.setPayments(payments);
			//log.debug(payments.toString());
			rs.close();
			
			//we'll gather up a list of the user's bikes from their payments
			Set<Bike> bikes = new HashSet<>();
			for(Payment payment : payments) {
				bikes.add(payment.getBike());
			}
			user.setBikes(bikes);
			//log.debug(bikes.toString());
			
			sql = "select * from\n"
					+ "(select offer_id, bid, user_id, name from\n"
					+ "(select offers_log.id as offer_id, user_id, registered_users.name from\n"
					+ "offers_log join registered_users on user_id = registered_users.id) as offer_log_user\n"
					+ "join offers on offer_id = offers.id) as user_offers\n"
					+ "where user_id = ?";
			
			stmt = connection.prepareStatement(sql);
			stmt.setInt(1, user.getUserId());
			
			rs = stmt.executeQuery();
			
			Set<Offer> offers = new HashSet<>();
			
			while (rs.next()) {
				
				Offer offer = new Offer();
				offer.setOfferId(rs.getInt("offer_id"));
				offer.setBid(rs.getDouble("bid"));
				offers.add(offer);
				
			}
			user.setOffers(offers);
			//log.debug(offers);
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		// return the list of Book objects populated by the DB.
		return user;
	}

	@Override
	public Set<User> getAll() {
		return null;
	}

	@Override
	public boolean update(User u) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(User u) {
		// TODO Auto-generated method stub
		return false;
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
