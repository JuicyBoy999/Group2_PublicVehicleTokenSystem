/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.VehicleData;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class VehicleDao {
    MySqlConnection mysql = new MySqlConnection();
    
    // Return ArrayList of users with role = 'driver'
    public ArrayList<String> getDrivers() {
        ArrayList<String> drivers = new ArrayList<>();
        Connection conn = mysql.openConnection();
        String sql = "Select name from users where role = 'driver'";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    // Iterate and add names to list
                    drivers.add(rs.getString("name"));
                }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
        
        return drivers;
    }
    
    // Map driver name to driver ID
    public int getDriverIdByName(String driverName) {
        Connection conn = mysql.openConnection();
        String sql = "Select user_id from users where name = ?";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, driverName);
            ResultSet rs = pstm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        catch(SQLException e) {
            System.out.println("Error getting driver ID: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }
        
        return -1;  // not found
    }
    
    public void addVehicle(VehicleData vehicle) {
        Connection conn = mysql.openConnection();
        String sql = "Insert into vehicles (number, type, seat, driver) values (?,?,?,?)";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            // Sends values from variables to database
            pstm.setString(1, vehicle.getVehicleNumber());
            pstm.setString(2, vehicle.getVehicleType());
            pstm.setInt(3, vehicle.getSeatCount());
            pstm.setInt(4, vehicle.getDriverId());    // Driver is stored as ID in database
            
            pstm.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
    }
    
    public boolean check(VehicleData vehicle) { // Check if unique or not
        Connection conn = mysql.openConnection();
        String sql = "Select * from vehicles where number = ?";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, vehicle.getVehicleNumber());
            ResultSet result = pstm.executeQuery();
            return result.next();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
        
        return false;
    }
    
    // Return ArrayList of all vehicles
    public ArrayList<VehicleData> getAllVehicles() {
        ArrayList<VehicleData> vehiclelist = new ArrayList<>();
        
        try {
            Connection conn = mysql.openConnection();
            String sql = "Select v.number, v.type, v.seat, u.user_id, u.name from vehicles v " +
                         "join users u on v.driver = u.user_id";
            
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();
                  
            while (rs.next()) {
                    vehiclelist.add(new VehicleData (
                        rs.getString("number"),
                        rs.getString("type"),
                        rs.getInt("seat"),
                        rs.getInt("user_id"),
                        rs.getString("name")
                    ));
                }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        
        return vehiclelist;
    }
}