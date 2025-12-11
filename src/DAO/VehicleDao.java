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
    
    public ArrayList<String> getDrivers() {
        // List of users with role = 'driver' is retrieved and return ArrayList
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
    
    public void addVehicle(VehicleData vehicle) {
        Connection conn = mysql.openConnection();
        String sql = "Insert into vehicles (number, seat, type, driver) values (?,?,?,?)";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            // Sends values from variables to database
            pstm.setString(1, vehicle.getVehicleNumber());
            pstm.setInt(2, vehicle.getSeatCount());
            pstm.setString(3, vehicle.getVehicleType());
            pstm.setInt(4, vehicle.getDriver());    // Driver is stored as ID in database
            
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
}