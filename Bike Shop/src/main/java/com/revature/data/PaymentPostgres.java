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
import com.revature.beans.Status;
import com.revature.beans.User;
import com.revature.utilities.DAOUtilities;

public class PaymentPostgres implements PaymentDAO {
	
	Connection connection = null;	// Our connection to the database
	PreparedStatement stmt = null;	// We use prepared statements to help protect against SQL injection
	private Logger log = Logger.getLogger(PaymentPostgres.class);
	/*------------------------------------------------------------------------------------------------*/
	@Override
	public boolean createPayment(Offer o, Bike b, User u) {
		//add to payments table, then add to payments_log
		try {
			connection = DAOUtilities.getConnection();
			connection.setAutoCommit(false);
			String sql = "insert into payments(id, total, paid) values(default, ?, 0)";
			stmt = connection.prepareStatement(sql);
			
			stmt.setDouble(1, o.getBid());
			//stmt.setDouble(2, 0.0);
			
			if(stmt.executeUpdate() != 0) {
				sql = "select max(id) from payments where total = ?";
				stmt = connection.prepareStatement(sql);
				stmt.setDouble(1, o.getBid());
				ResultSet rs = stmt.executeQuery();
				
				Integer id = 0;
				
				if(rs.next()) {
					id = rs.getInt("max");
					
					sql = "insert into payments_log(id, user_id, bike_id) values(?,?,?)";
					stmt = connection.prepareStatement(sql);
					
					stmt.setInt(1, id);
					stmt.setInt(2, u.getUserId());
					stmt.setInt(3, b.getBikeId());
					
					if(stmt.executeUpdate() != 0) {
						connection.commit();
						return true;
					}else {
						connection.rollback();
					}
				}else {
					connection.rollback();
				}
			}else {
				connection.rollback();
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally {
			closeResources();
		}
		return false;
	}
	
	@Override
	public Payment getById(Integer id) {
		Payment payment = new Payment();
		try{
		connection = DAOUtilities.getConnection();
		String sql = "select * from\n"
				+ "(select pay_id, total, paid, bike_id, model, brand, color, status_id, status.\"name\" as status_name from\n"
				+ "(select pay_id, total, paid, bike_id, model, brand, color, status_id from\n"
				+ "(select payments.id as pay_id, total, paid, bike_id from payments join payments_log\n"
				+ "on payments.id = payments_log.id) as pay\n"
				+ "join bikes on bike_id = bikes.id) as pay_bike\n"
				+ "join status on status_id = status.id) as fullpayments\n"
				+ "where pay_id = ?";
		
		
		stmt = connection.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next()) {
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
		}
		
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			closeResources();
		}
		return payment;
	}

	@Override
	public Set<Payment> getAll() {
		
		Set<Payment> payments = new HashSet<>();
		
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
					+ "join status on status_id = status.id) user_fullbikes_payments";			// Our SQL query
			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			ResultSet rs = stmt.executeQuery();			// Queries the database

			// So long as the ResultSet actually contains results...
			while (rs.next()) {
				Payment payment = new Payment();
				payment.setPaymentId(rs.getInt("pay_id"));
				payment.setTotalCost(rs.getDouble("total"));
				payment.setAmountPaid(rs.getDouble("paid"));
				Bike bike = new Bike();
				bike.setBikeId(rs.getInt("bike_id"));
				bike.setBrand(rs.getString("brand"));
				bike.setModel(rs.getString("model"));
				bike.setColor(rs.getString("color"));
				payment.setBike(bike);
				payments.add(payment);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// We need to make sure our statements and connections are closed, 
			// or else we could wind up with a memory leak
			closeResources();
		}
		
		// return the list of Book objects populated by the DB.
		return payments;
	}

	@Override
	public boolean update(Payment p) {
		
		try {
			connection = DAOUtilities.getConnection();
			connection.setAutoCommit(false);
			String sql = "update payments set paid = ? where payments.id = ? ";
			stmt = connection.prepareStatement(sql);
			
			stmt.setDouble(1, p.getAmountPaid());
			stmt.setInt(2, p.getPaymentId());
			
			if (stmt.executeUpdate() != 0) {
				//log.debug(stmt);
				connection.commit();
				return true;}
			else {
				//log.debug("Update failed to execute");
				return false;}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
	}

	@Override
	public boolean delete(Payment p) {
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
