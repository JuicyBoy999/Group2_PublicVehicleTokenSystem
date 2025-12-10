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

/**
 *
 * @author user
 */
public class userDAO {
        MySqlConnection mysql = new MySqlConnection(); 
    public void signUp(userData user){
        Connection conn = mysql.openConnection();
        String sql=  "Insert into users (username, email, phone,address, password) values( ?,?,?,?,?)";
        try(PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, user.getname());
            pstm.setString(2, user.getemail());
            pstm.setString(3, user.getphone());
            pstm.setString(4, user.getaddress());
            pstm.setString(5, user.getPassword());
            pstm.executeUpdate();
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            mysql.closeConnection(conn);
        }
    }
    
public boolean checkUser(userData user) {
    Connection conn = mysql.openConnection();
    String sql = "Select * from users where name = ? or email= ?";
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
}