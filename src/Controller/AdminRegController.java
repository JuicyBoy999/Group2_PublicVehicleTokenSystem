/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.userDAO;
import Model.userData;
import View.AdminRegistration;
import View.UserManagement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class AdminRegController {
    private final userDAO userdao = new userDAO();
    private final AdminRegistration adminView;
    private final UserManagement parentView;
    
    public AdminRegController(AdminRegistration adminView, UserManagement parentView) {
        this.adminView =  adminView;
        this.parentView =  parentView;
        adminView.AddAdminListener(new AddActionListener());
    }

    public void openAdminReg() {
        this.adminView.setVisible(true);
    }

    class AddActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ex) {
            try {
                String phone = adminView.getphone().getText();
                String email = adminView.getemail().getText();
                String name = adminView.getname().getText();
                String password = adminView.getpassword().getText();
                String address = adminView.getaddress().getText();

                if (name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty() || 
                    address.trim().isEmpty() || password.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(adminView, "Please fill all fields!");
                    return;
                }

                userData admin = new userData(phone, email, name, password, address, "Admin");

                boolean exists = userdao.checkUser(admin);
                if (exists) {
                    JOptionPane.showMessageDialog(adminView, "Admin already exists.");
                }
                else {
                    userdao.signUp(admin);
                    JOptionPane.showMessageDialog(adminView, "New admin created successfully!");
                    parentView.loadAllUsers();
                    adminView.dispose();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(adminView, "Error: " + e.getMessage());
            }
        }
    }
}