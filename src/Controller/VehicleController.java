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
import javax.swing.JOptionPane;

/**
 *
 * @author hp
 */
public class VehicleController {
    public final VehicleDao vehicledao = new VehicleDao();
    public final VehicleManagement vehicleView;
    
    public VehicleController(VehicleManagement vehicleView) {
        this.vehicleView = vehicleView;
        
        vehicleView.AddVehicleListener(new AddActionListener());
        vehicleView.CancelVehicleListener(new CancelActionListener());
    }
    
    public void open() {    // Open UI
        this.vehicleView.setVisible(true);
    }
    
    public void close() {   // Close UI
        this.vehicleView.dispose();
    }
    
    class AddActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String number = vehicleView.getVehicleNumber().getText();
                int seat = Integer.parseInt(vehicleView.getSeatCount().getText());  // Convert String from text field to int
                String type = vehicleView.getVehicleType().getSelectedItem().toString();    // Combobox returns object, convert it to string
                int driver = Integer.parseInt(vehicleView.getDriver().getSelectedItem().toString());

                VehicleData vehicledata = new VehicleData(number, seat, type, driver);

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
}