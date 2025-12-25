/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author hp
 */
public class NotificationData {
    private int notification_id;
    private int tripId;
    private String tripLabel;   // Human readable string for combo-box
    private String genre;
    private String message;
    
    public NotificationData(int tripId, String genre, String message) {
        this.tripId = tripId;
        this.genre = genre;
        this.message = message;
    }
    
    public NotificationData(int tripId, String tripLabel, String genre, String message) {
        this.tripId = tripId;
        this.tripLabel = tripLabel;
        this.genre = genre;
        this.message = message;
    }
    
    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
    public int getTripId() {
        return tripId;
    }
    
    public void setTripLabel(String tripLabel) {
        this.tripLabel = tripLabel;
    }
    public String getTripLabel() {
        return tripLabel;
    }
    
    public void setType(String genre) {
        this.genre = genre;
    }
    public String getType() {
        return genre;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    
    public void setNotifID(int notification_id) {
        this.notification_id = notification_id;
    }
    public int getNotifID() {
        return notification_id;
    }
    
    @Override
    public String toString() {
        return tripLabel;
    }
}
