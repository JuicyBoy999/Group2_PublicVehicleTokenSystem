/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import java.sql.*;
import java.util.Date;


/**
 *
 * @author Nitro V 16
 */
public class OTPDAO {
     MySqlConnection mysql = new MySqlConnection();
    
    public boolean storeOTP(String email, String otp, java.util.Date expiryTime) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE users SET reset_otp = ?, otp_expiry = ? WHERE email = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            Timestamp expiryTimestamp = new Timestamp(expiryTime.getTime());
            
            pstm.setString(1, otp);
            pstm.setTimestamp(2, expiryTimestamp);
            pstm.setString(3, email);
            
            int rowsAffected = pstm.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Error storing OTP: " + e);
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }
    
    public boolean verifyOTP(String email, String enteredOTP) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT reset_otp, otp_expiry FROM users WHERE email = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, email);
            ResultSet result = pstm.executeQuery();
            
            if (result.next()) {
                String storedOTP = result.getString("reset_otp");
                Timestamp expiryTimestamp = result.getTimestamp("otp_expiry");
                
                if (storedOTP != null && expiryTimestamp != null) {
                    Date expiryTime = new Date(expiryTimestamp.getTime());
                    return enteredOTP.equals(storedOTP) && !expiryTime.before(new Date());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error verifying OTP: " + e);
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }
    public boolean clearOTP(String email) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE users SET reset_otp = NULL, otp_expiry = NULL WHERE email = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, email);
            int rowsAffected = pstm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error clearing OTP: " + e);
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }
    
    public boolean emailExists(String email) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, email);
            ResultSet result = pstm.executeQuery();
            
            if (result.next()) {
                return result.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking email: " + e);
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }
}
