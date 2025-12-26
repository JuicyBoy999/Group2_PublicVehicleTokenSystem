package Controller;

import DAO.NotificationDao;
import DAO.TripDao;
import Model.Notification;
import Model.TripData;
import View.Driver_Ongoing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class DriverController {
    private final Driver_Ongoing view;
    private final TripDao tripDao = new TripDao();
    private final NotificationDao notifDao = new NotificationDao();

    public DriverController(Driver_Ongoing view) {
        this.view = view;

        // Toggle start/end trip
        view.getToggleTripButton().addActionListener(new ToggleTripAction());
    }

    public void openDriverOngoing(int tripId) {
        view.setCurrentTripId(tripId);
        loadTripState(tripId);
        view.setVisible(true);
    }

    private void loadTripState(int tripId) {
        TripData t = tripDao.getTripById(tripId);
        if (t == null) {
            JOptionPane.showMessageDialog(view, "Trip not found.");
            return;
        }

        // Set started label if we have started info (departure used as fallback)
        view.setStartedTimeText(t.getDepartureTime());

        // If status is ONGOING -> show End Trip, otherwise show Start Trip
        if (t.getStatus() != null && t.getStatus().equalsIgnoreCase("ONGOING")) {
            view.setToggleButtonText("End Trip");
        } else {
            view.setToggleButtonText("Start Trip");
        }
    }

    class ToggleTripAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int tripId = view.getCurrentTripId();
            if (tripId <= 0) {
                JOptionPane.showMessageDialog(view, "No trip loaded.");
                return;
            }

            TripData t = tripDao.getTripById(tripId);
            if (t == null) {
                JOptionPane.showMessageDialog(view, "Trip not found.");
                return;
            }

            String currentStatus = t.getStatus();

            if (currentStatus == null || !currentStatus.equalsIgnoreCase("ONGOING")) {
                // Start trip
                boolean ok = tripDao.startTrip(tripId);
                if (ok) {
                    String msg = "Driver for vehicle " + t.getVehicleNum() + " has started the trip on route " + t.getRouteName() + ".";
                    notifDao.createNotification(new Notification(tripId, msg));
                    JOptionPane.showMessageDialog(view, "Trip started.");
                    view.setToggleButtonText("End Trip");
                    view.setStartedTimeText("(just now)");
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to start trip.");
                }
            } else {
                // End trip
                boolean ok = tripDao.endTrip(tripId);
                if (ok) {
                    String msg = "Driver for vehicle " + t.getVehicleNum() + " has ended the trip on route " + t.getRouteName() + ".";
                    notifDao.createNotification(new Notification(tripId, msg));
                    JOptionPane.showMessageDialog(view, "Trip ended.");
                    view.setToggleButtonText("Start Trip");
                } else {
                    JOptionPane.showMessageDialog(view, "Failed to end trip.");
                }
            }

            // refresh state
            loadTripState(tripId);
        }
    }
}
