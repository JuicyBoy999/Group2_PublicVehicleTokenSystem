/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author hp
 */
public class SearchData {
    private int tripId;
    private String vehicleNumber;
    private String vehicleType;
    private String origin;
    private String destination;
    private String departure;
    private String arrival;
    private double fare;
    private int seats;
    
    // Constructor for loading table
    public SearchData(int tripId, String vehicleNumber, String vehicleType, String origin, String destination, String departure, String arrival, double fare, int seats) {
        this.tripId = tripId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
        this.fare = fare;
        this.seats = seats;
    }
    
    public int getTripId() {
        return tripId;
    }

    public String getVehicleNum() {
        return vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departure;
    }

    public String getArrivalTime() {
        return arrival;
    }

    public double getFare() {
        return fare;
    }

    public int getAvailableSeats() {
        return seats;
    }
}