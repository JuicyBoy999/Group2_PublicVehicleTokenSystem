/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author hp
 */
public class VehicleData {
    private int vehicle_id;
    private String number;
    private int seat;
    private String type;
    private int driverId;
    private String driverName;
    
    // Constructor for adding vehicle
    public VehicleData(String number, String type, int seat, int driverId) {
        this.number = number;
        this.seat = seat;
        this.type = type;
        this.driverId = driverId;
    }
    
    // Constructor for loading table
    public VehicleData(String number, String type, int seat, int driverId, String driverName) {
        this.number = number;
        this.type = type;
        this.seat = seat;
        this.driverId = driverId;
        this.driverName = driverName;
    }
    
    public void setVehicleNumber(String number) {
        this.number = number;
    }
    public String getVehicleNumber() {
        return number;
    }
    
    public void setSeatCount(int seat) {
        this.seat = seat;
    }
    public int getSeatCount() {
        return seat;
    }
    
    public void setVehicleType(String type) {
        this.type = type;
    }
    public String getVehicleType() {
        return type;
    }
    
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
    public int getDriverId() {
        return driverId;
    }
    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public String getDriverName() {
        return driverName;
    }
    
    public void setVehicleID(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }
    public int getVehicleID() {
        return vehicle_id;
    }
}