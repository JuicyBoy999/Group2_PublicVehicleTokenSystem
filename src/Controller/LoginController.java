/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.LoginDao;
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
class LoginController {
     private final LoginDao logindao = new LoginDao();
     private final Login loginView;

    public LoginController(Login loginView) {
        this.loginView = loginView;

        loginView.AddLoginListener(new  LoginListner());
//        loginView.AddRegisterListner(new RegisterListener());
//        loginView.AddForgotPasswordListener(new ForgotPasswordListener());
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
                String Password = loginView.getpassword().getText();
                userData userdata = new userData(email, Password);
                boolean check = logindao.login(userdata);

                if (check) {
                    JOptionPane.showMessageDialog(loginView, "Login successful");
                } else {
                    JOptionPane.showMessageDialog(loginView, "Invalid credentials");
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
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

//    class ForgotPasswordListener implements ActionListener {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            String email = loginView.getEmail().getText().trim();
//
//            if (email.isEmpty()) {
//                JOptionPane.showMessageDialog(loginView, "Please enter your email first.");
//                return;
//            }
//
//            Reset_Password resetView = new Reset_Password(email);
//            resetView.setVisible(true);
//        }
//    }
}

