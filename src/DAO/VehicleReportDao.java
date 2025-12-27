/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.MySqlConnection;
import Model.VehicleReport;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nitro V 16
 */
public class VehicleReportDao {
    private final MySqlConnection mysql = new MySqlConnection();

    // Save a new report
    public boolean saveReport(VehicleReport report) {
        String sql = "INSERT INTO vehicle_reports (trip_id, description, created_at, is_read) VALUES (?, ?, NOW(), FALSE)";
        Connection conn = mysql.openConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (report.getTripId() != null) {
                ps.setInt(1, report.getTripId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, report.getDescription());
            int r = ps.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // Get unread reports (new ones)
    public List<VehicleReport> getUnreadReports() {
        List<VehicleReport> list = new ArrayList<>();
        String sql = "SELECT report_id, trip_id, description, created_at, is_read FROM vehicle_reports WHERE is_read = FALSE ORDER BY created_at ASC";
        Connection conn = mysql.openConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VehicleReport vr = new VehicleReport(
                    rs.getInt("report_id"),
                    rs.getObject("trip_id") != null ? rs.getInt("trip_id") : null,
                    rs.getString("description"),
                    rs.getTimestamp("created_at"),
                    rs.getBoolean("is_read")
                );
                list.add(vr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    // Mark a report as read (so it won't popup again)
    public boolean markAsRead(int reportId) {
        String sql = "UPDATE vehicle_reports SET is_read = TRUE WHERE report_id = ?";
        Connection conn = mysql.openConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reportId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }
}
