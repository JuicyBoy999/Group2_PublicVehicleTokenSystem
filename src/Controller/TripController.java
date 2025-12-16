/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.TripDao;
import Model.TripData;
import View.Notification;
import View.RouteManagement;
import View.TripManagement;
import View.UserManagement;
import View.VehicleManagement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hp
 */
public class TripController {
    public final TripDao tripdao = new TripDao();
    public final TripManagement tripView;
    
    private int currentEditingId = -1; // tracks which vehicle is being edited
    
    public TripController(TripManagement tripView) {   // Constructor
        this.tripView = tripView;
        
        tripView.TripFormListener(new FormListener());
        tripView.AddTripListener(new AddActionListener());
        tripView.CancelTripListener(new CancelActionListener());
        tripView.VehicleManagementListener(new VehicleListener());
        tripView.RouteManagementListener(new RouteListener());
        tripView.UserManagementListener(new UserListener());
        tripView.NotificationListener(new NotifListener());
        
        // Mouse listener for Actions column
        tripView.getTripTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tripView.getTripTable().rowAtPoint(evt.getPoint());
                int col = tripView.getTripTable().columnAtPoint(evt.getPoint());

                if (col == 6) { // Actions column index = 6
                    DefaultTableModel model = (DefaultTableModel) tripView.getTripTable().getModel();
                    int tripId = (int) model.getValueAt(row, 0); // Hidden ID column
                    String vehicleNumber = (String) model.getValueAt(row, 1);

                    int choice = javax.swing.JOptionPane.showOptionDialog(
                        tripView,
                        "Do you want to Edit or Delete vehicle: " + vehicleNumber + "?",
                        "Vehicle Action",
                        javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                        javax.swing.JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Edit", "Delete", "Cancel"},
                        "Edit"
                    );

                    if (choice == 0) { // Edit
                        editTrip(tripId);
                    } else if (choice == 1) { // Delete
                        deleteTrip(tripId);
                    }
                }
            }
        });
    }
    
    public void openTripManagement() {    // Open UI
        this.tripView.setVisible(true);
        tripView.getFormPanel().setVisible(false);   // Hide form
        tripView.getScrollPane().setBounds(60, 310, 1150, 350);  // Move table up
        
        loadTripTable();
    }
    
    public void closeTripManagement() {   // Close UI
        this.tripView.dispose();
    }
 
    private void resetForm() {
        tripView.getVehicle().setSelectedIndex(0);
        tripView.getRoute().setSelectedIndex(0);
        tripView.getDeparture().setText("yyyy-mm-dd hh:mm:ss");
        tripView.getArrival().setText("yyyy-mm-dd hh:mm:ss");
        
        currentEditingId = -1; // Reset editing state
    }
    
    class FormListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetForm();    // Reset all fields to default
            loadVehicles();  // Fill combo box
            loadRoutes();

            // Show the form
            tripView.getFormPanel().setVisible(true);
            
            // Move table down
            tripView.getScrollPane().setBounds(60, 610, 1150, 100);

            // Change submit button text to "Add Vehicle"
            tripView.getSubmitButton().setText("Add Vehicle");
        }
    }

    // Add vehicles in JComboBox from database
    private void loadVehicles() {
        ArrayList<String> vehicles = tripdao.getVehicles();    // Fetch driver names
        JComboBox vehicle = tripView.getVehicle();
        vehicle.removeAllItems();    // Clear previous items
        
        vehicle.addItem("Select the Vehicle");    // Placeholder at index 0
        
        for (String v : vehicles) {
            vehicle.addItem(v);  // Add all vehicles from index 1
        }
    }
    
    // Add vehicles in JComboBox from database
    private void loadRoutes() {
        ArrayList<String> routes = tripdao.getRoutes();    // Fetch driver names
        JComboBox route = tripView.getRoute();
        route.removeAllItems();    // Clear previous items
        
        route.addItem("Select the Route");    // Placeholder at index 0
        
        for (String v : routes) {
            route.addItem(v);  // Add all routes from index 1
        }
    }
    
        
    class AddActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Read text fields from UI
                String vehicleNum = tripView.getVehicle().getSelectedItem().toString();    // Combobox returns object, convert it to string
                String routeName = tripView.getRoute().getSelectedItem().toString();
                String departure = tripView.getDeparture().getText();
                String arrival = tripView.getArrival().getText();

                // Validates inputs
                if (vehicleNum.equals("Select the Vehicle")) {
                    JOptionPane.showMessageDialog(tripView, "Please select a vehicle.");
                    return;
                }
                if (routeName.equals("Select the Route")) {
                    JOptionPane.showMessageDialog(tripView, "Please enter a valid number.");
                    return;
                }
                if (departure.equals("yyyy-mm-dd hh:mm:ss")) {
                    JOptionPane.showMessageDialog(tripView, "Please enter a valid date and time.");
                    return;
                }
                if (arrival.equals("yyyy-mm-dd hh:mm:ss")) {
                    JOptionPane.showMessageDialog(tripView, "Please enter a valid date and time.");
                    return;
                }
                
                // Map vehicle number to ID
                int vehicle = tripdao.getVehicleIdByNum(vehicleNum);
                if (vehicle == -1) {
                    JOptionPane.showMessageDialog(tripView, "Selected vehicle not found.");
                    return;
                }
                
                // Map route name to ID
                int route = tripdao.getRouteIdByName(routeName);
                if (route == -1) {
                    JOptionPane.showMessageDialog(tripView, "Selected route not found.");
                    return;
                }
                
                TripData tripdata = new TripData(vehicle, route, departure, arrival);

                // If editing, update the database
                if (currentEditingId != -1) {
                    tripdata.setTripID(currentEditingId);
                    
                    boolean check = tripdao.check(tripdata);  // Check if updated
                    if (check) {
                        JOptionPane.showMessageDialog(tripView, "Please update trip data.");
                        return;
                    }
                    else {
                        tripdao.updateTrip(tripdata);
                        JOptionPane.showMessageDialog(tripView, "Trip updated successfully.");
                        currentEditingId = -1;
                        tripView.getFormPanel().setVisible(false);
                        tripView.getScrollPane().setBounds(60, 310, 1150, 350);
                    }
                }
                else {
                    boolean check = tripdao.check(tripdata);  // Check if duplicate
                    if (check) {
                        JOptionPane.showMessageDialog(tripView, "Duplicate trip");
                        return;
                    }
                    else {
                        tripdao.addTrip(tripdata);
                        JOptionPane.showMessageDialog(tripView, "Trip added successfully.");
                    }
                }
                
                resetForm();
                loadTripTable();
            }
            catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    class CancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tripView.getFormPanel().setVisible(false);
            tripView.getScrollPane().setBounds(60, 310, 1150, 350);
        } 
    }
    
    private void editTrip(int tripId) {
        // Find the trip from your DAO
        ArrayList<TripData> vehicles = tripdao.getAllTrips();
        
        for (TripData t : vehicles) {
            if (t.getTripID() == tripId) {
                currentEditingId = tripId;

                // Populate form fields
                tripView.getVehicle().setSelectedItem(t.getVehicleNum());
                tripView.getRoute().setSelectedItem(t.getRouteName());
                tripView.getDeparture().setText(t.getDepartureTime());
                tripView.getArrival().setText(t.getArrivalTime());

                // Change Add button text to "Update"
                tripView.getSubmitButton().setText("Update Trip");
                tripView.getFormPanel().setVisible(true); // show the form
                break;
            }
        }
    }

    private void deleteTrip(int tripId) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(tripView,
                "Are you sure you want to delete this trip?",
                "Confirm Delete", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            tripdao.deleteTrip(tripId);
            loadTripTable();
        }
    }
    
    // Add vehicles in JTable from database
    private void loadTripTable() {
        ArrayList<TripData> triplist = tripdao.getAllTrips();
        DefaultTableModel tableModel = (DefaultTableModel) tripView.getTripTable().getModel();

        tableModel.setRowCount(0);  // Clear old rows
        
        if (triplist.isEmpty()) {
            tripView.getNoData().setVisible(true);  // Show "no data"
        }
        else {
            tripView.getNoData().setVisible(false); // Hide label
            
            for (TripData t : triplist) {
                Object[] row = {
                    t.getTripID(),     // Hidden ID column
                    t.getVehicleNum(),
                    t.getRouteName(),
                    t.getDepartureTime(),
                    t.getArrivalTime(),
                    t.getStatus(),
                    "Edit | Delete"
                };

                tableModel.addRow(row);
            }
        }
        updateNoDataLabelPosition();
    }
    
    
    private void updateNoDataLabelPosition() {
        int scrollX = tripView.getScrollPane().getX();
        int scrollY = tripView.getScrollPane().getY();
        int scrollWidth = tripView.getScrollPane().getWidth();
        int scrollHeight = tripView.getScrollPane().getHeight();

        // Get label preferred size
        int labelWidth = tripView.getNoData().getPreferredSize().width;
        int labelHeight = tripView.getNoData().getPreferredSize().height;

        // Center label inside scroll pane
        int labelX = scrollX + (scrollWidth - labelWidth) / 2;
        int labelY = scrollY + (scrollHeight - labelHeight) / 2;

        tripView.getNoData().setBounds(labelX, labelY, labelWidth, labelHeight);
        tripView.getNoData().repaint();
    }
    
    
    // Open Vehicle Management
    class VehicleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            VehicleManagement vm = new VehicleManagement();  // Create view
            VehicleController vc = new VehicleController(vm); // Create controller
            vc.openVehicleManagement();  // Open Vehicle Management page  
            closeTripManagement();   // Close Trip Management page
        }
    }
    
    // Open Route Management
    class RouteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RouteManagement rm = new RouteManagement();  // Create view
            rm.setVisible(true);    // Open Route Management page
            closeTripManagement();   // Close Trip Management page
        }
    }
    
    // Open User Management
    class UserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            UserManagement um = new UserManagement();  // Create view
            um.setVisible(true);    // Open User Management page
            closeTripManagement();   // Close Trip Management page
        }
    }
    
    // Open Notification
    class NotifListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Notification n = new Notification();  // Create view
            n.setVisible(true);    // Open Notification Management page
            closeTripManagement();   // Close Trip Management page
        }
    }
}