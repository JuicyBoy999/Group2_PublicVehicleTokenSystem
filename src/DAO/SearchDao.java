/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.SearchData;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class SearchDao {
    MySqlConnection mysql = new MySqlConnection();
    
    // Return ArrayList of all trips matching searched place and optional vehicle type
    public ArrayList<SearchData> searchTrips(String destination, String vehicleType) {
        ArrayList<SearchData> result = new ArrayList<>();
        Connection conn = mysql.openConnection();

        String sql = "Select t.trip_id, v.number as vehicle_number, v.type as vehicle_type, "
                   + "r.origin, r.destination, t.departure, t.arrival, r.fare, v.seat - IFNULL(b.booked_seats,0) as available_seats "
                   + "from trips t "
                   + "join vehicles v on t.vehicle = v.vehicle_id "
                   + "join routes r on t.route = r.route_id "
                   + "left join (select trip_id, sum(number_of_seats) as booked_seats from bookings group by trip_id) b "
                   + "on t.trip_id = b.trip_id "
                   + "where (r.destination like ? or r.origin like ?) "
                   + "and (v.seat - IFNULL(b.booked_seats, 0)) > 0 and t.status in ('Scheduled', 'Ongoing')";

        if (!vehicleType.equalsIgnoreCase("All")) {
            sql += "and v.type = ? ";
        }

        sql += "order by t.departure ASC";

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, "%" + destination + "%");
            pstm.setString(2, "%" + destination + "%");
            
            if (!vehicleType.equalsIgnoreCase("All")) {
                pstm.setString(3, vehicleType);
            }

            ResultSet rs = pstm.executeQuery();
            
            while (rs.next()) {
                SearchData trip = new SearchData(
                    rs.getInt("trip_id"),
                    rs.getString("vehicle_number"),
                    rs.getString("vehicle_type"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getTimestamp("departure").toString(),
                    rs.getTimestamp("arrival").toString(),
                    rs.getDouble("fare"),
                    rs.getInt("available_seats")
                );
                result.add(trip);
            }

        }
        catch (SQLException e) {
            System.out.println("Error fetching trips: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }

        return result;
    }

    // Fetch by trip_id (for booking)
    public SearchData getTripById(int tripId) {
        Connection conn = mysql.openConnection();
        String sql = "Select t.trip_id, v.number as vehicle_number, v.type as vehicle_type, "
                   + "r.origin, r.destination, t.departure, t.arrival, r.fare, v.seat - IFNULL(b.booked_seats,0) as available_seats "
                   + "from trips t "
                   + "join vehicles v on t.vehicle = v.vehicle_id "
                   + "join routes r on t.route = r.route_id "
                   + "left join (select trip_id, sum(number_of_seats) as booked_seats from bookings group by trip_id) b "
                   + "on t.trip_id = b.trip_id "
                   + "where t.trip_id = ? "
                   + "and (v.seat - IFNULL(b.booked_seats, 0)) > 0 and t.status IN ('Scheduled', 'Ongoing')";

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, tripId);
            ResultSet rs = pstm.executeQuery();
            
            if (rs.next()) {
                return new SearchData(
                    rs.getInt("trip_id"),
                    rs.getString("vehicle_number"),
                    rs.getString("vehicle_type"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getTimestamp("departure").toString(),
                    rs.getTimestamp("arrival").toString(),
                    rs.getDouble("fare"),
                    rs.getInt("available_seats")
                );
            }
        }
        catch (SQLException e) {
            System.out.println("Error fetching trip by ID: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }
        return null;
    }
    
    // Insert booking
    public boolean bookTrip(int tripId, int passengerId) {
        Connection conn = mysql.openConnection();
        String sql = "Insert into bookings (trip_id, user_id) values (?, ?)";

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, tripId);
            pstm.setInt(2, passengerId);

            int rows = pstm.executeUpdate();
            return rows > 0;
        }
        catch (SQLException e) {
            System.out.println("Booking error: " + e.getMessage());
            return false;
        }
        finally {
            mysql.closeConnection(conn);
        }
    }
    
    // Check number of available seats
    public int getAvailableSeats(int tripId) {
        Connection conn = mysql.openConnection();
        String sql = "Select v.seat - IFNULL(b.booked_seats,0) as available_seats "
                   + "from trips t "
                   + "join vehicles v on t.vehicle = v.vehicle_id "
                   + "left join (Select trip_id, sum(number_of_seats) as booked_seats from bookings group by trip_id) b "
                   + "on t.trip_id = b.trip_id "
                   + "where t.trip_id = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, tripId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("available_seats");
            }
        }
        catch (SQLException e) {
            System.out.println("Seat check error" + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }

    public void updateAvailableSeats(int tripId, int seatsBooked) {
        Connection conn = mysql.openConnection();
        String sql = "Update trips set available_seats = available_seats = ? where trip_id = ?";
        
        try (PreparedStatement pstm = conn. prepareStatement(sql)) {
            pstm.setInt(1, seatsBooked);
            pstm.setInt(2, tripId);
            pstm.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            mysql.closeConnection(conn);
        }
    }
}