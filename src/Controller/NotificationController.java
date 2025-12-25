/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.NotificationDao;
import Model.NotificationData;
import View.Notification;
import View.RouteManagement;
import View.TripManagement;
import View.UserManagement;
import View.VehicleManagement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import utils.EmailService;

/**
 *
 * @author hp
 */
public class NotificationController {
    public final NotificationDao notifdao = new NotificationDao();
    public final Notification notifView;
    
    Map<String, Integer> tripMap = new HashMap<>();
        
    public NotificationController(Notification notifView) { // Constructor
        this.notifView = notifView;
        
        notifView.SendNotificationListener(new SendListener());
        notifView.VehicleManagementListener(new VehicleListener());
        notifView.RouteManagementListener(new RouteListener());
        notifView.TripManagementListener(new TripListener());
        notifView.UserManagementListener(new UserListener());
    }
    
    public void openNotification() {    // Open UI
        this.notifView.setVisible(true);
        loadTrips();
    }
    
    public void closeNotification() {
        this.notifView.dispose();
    }
    
    private void resetForm() {
        notifView.getTrip().setSelectedIndex(0);
        notifView.getGenre().setSelectedIndex(0);
        notifView.getMessage().setText("Enter your message here");
    }
    
    // Add trips in JComboBox from database
    private void loadTrips() {
        notifView.getTrip().removeAllItems();   // Clear previous items
        notifView.getTrip().addItem("Select the Trip"); // Placeholder at index 0
        
        tripMap.clear();    // Reset Mapping
        
        for (NotificationData n : notifdao.getTrips()) {
            String label = n.getTripLabel();
            int id = n.getTripId();
            
            notifView.getTrip().addItem(label);  // UI only sees label
            tripMap.put(label, id); // Controller remembers ID
        }
    }
    
    class SendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Read text fields from UI
                String trip = notifView.getTrip().getSelectedItem().toString();
                String type = notifView.getGenre().getSelectedItem().toString();
                String message = notifView.getMessage().getText();
                
                // Validates inputs
                if (trip.equals("Select the Trip")) {
                    JOptionPane.showMessageDialog(notifView, "Please select a trip.");
                    return;
                }
                if (type.equals("Select Notification Type")) {
                    JOptionPane.showMessageDialog(notifView, "Please select a notification type.");
                    return;
                }
                if (message.equals("Enter your message here")) {
                    JOptionPane.showMessageDialog(notifView, "Please enter message.");
                    return;
                }
                
                // Get real trip id from map
                int tripID = tripMap.get(trip);
                
                // Save notification
                NotificationData notif = new NotificationData(tripID, type, message);
                notifdao.saveNotification(notif);
                
                //Build email body
                String subject = "Trip Update: " + type;
                String body = "<h3>Trip Notification</h3>" +
                                   "<p><strong>Type:</strong> " + type + "</p>" +
                                   "<p>" + message + "</p>" +
                                   "<br><p>Thank you,<br>Bato+ Management Team</p>";
                
                // Get passenger emails
                ArrayList<String> emails = notifdao.getPassengerEmailsByTrip(tripID);
                
                // Send email
                if (emails.isEmpty()) {
                    JOptionPane.showMessageDialog(notifView, "No passengers booked for this trip.");
                }
                else {
                    for (String email : emails) {
                        EmailService.sendEmail(email, subject, body);
                    }
                    JOptionPane.showMessageDialog(notifView, "Notification sent successfully!");
                }
                resetForm();
            }
            catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        } 
    }
    
    // Open Vehicle Management
    class VehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            VehicleManagement vm = new VehicleManagement();  // Create view
            VehicleController vc = new VehicleController(vm); // Create controller
            vc.openVehicleManagement();  // Open Vehicle Management page  
            closeNotification();   // Close Notification page
        }
    }
    
    // Open Route Management
    class RouteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RouteManagement rm = new RouteManagement();  // Create view
            rm.setVisible(true);    // Open Route Management page
            closeNotification();   // Close Notification page
        }
    }
    
    // Open Trip Management
    class TripListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TripManagement tm = new TripManagement();  // Create view
            TripController tc = new TripController(tm); // Create controller
            tc.openTripManagement();  // Open Trip Management page
            closeNotification();   // Close Notification page
        }
    }
    
    // Open User Management
    class UserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            UserManagement um = new UserManagement();  // Create view
            um.setVisible(true);    // Open User Management page
            closeNotification();   // Close Notification page
        }
    }
}