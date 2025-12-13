package Controller;

import DAO.OTPDAO;
import DAO.userDAO;
import View.Email;
import View.EmailOTP;
import View.ResetPassword;
import View.Login;
import utils.OTPService;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utils.EmailService;

public class ForgotPasswordController {
    private Email emailView;
    private EmailOTP otpView;
    private ResetPassword resetView;
    private OTPDAO otpDao = new OTPDAO();
    private userDAO userDao = new userDAO();
    
    public ForgotPasswordController(Email view) {
        this.emailView = view;
        emailView.addSendOTPListener(new SendOTPListener());
        emailView.addBackListener(new BackListener());
    }
    
    public void open() {
        emailView.setVisible(true);
    }
    
    class SendOTPListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailView.getEmail();
            
            if (email.isEmpty() || email.equals("Enter email")) {
                JOptionPane.showMessageDialog(emailView, "Please enter your email");
                return;
            }
            
            if (!otpDao.emailExists(email)) {
                JOptionPane.showMessageDialog(emailView, "Email not found in our system");
                return;
            }
            
            String otp = OTPService.generateOTP();
            java.util.Date expiryTime = OTPService.calculateExpiryTime();
            
            if (otpDao.storeOTP(email, otp, expiryTime)) {
                boolean emailSent = EmailService.sendOTPEmail(email, otp);
                
                if (emailSent) {
                    JOptionPane.showMessageDialog(emailView, "OTP sent to your email: ");
                    
                    emailView.setVisible(false);
                    emailView.dispose();
                    
                    otpView = new EmailOTP(email);
                    otpView.addVerifyListener(new VerifyOTPListener(email));
                    otpView.addResendListener(new ResendOTPListener(email));
                    otpView.setVisible(true);
                    
                } else {
                    JOptionPane.showMessageDialog(emailView, "Failed to send email. Check email configuration.");
                }
            } else {
                JOptionPane.showMessageDialog(emailView, "Failed to generate OTP");
            }
        }
    }
    
    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            emailView.dispose();
            new LoginController(new Login()).open();
        }
    }
    
    class VerifyOTPListener implements ActionListener {
        private String email;
        
        public VerifyOTPListener(String email) {
            this.email = email;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredOTP = otpView.getOTP();
            
            if (enteredOTP.isEmpty() || enteredOTP.equals("Enter OTP") || enteredOTP.length() != 6) {
                JOptionPane.showMessageDialog(otpView, "Please enter a valid 6-digit OTP");
                return;
            }
            
            if (otpDao.verifyOTP(email, enteredOTP)) {
                otpView.setVisible(false);
                otpView.dispose();
                
                resetView = new ResetPassword(email);
                resetView.addResetListener(new ResetPasswordListener(email));
                resetView.setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(otpView, "Invalid or expired OTP");
            }
        }
    }
    
    class ResendOTPListener implements ActionListener {
        private String email;
        
        public ResendOTPListener(String email) {
            this.email = email;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String newOTP = OTPService.generateOTP();
            java.util.Date newExpiry = OTPService.calculateExpiryTime();
            
            if (otpDao.storeOTP(email, newOTP, newExpiry)) {
                JOptionPane.showMessageDialog(otpView, "New OTP sent");
            }
        }
    }
    
    class ResetPasswordListener implements ActionListener {
        private String email;
        
        public ResetPasswordListener(String email) {
            this.email = email;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String newPassword = resetView.getNewPassword();
            String confirmPassword = resetView.getConfirmPassword();
            
            if (newPassword.isEmpty() || newPassword.equals("Enter Password") ||
                confirmPassword.isEmpty() || confirmPassword.equals("Enter Password")) {
                JOptionPane.showMessageDialog(resetView, "Please enter both passwords");
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(resetView, "Passwords don't match");
                return;
            }
            
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(resetView, "Password must be at least 6 characters");
                return;
            }
            
            if (userDao.updatePassword(email, newPassword)) {
                otpDao.clearOTP(email); 
                JOptionPane.showMessageDialog(resetView, "Password reset successful!");
                
                resetView.setVisible(false);
                resetView.dispose();
                
                new LoginController(new Login()).open();
                
            } else {
                JOptionPane.showMessageDialog(resetView, "Failed to reset password");
            }
        }
    }
}