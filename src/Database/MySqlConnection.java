/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author user
 */
public class MySqlConnection implements Database {
    
    @Override
    public Connection openConnection() {
        try{
            String password="";
            String username="root";
            String database="bato_plus";
            Connection connection;
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +database, username,password);
            if (connection != null){
                System.out.println("Database connected successfully!");
        }else{
            System.out.println("Database connection failed!");
        }
        return connection;
        }
        catch(SQLException e){
            System.out.println(e);
            return null;
        }
    }    

    @Override
    public void closeConnection(Connection conn) {
        try{
            if (conn !=null && !conn.isClosed()){
                conn.close();
            }
        }catch(SQLException e){
            System.out.println(e);
    }
}
    @Override
    public ResultSet runQuery(Connection conn, String query) {
        try{
            Statement stmp = conn.createStatement();            
            ResultSet result=stmp.executeQuery(query);
            return result;
        }catch(SQLException e){
            System.out.println(e); 
            return null;
        }
    }
    @Override
    public int executeUpdate(Connection conn, String query) {
        try{
            Statement stmp = conn.createStatement();            
            int result=stmp.executeUpdate(query);
            return result;
        }catch(SQLException e){
            System.out.println(e); 
            return -1;
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> overall
