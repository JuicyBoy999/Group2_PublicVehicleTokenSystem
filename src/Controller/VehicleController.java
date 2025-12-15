/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.VehicleDao;
import Model.VehicleData;
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
public class VehicleController {
    public final VehicleDao vehicledao = new VehicleDao();
    public final VehicleManagement vehicleView;
    
    private int currentEditingId = -1; // tracks which vehicle is being edited
    
    public VehicleController(VehicleManagement vehicleView) {   // Constructor
        this.vehicleView = vehicleView;
        
        vehicleView.VehicleFormListener(new FormListener());
        vehicleView.AddVehicleListener(new AddActionListener());
        vehicleView.CancelVehicleListener(new CancelActionListener());
        vehicleView.RouteManagementListener(new RouteListener());
        vehicleView.TripManagementListener(new TripListener());
        vehicleView.UserManagementListener(new UserListener());
        vehicleView.NotificationListener(new NotifListener());
        
        // Mouse listener for Actions column
        vehicleView.getVehicleTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = vehicleView.getVehicleTable().rowAtPoint(evt.getPoint());
                int col = vehicleView.getVehicleTable().columnAtPoint(evt.getPoint());

                if (col == 5) { // Actions column index = 5
                    DefaultTableModel model = (DefaultTableModel) vehicleView.getVehicleTable().getModel();
                    int vehicleId = (int) model.getValueAt(row, 0); // Hidden ID column
                    String vehicleNumber = (String) model.getValueAt(row, 1);

                    int choice = javax.swing.JOptionPane.showOptionDialog(
                        vehicleView,
                        "Do you want to Edit or Delete vehicle: " + vehicleNumber + "?",
                        "Vehicle Action",
                        javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                        javax.swing.JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Edit", "Delete", "Cancel"},
                        "Edit"
                    );

                    if (choice == 0) { // Edit
                        editVehicle(vehicleId);
                    }
                    else if (choice == 1) { // Delete
                        deleteVehicle(vehicleId);
                    }
                }
            }
        });
    }
    
    public void openVehicleManagement() {    // Open UI
        this.vehicleView.setVisible(true);
        vehicleView.getFormPanel().setVisible(false);   // Hide form
        vehicleView.getScrollPane().setBounds(60, 310, 1150, 350);  // Move table up
        loadDrivers();
        loadVehicleTable();
    }
    
    public void closeVehicleManagement() {   // Close UI
        this.vehicleView.dispose();
    }
 
    private void resetForm() {
        vehicleView.getVehicleNumber().setText("######");
        vehicleView.getSeatCount().setText("0");
        vehicleView.getVehicleType().setSelectedIndex(0);
        vehicleView.getDriver().setSelectedIndex(0);
        currentEditingId = -1; // Reset editing state
    }
    
    class FormListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetForm();    // Reset all fields to default

            // Show the form
            vehicleView.getFormPanel().setVisible(true);
            
            // Move table down
            vehicleView.getScrollPane().setBounds(60, 610, 1150, 100);

            // Change submit button text to "Add Vehicle"
            vehicleView.getSubmitButton().setText("Add Vehicle");
        }
    }

    // Add drivers in JComboBox from database
    private void loadDrivers() {
        ArrayList<String> drivers = vehicledao.getDrivers();    // Fetch driver names
        JComboBox driver = vehicleView.getDriver();
        driver.removeAllItems();    // Clear previous items
        
        driver.addItem("Select the Driver");    // Placeholder at index 0
        
        for (String d : drivers) {
            driver.addItem(d);  // Add all drivers from index 1
        }
    }
        
    class AddActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Read text fields from UI
                String number = vehicleView.getVehicleNumber().getText();
                int seat = Integer.parseInt(vehicleView.getSeatCount().getText());  // Convert String from text field to int
                String type = vehicleView.getVehicleType().getSelectedItem().toString();    // Combobox returns object, convert it to string
                String driverName = vehicleView.getDriver().getSelectedItem().toString();

                // Validates inputs
                if (number.equals("######")) {
                    JOptionPane.showMessageDialog(vehicleView, "Please enter a valid vehicle number.");
                    return;
                }
                if (seat == 0) {
                    JOptionPane.showMessageDialog(vehicleView, "Please enter a valid number.");
                    return;
                }
                if (type.equals("Select the Vehicle Type")) {
                    JOptionPane.showMessageDialog(vehicleView, "Please select a vehicle type.");
                    return;
                }
                if (driverName.equals("Select the Driver")) {
                    JOptionPane.showMessageDialog(vehicleView, "Please select a driver.");
                    return;
                }
                
                // Map driver name to ID
                int driver = vehicledao.getDriverIdByName(driverName);
                if (driver == -1) {
                    JOptionPane.showMessageDialog(vehicleView, "Selected driver not found.");
                    return;
                }
                
                VehicleData vehicledata = new VehicleData(number, type, seat, driver);

                // If editing, update the database
                if (currentEditingId != -1) {
                    vehicledata.setVehicleID(currentEditingId);
                    
                    boolean check = vehicledao.check(vehicledata);  // Check if updated
                    if (check) {
                        JOptionPane.showMessageDialog(vehicleView, "Please update vehicle data.");
                        return;
                    }
                    else {
                        vehicledao.updateVehicle(vehicledata);
                        JOptionPane.showMessageDialog(vehicleView, "Vehicle updated successfully.");
                        currentEditingId = -1;
                        vehicleView.getFormPanel().setVisible(false);
                        vehicleView.getScrollPane().setBounds(60, 310, 1150, 350);
                    }
                }
                else {
                    boolean check = vehicledao.check(vehicledata);  // Check if duplicate
                    if (check) {
                        JOptionPane.showMessageDialog(vehicleView, "Duplicate vehicle");
                        return;
                    }
                    else {
                        vehicledao.addVehicle(vehicledata);
                        JOptionPane.showMessageDialog(vehicleView, "Vehicle added successfully.");
                    }
                }
                
                resetForm();
                loadVehicleTable();
            }
            catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    class CancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            vehicleView.getFormPanel().setVisible(false);
            vehicleView.getScrollPane().setBounds(60, 310, 1150, 350);
        } 
    }
    
    private void editVehicle(int vehicleId) {
        // Find the vehicle from your DAO
        ArrayList<VehicleData> vehicles = vehicledao.getAllVehicles();
        
        for (VehicleData v : vehicles) {
            if (v.getVehicleID() == vehicleId) {
                currentEditingId = vehicleId;

                // Populate form fields
                vehicleView.getVehicleNumber().setText(v.getVehicleNumber());
                vehicleView.getSeatCount().setText(String.valueOf(v.getSeatCount()));
                vehicleView.getVehicleType().setSelectedItem(v.getVehicleType());
                vehicleView.getDriver().setSelectedItem(v.getDriverName());

                // Change Add button text to "Update"
                vehicleView.getSubmitButton().setText("Update");
                vehicleView.getFormPanel().setVisible(true); // show the form
                break;
            }
        }
    }

    private void deleteVehicle(int vehicleId) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(vehicleView,
                "Are you sure you want to delete this vehicle?",
                "Confirm Delete", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            vehicledao.deleteVehicle(vehicleId);
            loadVehicleTable();
        }
    }
    
    // Add vehicles in JTable from database
    private void loadVehicleTable() {
        ArrayList<VehicleData> vehiclelist = vehicledao.getAllVehicles();
        DefaultTableModel tableModel = (DefaultTableModel) vehicleView.getVehicleTable().getModel();

        tableModel.setRowCount(0);  // Clear old rows

        for (VehicleData v : vehiclelist) {
            Object[] row = {
                v.getVehicleID(),     // Hidden ID column
                v.getVehicleNumber(),
                v.getVehicleType(),
                v.getSeatCount(),
                v.getDriverName(),
                "Edit | Delete"
            };
            
            tableModel.addRow(row);
        }
    }
    
    // Open Route Management
    class RouteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RouteManagement rm = new RouteManagement();  // Create view
            rm.setVisible(true);    // Open Route Management page
            closeVehicleManagement();   // Close Vehicle Management page
        }
    }
    
    // Open Trip Management
    class TripListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            TripManagement tm = new TripManagement();  // Create view
            TripController tc = new TripController(tm); // Create controller
            tc.openTripManagement();  // Open Trip Management page
            closeVehicleManagement();   // Close Vehicle Management page
        }
    }
    
    // Open User Management
    class UserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            UserManagement um = new UserManagement();  // Create view
            um.setVisible(true);    // Open User Management page
            closeVehicleManagement();   // Close Vehicle Management page
        }
    }
    
    // Open Notification
    class NotifListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Notification n = new Notification();  // Create view
            n.setVisible(true);    // Open Notification Management page
            closeVehicleManagement();   // Close Vehicle Management page
        }
    }
}