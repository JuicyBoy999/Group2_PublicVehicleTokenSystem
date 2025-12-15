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
        String sql = "Select username from users where role = 'driver'";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    // Iterate and add names to list
                    drivers.add(rs.getString("username"));
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
        String sql = "Select user_id from users where username = ?";
        
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
        String sql;
        PreparedStatement pstm = null;

        try {
            if (vehicle.getVehicleID() > 0) {
                // Updating: ignore this vehicle
                sql = "Select * from vehicles where number = ? and vehicle_id <> ?";
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, vehicle.getVehicleNumber());
                pstm.setInt(2, vehicle.getVehicleID());
            } else {
                // Adding: check normally
                sql = "Select * from vehicles where number = ?";
                pstm = conn.prepareStatement(sql);
                pstm.setString(1, vehicle.getVehicleNumber());
            }

            ResultSet rs = pstm.executeQuery();
            return rs.next();
        } catch(SQLException e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }

        return false;
    }
    
    public VehicleData getVehicleById(int vehicleId) {
        Connection conn = mysql.openConnection();
        String sql = "Select v.vehicle_id, v.number, v.type, v.seat, u.user_id, u.username " +
                     "from vehicles v join users u on v.driver = u.user_id where v.vehicle_id = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, vehicleId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                VehicleData v = new VehicleData(
                    rs.getString("number"),
                    rs.getString("type"),
                    rs.getInt("seat"),
                    rs.getInt("user_id"),
                    rs.getString("name")
                );
                v.setVehicleID(rs.getInt("vehicle_id"));
                return v;
            }
        } catch(SQLException e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    public void updateVehicle(VehicleData vehicle) {
        Connection conn = mysql.openConnection();
        String sql = "Update vehicles set number = ?, type = ?, seat = ?, driver = ? where vehicle_id = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, vehicle.getVehicleNumber());
            pstm.setString(2, vehicle.getVehicleType());
            pstm.setInt(3, vehicle.getSeatCount());
            pstm.setInt(4, vehicle.getDriverId());
            pstm.setInt(5, vehicle.getVehicleID());

            pstm.executeUpdate();
        } 
        catch(SQLException e) {
            System.out.println(e);
        } 
        finally {
            mysql.closeConnection(conn);
        }
    }
    
    public void deleteVehicle(int vehicleId) {
        Connection conn = mysql.openConnection();
        String sql = "Delete from vehicles where vehicle_id = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, vehicleId);
            pstm.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
    }
    
    // Return ArrayList of all vehicles
    public ArrayList<VehicleData> getAllVehicles() {
        ArrayList<VehicleData> vehiclelist = new ArrayList<>();
        Connection conn = mysql.openConnection();
        String sql = "Select v.vehicle_id, v.number, v.type, v.seat, u.user_id, u.username from vehicles v " +
                     "join users u on v.driver = u.user_id";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();) {
                while (rs.next()) {
                    VehicleData v = new VehicleData (
                        rs.getString("number"),
                        rs.getString("type"),
                        rs.getInt("seat"),
                        rs.getInt("user_id"),
                        rs.getString("name")
                    );
                    
                    v.setVehicleID(rs.getInt("vehicle_id")); // important!
                    vehiclelist.add(v);

                }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
        
        return vehiclelist;
    }
}