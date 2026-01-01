/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.TripData;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hp
 */
public class TripDao {
    MySqlConnection mysql = new MySqlConnection();
    
    public ArrayList<String> getVehicles() {
        ArrayList<String> vehicles = new ArrayList<>();
        Connection conn = mysql.openConnection();
        
        String sql = "SELECT type, number FROM vehicles";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                String display = rs.getString("type") + " - " + rs.getString("number");
                vehicles.add(display);
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
        return vehicles;
    }
    
    public int getVehicleIdByNum(String vehicleDisplay) {
        String vehicleNum = vehicleDisplay.split(" - ")[1];
        
        Connection conn = mysql.openConnection();
        String sql = "SELECT vehicle_id FROM vehicles WHERE number = ?";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, vehicleNum);
            ResultSet rs = pstm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("vehicle_id");
            }
        }
        catch (SQLException e) {
            System.out.println("Error getting vehicle ID: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }
        return -1;
    }
    
    public ArrayList<String> getRoutes() {
        ArrayList<String> routes = new ArrayList<>();
        Connection conn = mysql.openConnection();
        
        String sql = "SELECT route_name, origin, destination FROM routes";
        
        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                String display = rs.getString("route_name") + " - " + rs.getString("origin") + " -> " + rs.getString("destination");
                routes.add(display);
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
        return routes;
    }
    
    public int getRouteIdByName(String routeDisplay) {
        String routeName = routeDisplay.split(" - ")[0];
        
        Connection conn = mysql.openConnection();
        String sql = "SELECT route_id FROM routes WHERE route_name = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, routeName);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("route_id");
            }
        }
        catch (SQLException e) {
            System.out.println("Error getting route ID: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(conn);
        }
        return -1;
    }
    
    public int startTrip(int tripId) {
        Connection con = mysql.openConnection();
        String sql = "UPDATE trips SET status = ?, started_at = CURRENT_TIME WHERE trip_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "ONGOING");
            ps.setInt(2, tripId);
            ps.executeUpdate();

        }
        catch (Exception e) {
            System.out.println("No trips found");
        }
        finally {
            mysql.closeConnection(con);
        }
        return -1;
}

    public void EndTrip(int tripId) {
        Connection con = mysql.openConnection();
        String sql = "UPDATE trips SET status = ?, ended_at = CURRENT_TIME WHERE trip_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "COMPLETED");
            ps.setInt(2, tripId);
            ps.executeUpdate();

        }
        catch (Exception e) {
            System.out.println("No trips found");
        }
        finally {
            mysql.closeConnection(con);
        }
    }

    
    public void addTrip(TripData trip) {
        Connection conn = mysql.openConnection();
        String sql = "Insert into trips (vehicle, route, departure, arrival, available_seats) " +
                     "values (?,?,?,?, (select seat from vehicles where vehicle_id = ?))";
        try(PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, trip.getVehicleId());
            pstm.setInt(2, trip.getRouteId());
            pstm.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
            pstm.setTimestamp(4, Timestamp.valueOf(trip.getArrivalTime()));
            pstm.setInt(5, trip.getVehicleId());    // For the subquery
            pstm.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
        finally {
            mysql.closeConnection(conn);
        }
    }
    
    public boolean check(TripData trip) {
        Connection conn = mysql.openConnection();
        String sql;
        PreparedStatement pstm = null;
        try {
            if (trip.getTripID() > 0) {
                sql = "Select * from trips where vehicle = ? and route = ? and departure = ? and trip_id <> ?";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, trip.getVehicleId());
                pstm.setInt(2, trip.getRouteId());
                pstm.setTimestamp(3, Timestamp.valueOf(trip.getDepartureTime()));
                pstm.setInt(2, trip.getTripID());
            }
            else {
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
                    rs.getString("origin"),
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
    
    public void cancelTrip(int tripId) {
        Connection con = mysql.openConnection();
        String sql = "Update trips set status = 'Cancelled' where trip_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tripId);
            ps.executeUpdate();

        }
        catch (Exception e) {
            System.out.println("Cancel trip error: " + e.getMessage());
        }
        finally {
            mysql.closeConnection(con);
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
                        rs.getString("origin"),
                        rs.getString("arrival"),
                        rs.getString("status")
                    );
                    
                    t.setTripID(rs.getInt("trip_id")); 
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
    
    public String getVehicleDisplayById(int vehicleId) {
    Connection conn = mysql.openConnection();
    String sql = "SELECT type, number FROM vehicles WHERE vehicle_id = ?";

    try (PreparedStatement pstm = conn.prepareStatement(sql)) {
        pstm.setInt(1, vehicleId);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            return rs.getString("type") + " - " + rs.getString("number");
        }
    }
    catch (SQLException e) {
        System.out.println(e);
    }
    finally {
        mysql.closeConnection(conn);
    }
    return null;
}

    public String getRouteDisplayById(int routeId) {
    Connection conn = mysql.openConnection();
    String sql = "SELECT route_name, origin, destination FROM routes WHERE route_id = ?";

    try (PreparedStatement pstm = conn.prepareStatement(sql)) {
        pstm.setInt(1, routeId);
        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            return rs.getString("route_name") + " - " +
                   rs.getString("origin") + " -> " +
                   rs.getString("destination");
        }
    }
    catch (SQLException e) {
        System.out.println(e);
    }
    finally {
        mysql.closeConnection(conn);
    }
    return null;
}

    public List<TripData> getAssignedTrips(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}