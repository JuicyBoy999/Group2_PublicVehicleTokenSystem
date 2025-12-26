/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.userData;
import java.sql.*;
import utils.PasswordService;

/**
 *
 * @author Nitro V 16
 */
public class LoginDao {
    MySqlConnection mysql = new MySqlConnection();
    
    public boolean login(userData user) {
        Connection conn = mysql.openConnection();
        if (conn == null) {
            System.out.println("LoginDao: Failed to open DB connection");
            return false;
        }
        
        String sql = "SELECT password FROM users WHERE email = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, user.getemail());
            ResultSet result = pstm.executeQuery();
            
            if (result.next()) {
                String storedHash = result.getString("password");
                return PasswordService.verifyPassword(user.getPassword(), storedHash);
            }
        } catch (SQLException e) {
            System.out.print("Login error: " + e);
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }
    
    public String loginAndGetRole(userData user) {
        Connection conn = mysql.openConnection();
        if (conn == null) {
            System.out.println("LoginDao: Failed to open DB connection");
            return null;
        }
        
        String sql = "SELECT password, role FROM users WHERE email = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, user.getemail());
            ResultSet result = pstm.executeQuery();
            
            if (result.next()) {
                String storedHash = result.getString("password");
                String role = result.getString("role");
                
                if (PasswordService.verifyPassword(user.getPassword(), storedHash)) {
                    return role;
                }
            }
        } catch (SQLException e) {
            System.out.print("Login error: " + e);
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    public int getUserIdByEmail(String email) {
        int userID = -1;    // Default if not found
        Connection conn = mysql.openConnection();
        
        if (conn == null) {
            System.out.println("LoginDao: Failed to open DB connection");
            return userID;
        }
        
        String sql = "Select user_id FROM users WHERE email = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
            
            if (rs.next()) {
                userID = rs.getInt("user_id");
            }
        }
        catch (SQLException e) {
            System.out.print("Error fetching user ID: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }
        return userID;
    }
}