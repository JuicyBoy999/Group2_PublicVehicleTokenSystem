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
    
    // Return ArrayList of all trips matching destination and optional vehicle type
    public ArrayList<SearchData> searchTrips(String destination, String vehicleType) {
        ArrayList<SearchData> result = new ArrayList<>();
        Connection conn = mysql.openConnection();

        String sql = "SELECT t.trip_id, v.number AS vehicle_number, v.type AS vehicle_type, "
                   + "r.origin, r.destination, t.departure, t.arrival, r.fare, v.seat - IFNULL(b.booked_seats,0) AS available_seats "
                   + "FROM trips t "
                   + "JOIN vehicles v ON t.vehicle = v.vehicle_id "
                   + "JOIN routes r ON t.route = r.route_id "
                   + "LEFT JOIN (SELECT trip_id, COUNT(*) AS booked_seats FROM bookings GROUP BY trip_id) b "
                   + "ON t.trip_id = b.trip_id "
                   + "WHERE (r.destination LIKE ? OR r.origin LIKE ?) "
                   + "AND (v.seat - IFNULL(b.booked_seats, 0)) > 0 AND t.status IN ('Scheduled', 'Ongoing')";

        if (!vehicleType.equalsIgnoreCase("All")) {
            sql += "AND v.type = ? ";
        }

        sql += "ORDER BY t.departure ASC";

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

    // Optional: fetch by trip_id (for booking)
    public SearchData getTripById(int tripId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT t.trip_id, v.number AS vehicle_number, v.type AS vehicle_type, "
                   + "r.origin, r.destination, t.departure, t.arrival, r.fare, v.seat - IFNULL(b.booked_seats,0) AS available_seats "
                   + "FROM trips t "
                   + "JOIN vehicles v ON t.vehicle = v.vehicle_id "
                   + "JOIN routes r ON t.route = r.route_id "
                   + "LEFT JOIN (SELECT trip_id, COUNT(*) AS booked_seats FROM bookings GROUP BY trip_id) b "
                   + "ON t.trip_id = b.trip_id "
                   + "WHERE t.trip_id = ? "
                   + "AND (v.seat - IFNULL(b.booked_seats, 0)) > 0 AND t.status IN ('Scheduled', 'Ongoing')";

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
}