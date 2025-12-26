/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.userDAO;
import Model.userData;
import View.Login;
import View.Signup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class UserController {
     private final userDAO userdao = new userDAO();
    private final Signup userView;
    
    public UserController(Signup userView) {
        this.userView =  userView;

        userView.AddAAUserListener(new AddUserActionListener());
        userView.SignIn(new LoginListener() );
    }

    public void open() {
        this.userView.setVisible(true);
    }

    public void close() {
        this.userView.dispose();
    }


     class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ex) {
            Login log = new Login();
            LoginController login = new LoginController(log);
            login.open();
            close();
        }
    }

class AddUserActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent ex) {
        try {
            String phone = userView.getphone().getText();
            String email = userView.getemail().getText();
            String name = userView.getname().getText();
            String password = userView.getpassword().getText();
            String address = userView.getaddress().getText();
            String role = (String) userView.getrole().getSelectedItem();
            
            if (name.trim().isEmpty() || email.trim().isEmpty() || 
                phone.trim().isEmpty() || address.trim().isEmpty() || 
                password.trim().isEmpty() || role.equals("Select Your Role")) {
                JOptionPane.showMessageDialog(userView, 
                    "Please fill all fields and select a role!");
                return;
            }
            
            userData userdata = new userData(phone, email, name, password, address, role);
            
            boolean exists = userdao.checkUser(userdata);
            if (exists) {
                JOptionPane.showMessageDialog(userView,
                      "User already exists with this email or phone number.");
            } else {
                userdao.signUp(userdata);
                JOptionPane.showMessageDialog(userView,
                        "Registration successful! Please log in.");
                
                Login loginView = new Login();
                LoginController loginController = new LoginController(loginView);
                
                close();
                loginController.open();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(userView, "Error: " + e.getMessage());
        }
    }
}
}