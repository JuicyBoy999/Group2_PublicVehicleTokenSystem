/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.VehicleDao;
import Model.VehicleData;
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
    
    public VehicleController(VehicleManagement vehicleView) {   // Constructor
        this.vehicleView = vehicleView;
        
        vehicleView.AddVehicleListener(new AddActionListener());
        vehicleView.CancelVehicleListener(new CancelActionListener());
    }
    
    public void open() {    // Open UI
        this.vehicleView.setVisible(true);
        loadDrivers();
        loadVehicleTable();
    }
    
    public void close() {   // Close UI
        this.vehicleView.dispose();
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

                boolean check = vehicledao.check(vehicledata);  // Check if duplicate
                if (check) {
                    JOptionPane.showMessageDialog(vehicleView, "Duplicate vehicle");
                }
                else {
                    vehicledao.addVehicle(vehicledata);
                    JOptionPane.showMessageDialog(vehicleView, "Vehicle added successfully.");
                }
            }
            catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    class CancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {    // Reset all fields
            vehicleView.getVehicleNumber().setText("######");
            vehicleView.getSeatCount().setText("0");
            vehicleView.getVehicleType().setSelectedIndex(0);
            vehicleView.getDriver().setSelectedIndex(0);
        }
        
    }
    
    // Add vehicles in JTable from database
    private void loadVehicleTable() {
        ArrayList<VehicleData> vehiclelist = vehicledao.getAllVehicles();
        DefaultTableModel tableModel = (DefaultTableModel) vehicleView.getVehicleTable().getModel();
        
        tableModel.setRowCount(0);  // Clear old rows
        
        for (VehicleData v : vehiclelist) {
            Object[] row = {
                v.getVehicleNumber(),
                v.getVehicleType(),
                v.getSeatCount(),
                v.getDriverName(),
                "Edit | Delete"
            };
            
            tableModel.addRow(row);
        }
    }
}