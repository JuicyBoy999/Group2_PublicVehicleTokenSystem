/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.LoginDao;
import Model.userData;
import View.DriverScheduled;
import View.Email;
import View.Login;
import View.SearchTrips;
import View.Signup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
class LoginController {
    private final LoginDao logindao = new LoginDao();
    private final Login loginView;

    public LoginController(Login loginView) {
        this.loginView = loginView;

        loginView.AddLoginListener(new  LoginListner());
        loginView.AddRegisterListener(new RegisterListener());
        loginView.addForgotPasswordListener(new ForgotPasswordListener());
    }

    public void open() {
        this.loginView.setVisible(true);
    }

    public void close() {
        this.loginView.dispose();
    }

class LoginListner implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String email = loginView.getemail().getText();
            String password = loginView.getpassword().getText();
            
            if (email.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(loginView, "Please enter email and password!");
                return;
            }
            
            userData userdata = new userData(email, password);
            String role = logindao.loginAndGetRole(userdata); 
            
            if (role != null) {
                JOptionPane.showMessageDialog(loginView, "Login successful!");
                close();
                
                if (role.equals("Passenger")) {
                    System.out.println("Redirecting to Passenger Dashboard...");
                    int userID = logindao.getUserIdByEmail(email);
                    SearchTrips view = new SearchTrips();
                    SearchController controller = new SearchController(view, userID);
                    controller.openSearchTrips();
                } else if (role.equals("Driver")) {
                    System.out.println("Redirecting to Driver Dashboard...");
                    
                    DriverScheduled driverView = new DriverScheduled();
                    // DriverController driverController = new DriverController(driverView);
                    // driverController.open();
                    driverView.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(loginView, "Invalid credentials");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            JOptionPane.showMessageDialog(loginView, "Error: " + ex.getMessage());
        }
    }
}

    class RegisterListener implements ActionListener {
          @Override
        public void actionPerformed(ActionEvent e) {
            Signup SignupView = new Signup();
            UserController signUpController = new UserController(SignupView);

            close();
            signUpController.open();
        }
    }

    class ForgotPasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Email forgotView = new Email();
            ForgotPasswordController forgotController = new ForgotPasswordController(forgotView);
            
            close(); 
            forgotController.open(); 
        }
    }
}