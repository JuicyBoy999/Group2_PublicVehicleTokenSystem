/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.VehicleData;
import java.sql.*;

/**
 *
 * @author hp
 */
public class VehicleDao {
    MySqlConnection mysql = new MySqlConnection();
    
    public void addVehicle(VehicleData vehicle) {
        Connection conn = mysql.openConnection();
        String sql = "Insert into vehicles (number, seat, type, driver) values (?,?,?,?)";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, vehicle.getVehicleNumber());  // Sends values from variables to database
            pstm.setInt(2, vehicle.getSeatCount());
            pstm.setString(3, vehicle.getVehicleType());
            pstm.setInt(4, vehicle.getDriver());
            
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