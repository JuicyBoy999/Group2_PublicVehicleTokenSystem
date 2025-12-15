/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.TripData;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author hp
 */
public class TripDao {
    MySqlConnection mysql = new MySqlConnection();
    
    // Return ArrayList of vehicles
    public ArrayList<String> getVehicles() {
        ArrayList<String> vehicles = new ArrayList<>();
        Connection conn = mysql.openConnection();
        String sql = "Select number from vehicles";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    // Iterate and add numbers to list
                    vehicles.add(rs.getString("number"));
                }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
        
        return vehicles;
    }
    
    // Map vehicle number to vehicle ID
    public int getVehicleIdByNum(String vehicleNum) {
        Connection conn = mysql.openConnection();
        String sql = "Select vehicle_id from vehicles where number = ?";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, vehicleNum);
            ResultSet rs = pstm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("vehicle_id");
            }
        }
        catch(SQLException e) {
            System.out.println("Error getting vehicle ID: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }
        
        return -1;  // not found
    }
    
    // Return ArrayList of routes
    public ArrayList<String> getRoutes() {
        ArrayList<String> routes = new ArrayList<>();
        Connection conn = mysql.openConnection();
        String sql = "Select route_name from routes";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    // Iterate and add route names to list
                    routes.add(rs.getString("route_name"));
                }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
        
        return routes;
    }
    
    // Map route name to route ID
    public int getRouteIdByName(String routeName) {
        Connection conn = mysql.openConnection();
        String sql = "Select route_id from routes where route_name = ?";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, routeName);
            ResultSet rs = pstm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("route_id");
            }
        }
        catch(SQLException e) {
            System.out.println("Error getting route ID: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }
        
        return -1;  // not found
    }
    
    
    public void addTrip(TripData trip) {
        Connection conn = mysql.openConnection();
        String sql = "Insert into trips (vehicle, route, departure, arrival) values (?,?,?,?)";
        
        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            // Sends values from variables to database
            pstm.setInt(1, trip.getVehicleId());    // Vehicle is stored as ID in database
            pstm.setInt(2, trip.getRouteId());  // Routes as well
            
            // String (UI Friendly) -> Datetime (Database friendly)
            pstm.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
            pstm.setTimestamp(4, Timestamp.valueOf(trip.getArrivalTime()));
            
            pstm.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
    }
    
    public boolean check(TripData trip) { // Check if unique or not
        Connection conn = mysql.openConnection();
        String sql;
        PreparedStatement pstm = null;

        try {
            if (trip.getTripID() > 0) {
                // Updating: ignore this vehicle
                sql = "Select * from trips where vehicle = ? and route = ? and departure = ? and trip_id <> ?";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, trip.getVehicleId());
                pstm.setInt(2, trip.getRouteId());
                pstm.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
                pstm.setInt(2, trip.getTripID());
            }
            else {
                // Adding: check normally
                sql = "Select * from trips where vehicle = ? and route = ? and departure = ?";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, trip.getVehicleId());
                pstm.setInt(2, trip.getRouteId());
                pstm.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
            }

            ResultSet rs = pstm.executeQuery();
            return rs.next();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }

        return false;
    }
    
    // Get full record by its ID
    public TripData getTripById(int tripId) {
        Connection conn = mysql.openConnection();
        String sql = "Select t.trip_id, t.departure, t.arrival, t.status, v.vehicle_id, v.number, r.route_id, r.route_name " +
                     "from trips t join vehicles v on t.vehicle = v.vehicle_id " +
                     "join routes r on t.route = r.route_id " +
                     "where t.trip_id = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, tripId);
            ResultSet rs = pstm.executeQuery();
            
            if (rs.next()) {
                TripData t = new TripData(
                    rs.getInt("vehicle_id"),
                    rs.getString("number"),
                    rs.getInt("route_id"),
                    rs.getString("route_name"),
                    rs.getString("departure"),
                    rs.getString("arrival"),
                    rs.getString("status")
                );
                t.setTripID(rs.getInt("trip_id"));
                return t;
            }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    public void updateTrip(TripData trip) {
        Connection conn = mysql.openConnection();
            String sql = "Update trips set vehicle = ?, route = ?, departure = ?, arrival = ? where trip_id = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, trip.getVehicleId());
            pstm.setInt(2, trip.getRouteId());
            pstm.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
            pstm.setTimestamp(4, Timestamp.valueOf(trip.getArrivalTime()));
            pstm.setInt(5, trip.getTripID());

            pstm.executeUpdate();
        } 
        catch(SQLException e) {
            System.out.println(e);
        } 
        finally {
            mysql.closeConnection(conn);
        }
    }
    
    public void deleteTrip(int tripId) {
        Connection conn = mysql.openConnection();
        String sql = "Delete from trips where trip_id = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, tripId);
            pstm.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
    }
    
    // Return ArrayList of all trips
    public ArrayList<TripData> getAllTrips() {
        ArrayList<TripData> triplist = new ArrayList<>();
        
        Connection conn = mysql.openConnection();
        String sql = "Select t.trip_id, t.departure, t.arrival, t.status, " + 
                     "v.vehicle_id, v.number, r.route_id, r.route_name from trips t " +
                     "join vehicles v on t.vehicle = v.vehicle_id " +
                     "join routes r on t.route = r.route_id";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();) {
                while (rs.next()) {
                    TripData t = new TripData (
                        rs.getInt("vehicle_id"),
                        rs.getString("number"),
                        rs.getInt("route_id"),
                        rs.getString("route_name"),
                        rs.getString("departure"),
                        rs.getString("arrival"),
                        rs.getString("status")
                    );
                    
                    t.setTripID(rs.getInt("trip_id")); // important!
                    triplist.add(t);
                }
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
        
        return triplist;
    }
}