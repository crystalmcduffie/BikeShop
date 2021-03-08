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
import com.revature.beans.User;
import com.revature.utilities.DAOUtilities;

public class OfferPostgres implements OfferDAO {

	Connection connection = null;	// Our connection to the database
	PreparedStatement stmt = null;	// We use prepared statements to help protect against SQL injection
	private Logger log = Logger.getLogger(OfferPostgres.class);
	/*------------------------------------------------------------------------------------------------*/
	
	@Override
	public boolean update(Offer o) {
		return false;
	}

	@Override
	public boolean delete(Offer o) {
		//delete from offers_log table then delete from offers
		try {
			connection = DAOUtilities.getConnection();
			connection.setAutoCommit(false);
			String sql = "delete from offers_log where id = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setInt(1, o.getOfferId());
			
			if(stmt.executeUpdate() !=0 ) {
				sql = "delete from offers where id = ?";
				stmt = connection.prepareStatement(sql);
				
				stmt.setInt(1, o.getOfferId());
				
				if(stmt.executeUpdate()!=0) {
					connection.commit();
					return true;
				}
				else {
					return false;
				}
			}else {
				return false;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			closeResources();
		}
		
	}

	public boolean add(Offer o) {
		
		try {
			connection = DAOUtilities.getConnection();	// Get our database connection from the manager
			String sql = "insert into offers"
					+ "(id, bid)"
					+"VALUES (default, ?)";			// Our SQL query

			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			
			// We set ?'s before we execute
			stmt.setDouble(1, o.getBid());
	
			if(stmt.executeUpdate() != 0)
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// We need to make sure our statements and connections are closed, 
			// or else we could wind up with a memory leak
			closeResources();
		}
	}
	
	@Override
	public Set<Offer> getAll() {
		
		return null;
	}

	@Override
	public Offer getById(Integer offerId) {
		Offer offer = new Offer();
		try {
			connection = DAOUtilities.getConnection();
			//connection.setAutoCommit(false);
			String sql = "select * from offers where id = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setInt(1, offerId);
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				offer.setOfferId(rs.getInt("id"));
				offer.setBid(rs.getDouble("bid"));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			closeResources();
		}
		return offer;
	}

	@Override
	public boolean makeOffer(Offer o, Bike b, User u) {
		try {
			connection = DAOUtilities.getConnection();	// Get our database connection from the manager
			connection.setAutoCommit(false);
			String sql = "insert into offers"
					+ "(id, bid)"
					+"VALUES (default, ?)";			// Our SQL query
			stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
			
			stmt.setDouble(1, o.getBid());
			
			if(stmt.executeUpdate() != 0) {
				sql = "select max(id) from offers where bid = ?";
				stmt = connection.prepareStatement(sql);
				stmt.setDouble(1, o.getBid());
				ResultSet rs = stmt.executeQuery();
				
				Integer id;
				if(rs.next()) {
					id = rs.getInt("max");
					
					sql = "insert into offers_log(id, user_id, bike_id)"
							+ " values (?,?,?)";
					stmt = connection.prepareStatement(sql);	// Creates the prepared statement from the query
					
					stmt.setInt(1, id);
					stmt.setInt(2, u.getUserId());
					stmt.setInt(3, b.getBikeId());
					
					if(stmt.executeUpdate() != 0) {
						connection.commit();
						return true;
					}
					else {
						connection.rollback();
					}
				}	
				else {
					connection.rollback();
				}
			}
			else {
				connection.rollback();
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			// We need to make sure our statements and connections are closed, 
			// or else we could wind up with a memory leak
			closeResources();
		}
		return false;
	}

	@Override
	public Integer getUserId(Offer o) {
		Integer userId = 0;
		try {
			connection = DAOUtilities.getConnection();
			//connection.setAutoCommit(false);
			String sql = "select * from\n"
					+ "(select offers.id as offer_id, bid, user_id, bike_id from offers join\n"
					+ "offers_log on offers.id = offers_log.id) as offer_ids\n"
					+ "where offer_id = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setInt(1, o.getOfferId());
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				userId = rs.getInt("user_id");
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			closeResources();
		}
		return userId;
	}

	@Override
	public Integer getBikeId(Offer o) {
		Integer bikeId = 0;
		try {
			connection = DAOUtilities.getConnection();
			//connection.setAutoCommit(false);
			String sql = "select * from\n"
					+ "(select offers.id as offer_id, bid, user_id, bike_id from offers join\n"
					+ "offers_log on offers.id = offers_log.id) as offer_ids\n"
					+ "where offer_id = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setInt(1, o.getOfferId());
			
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				bikeId = rs.getInt("bike_id");
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally {
			closeResources();
		}
		return bikeId;
	}

	@Override
	public boolean acceptOffer(Offer o, Bike b) {
		try {
			connection = DAOUtilities.getConnection();
			connection.setAutoCommit(false);
			//get all offers for the bike
			String sql = "select * from offers_log where bike_id = ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setInt(1, b.getBikeId());
			
			ResultSet rs = stmt.executeQuery();
			Set<Integer> offerIds = new HashSet<>();
			while(rs.next()) {
				Integer id = rs.getInt("id");
				offerIds.add(id);
			}
			//delete those offers
			//sql = "delete from offers where id = ?";
			//stmt = connection.prepareStatement(sql);
			int count = 1;
			int flag = 0;
			for(Integer id : offerIds) {
				Offer offer = new Offer();
				offer.setOfferId(id);
				if(delete(offer) == false) {
					flag = 1;
					connection.rollback();
				}
//				stmt.setInt(1, id);
//				if(stmt.executeUpdate() == 0) {
//					for(int i = 0; i<count; i++) {
//						flag = 1;
//						connection.rollback();
//					}
//				}
			}
			if(flag == 0) {
				connection.commit();
				return true;
			}else {
				return false;
			}
			
		}catch(SQLException e) {
			return false;
		}finally {
			closeResources();
		}
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
