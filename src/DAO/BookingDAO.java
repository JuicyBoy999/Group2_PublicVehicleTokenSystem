/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.Booking;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nitro V 16
 */
public class BookingDAO {
    MySqlConnection mysql = new MySqlConnection(); 
    
    public boolean createBooking(Booking booking) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO bookings (user_id, trip_id, number_of_seats, total_fare, status, booking_token, booked_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getTripId());
            stmt.setInt(3, booking.getNumberOfSeats());
            stmt.setDouble(4, booking.getTotalFare());
            stmt.setString(5, booking.getStatus());
            stmt.setString(6, booking.getBookingToken());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            mysql.closeConnection(conn);
        }
        return false;
    }
    
    public List<Booking> getUserBookings(int userId) {
        List<Booking> bookings = new ArrayList<>();
        Connection conn = mysql.openConnection();
        
        String sql = "SELECT b.*, r.origin, r.destination " +
                     "FROM bookings b " +
                     "JOIN trips t ON b.trip_id = t.trip_id " +
                     "JOIN routes r ON t.route = r.route_id " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.booked_at DESC";
                     
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setTripId(rs.getInt("trip_id"));
                booking.setNumberOfSeats(rs.getInt("number_of_seats"));
                booking.setTotalFare(rs.getDouble("total_fare"));
                booking.setStatus(rs.getString("status"));
                booking.setBookingToken(rs.getString("booking_token"));
                
                booking.setOrigin(rs.getString("origin"));
                booking.setDestination(rs.getString("destination"));
                
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysql.closeConnection(conn);
        }
        
        return bookings;
    }
    
    // Filter
    public List<Booking> getUserBookingsByStatus (int userID, String status) {
        List<Booking> bookings = new ArrayList<>();
        Connection conn = mysql.openConnection();
        
        String sql = "Select b.*, r.origin, r.destination " +
                     "from bookings b " +
                     "join trips t on b.trip_id = t.trip_id " +
                     "join routes r on t.route = r.route_id " +
                     "where b.user_id = ? and b.status = ? " +
                     "order by b.booked_at desc";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, userID);
            pstm.setString(2, status);
            
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Booking booking = new Booking();
                
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setTripId(rs.getInt("trip_id"));
                booking.setNumberOfSeats(rs.getInt("number_of_seats"));
                booking.setTotalFare(rs.getDouble("total_fare"));
                booking.setStatus(rs.getString("status"));
                booking.setBookingToken(rs.getString("booking_token"));               
                booking.setOrigin(rs.getString("origin"));
                booking.setDestination(rs.getString("destination"));
                
                bookings.add(booking);                
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            mysql.closeConnection(conn);
        }
        return bookings;
    }
}