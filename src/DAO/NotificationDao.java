/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.NotificationData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class NotificationDao {
    MySqlConnection mysql = new MySqlConnection();
    
    public ArrayList<NotificationData> getTrips() {
        ArrayList<NotificationData> trips = new ArrayList<>();
        Connection conn = mysql.openConnection();
        
        String sql = "Select t.trip_id, v.number, r.route_name, t.departure, t.arrival " +
                     "from trips t " +
                     "join vehicles v ON t.vehicle = v.vehicle_id " +
                     "join routes r ON t.route = r.route_id " +
                     "where t.status <> 'Cancelled'";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int tripId = rs.getInt("trip_id");
                String label = rs.getString("number") + " | " +
                               rs.getString("route_name") + " | " +
                               rs.getString("departure") + " → " +
                               rs.getString("arrival");
                trips.add(new NotificationData(tripId, label, "", ""));
            }
        }
        catch (Exception e) {
            System.out.println("Error loading trips for notifications: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }
        return trips;
    }

    // Save notification
    public void saveNotification(NotificationData notif) {
        Connection con = mysql.openConnection();
        String sql = "INSERT INTO notifications(trip_id, notification_type, message, created_at) " +
                     "VALUES (?, ?, ?, NOW())";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, notif.getTripId());
            ps.setString(2, notif.getType());
            ps.setString(3, notif.getMessage());
            
            ps.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("Error saving notification: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(con);
        }
    }

    // Get passenger emails for selected trip
    public ArrayList<String> getPassengerEmailsByTrip(int tripId) {
        ArrayList<String> emails = new ArrayList<>();
        Connection con = mysql.openConnection();

        String sql = "SELECT u.email FROM bookings b " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "WHERE b.trip_id = ? AND b.status = 'CONFIRMED'";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                emails.add(rs.getString("email"));
            }
        }
        catch (Exception e) {
            System.out.println("Error fetching passenger emails: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(con);
        }
        return emails;
    }
}