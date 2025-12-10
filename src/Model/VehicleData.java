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
    private int driver;
    
    public VehicleData(String number, int seat, String type, int driver) {
        this.number = number;
        this.seat = seat;
        this.type = type;
        this.driver = driver;
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
    
    public void setDriver(int driver) {
        this.driver = driver;
    }
    public int getDriver() {
        return driver;
    }
    
    public void setVehicleID(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }
    public int getVehicleID() {
        return vehicle_id;
    }
}