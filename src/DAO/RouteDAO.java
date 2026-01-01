/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.Route;
import java.sql.*;

/**
 *
 * @author Nitro V 16
 */


public class RouteDAO {
        MySqlConnection mysql = new MySqlConnection();
    public void addRoute(Route route){
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO routes(route_name, origin, destination, duration, fare) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setString(1, route.getRouteName());
            pstm.setString(2, route.getOrigin());
            pstm.setString(3, route.getDestination());
            pstm.setInt(4, route.getDuration());
            pstm.setDouble(5, route.getFare());
            pstm.executeUpdate();
        }
        catch(SQLException e){
            System.out.print(e);
        }
        finally{
            mysql.closeConnection(conn);
        }
    }
    
    public ResultSet getAllRoutes() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT * FROM routes";
        
        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            return pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }
    
    public void deleteRoute(int id) {
        Connection conn = mysql.openConnection();
        String sql = "DELETE FROM routes WHERE route_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
    }
    
    public void updateRoute(Route route) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE routes SET route_name=?, origin=?, destination=?, duration=?, fare=? WHERE route_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, route.getRouteName());
            pst.setString(2, route.getOrigin());
            pst.setString(3, route.getDestination());
            pst.setInt(4, route.getDuration());
            pst.setDouble(5, route.getFare());
            pst.setInt(6, route.getRoute_ID());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
    }
}