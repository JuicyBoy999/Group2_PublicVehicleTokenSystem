/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.userData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.PasswordService;

/**
 *
 * @author user
 */
public class userDAO {
        MySqlConnection mysql = new MySqlConnection(); 
    public void signUp(userData user){
        Connection conn = mysql.openConnection();
        String sql=  "Insert into users (username, email, phone,address, password, role) values( ?,?,?,?,?,?)";
        try(PreparedStatement pstm = conn.prepareStatement(sql)){
            String hashedPassword = PasswordService.hashPassword(user.getPassword());
            pstm.setString(1, user.getname());
            pstm.setString(2, user.getemail());
            pstm.setString(3, user.getphone());
            pstm.setString(4, user.getaddress());
            pstm.setString(5, hashedPassword);
            pstm.setString(6, user.getrole());
            pstm.executeUpdate();
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            mysql.closeConnection(conn);
        }
    }
    
    public boolean updatePassword(String email, String newPassword) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            String hashedPassword = PasswordService.hashPassword(newPassword);
            
            pstm.setString(1, hashedPassword);
            pstm.setString(2, email);
            
            int rowsAffected = pstm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e);
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }
    
    public boolean checkUser(userData user) {
        Connection conn = mysql.openConnection();
        String sql = "Select * from users where username = ? or email= ?";
        try(PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, user.getname());
            pstm.setString(2, user.getemail());
            ResultSet result = pstm.executeQuery();
            return result.next();
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            mysql.closeConnection(conn);
        }
        return false;
    }
    
    public ResultSet getAllUsers() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT username, email, phone, role, created_at FROM users";
        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            return pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e);
            return null;
        }
    }
    
    public ResultSet getUsersByRole(String role) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT username, email, phone, role, created_at FROM users WHERE role = ?";
        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setString(1, role);
            return pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching users by role: " + e);
            return null;
        }
    }

    public userData getUserById(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                userData user = new userData(
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("username"), // mapped to name
                    rs.getString("password"),
                    rs.getString("address"),
                    rs.getString("role")
                );
                user.setId(rs.getInt("user_id"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by ID: " + e);
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }
}