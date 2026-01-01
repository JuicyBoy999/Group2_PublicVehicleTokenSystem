/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Timestamp;

/**
 *
 * @author Nitro V 16
 */
public class VehicleReport {
    private int reportId;
    private Integer tripId; // nullable
    private String description;
    private Timestamp createdAt;
    private boolean isRead;

    public VehicleReport() {}

    public VehicleReport(Integer tripId, String description) {
        this.tripId = tripId;
        this.description = description;
    }

    // Full constructor used when loading from DB
    public VehicleReport(int reportId, Integer tripId, String description, Timestamp createdAt, boolean isRead) {
        this.reportId = reportId;
        this.tripId = tripId;
        this.description = description;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }

    public Integer getTripId() { return tripId; }
    public void setTripId(Integer tripId) { this.tripId = tripId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public java.sql.Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.sql.Timestamp createdAt) { this.createdAt = createdAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
