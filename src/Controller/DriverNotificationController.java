package Controller;

import DAO.NotificationDao;
import Model.NotificationData;
import java.util.ArrayList;

/**
 * Controller for driver-generated notifications (delay, vehicle problem, etc.)
 */
public class DriverNotificationController {

    private final NotificationDao notificationDao;
    public Integer adminId = null;  // Integer to allow null

    public DriverNotificationController() {
        this.notificationDao = new NotificationDao();
    }

    /**
     * Synchronously report a delay for a specific trip.
     * Saves a notification to DB and returns the list of emails that were attempted.
     *
     * @param tripId       Trip id for which delay is reported (must be > 0)
     * @param delayMinutes Estimated delay in minutes (non-empty)
     * @param reason       Optional reason
     * @return true if operation completed (notification saved). Note: returning true
     *         doesn't guarantee every email was delivered — email sending errors are logged.
     */
    public boolean reportDelay(int tripId, String delayMinutes, String reason) {
        if (tripId <= 0) {
            System.out.println("reportDelay: invalid tripId");
            return false;
        }
        if (delayMinutes == null || delayMinutes.trim().isEmpty()) {
            System.out.println("reportDelay: delay minutes missing");
            return false;
        }

        String message = "Trip delayed by " + delayMinutes + " minute" + (delayMinutes.equals("1") ? "" : "s") +
                         ". Reason: " + (reason != null && !reason.trim().isEmpty() ? reason.trim() : "Unknown");

        try {
            // Save notification to DB (notification_type will be 'DELAY')
            NotificationData notif = new NotificationData(tripId, "DELAY", message, adminId);
            notificationDao.saveNotification(notif);

            // Fetch passenger emails (NotificationDao handles filtering by booking status)
            ArrayList<String> emails = notificationDao.getPassengerEmailsByTrip(tripId);

            // Build email subject & body
            String subject = "Trip Delay Notification";
            String body = "<h3>Trip Delay</h3>"
                        + "<p>" + message + "</p>"
                        + "<br><p>Thank you,<br>Bato+ Management Team</p>";

            // Send emails (best-effort). EmailService is expected to handle exceptions internally.
            if (emails != null && !emails.isEmpty()) {
                for (String email : emails) {
                    try {
                        utils.EmailService.sendEmail(email, subject, body);
                    } catch (Exception ex) {
                        // Log and continue sending to other passengers
                        System.out.println("Error sending delay email to " + email + ": " + ex.getMessage());
                    }
                }
            } else {
                // No emails found — still returns true because notification was saved
                System.out.println("reportDelay: no passenger emails found for trip " + tripId);
            }

            return true;
        } catch (Exception ex) {
            System.out.println("reportDelay error: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Report a vehicle problem to admin (synchronous).
     */
    public boolean reportVehicleProblem(String problemDescription) {
        if (problemDescription == null || problemDescription.trim().isEmpty()) {
            return false;
        }

        try {
            NotificationData notif = new NotificationData(0, "PROBLEM", problemDescription.trim(), adminId);
            notificationDao.saveNotification(notif);
            return true;
        } catch (Exception ex) {
            System.out.println("reportVehicleProblem error: " + ex.getMessage());
            return false;
        }
    }
}
