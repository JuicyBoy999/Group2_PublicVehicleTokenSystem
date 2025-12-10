/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.userData;
import java.sql.*;

/**
 *
 * @author Nitro V 16
 */
public class LoginDao {
     MySqlConnection mysql = new MySqlConnection();
    
    public boolean login(userData user) {
        Connection conn = mysql.openConnection();
        if (conn == null){
            System.out.println("LoginDao: Failed to open DB connection");
            return false;
        }
        String sql = "SELECT * FROM users WHERE email=? AND Password=?";

        try (PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, user.getemail());
            pstm.setString(2, user.getPassword());

            ResultSet result = pstm.executeQuery();
            return result.next();  

        } catch (SQLException e) {
            System.out.print(e);
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }
}

