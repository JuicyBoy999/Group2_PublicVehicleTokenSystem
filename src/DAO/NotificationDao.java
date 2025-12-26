package DAO;

import Database.MySqlConnection;
import Model.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationDao {
    private final MySqlConnection mysql = new MySqlConnection();

    /**
     * Insert a notification (simple storage).
     * 
     * Database note: If you don't have a `notifications` table yet, create it like:
     *
     * CREATE TABLE notifications (
     *   notification_id INT AUTO_INCREMENT PRIMARY KEY,
     *   trip_id INT,
     *   message TEXT,
     *   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     * );
     *
     * This DAO intentionally keeps notifications simple so UI/clients can read them.
     */
    public boolean createNotification(Notification n) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO notifications (trip_id, message, created_at) VALUES (?,?,CURRENT_TIMESTAMP)";

        try (PreparedStatement p = conn.prepareStatement(sql)) {
            p.setInt(1, n.getTripId());
            p.setString(2, n.getMessage());
            int affected = p.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("Notification insert error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }
}
