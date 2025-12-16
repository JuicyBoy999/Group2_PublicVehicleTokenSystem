/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author hp
 */
public class TripData {
    private int trip_id;
    private int vehicleId;
    private String vehicleName;
    private int routeId;
    private String routeName;
    private String departure;
    private String arrival;
    private String status;
    
    // Constructor for adding trip
    public TripData(int vehicleId, int routeId, String departure, String arrival) {
        this.vehicleId = vehicleId;
        this.routeId = routeId;
        this.departure = departure;
        this.arrival = arrival;
    }
    
    // Constructor for loading table
    public TripData(int vehicleId, String vehicleName, int routeId, String routeName, String departure, String arrival, String status) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.routeId = routeId;
        this.routeName = routeName;
        this.departure = departure;
        this.arrival = arrival;
        this.status = status;
    }
    
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }
    public int getVehicleId() {
        return vehicleId;
    }
    
    public void setVehicleNum(String vehicleName) {
        this.vehicleName = vehicleName;
    }
    public String getVehicleNum() {
        return vehicleName;
    }
    
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
    public int getRouteId() {
        return routeId;
    }
    
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
    public String getRouteName() {
        return routeName;
    }
    
    public void setDepartureTime(String departure) {
        this.departure = departure;
    }
    public String getDepartureTime() {
        return departure;
    }
    
    public void setArrivalTime(String arrival) {
        this.arrival = arrival;
    }
    public String getArrivalTime() {
        return arrival;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    
    public void setTripID(int trip_id) {
        this.trip_id = trip_id;
    }
    public int getTripID() {
        return trip_id;
    }
}