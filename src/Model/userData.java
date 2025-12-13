/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author user
 */
public class userData {
    private int user_id;
    private String phone;
    private String email;
    private String name;
    private String password;
    private String address;
    private String role; 
    private String resetOtp;
    private java.util.Date otpExpiry;
    
    public userData(String resetOtp, java.util.Date otpExpiry){
        this.resetOtp = resetOtp;
        this.otpExpiry = otpExpiry;
    }
    
    public userData(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public userData(String phone, String email, String name, String password, String address, String role) {
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.role = role;
    }
    
    public String getResetOtp() {return resetOtp;}
    public void setResetOtp(String resetOtp) {this.resetOtp = resetOtp;}
    
    public java.util.Date getOtpExpiry() {return otpExpiry;}
    public void setOtpExpiry(java.util.Date otpExpiry) {this.otpExpiry = otpExpiry;}
    
    public void setname(String name){this.name=name;}
    public String getname(){return name;}
    
    public void setemail(String email){this.email=email;}
    public String getemail(){return email;}
    
    public void setPassword(String password){this.password=password;}
    public String getPassword(){return password;}
    
    public void setphone(String phone){this.phone=phone;}
    public String getphone(){return phone;}
    
    public void setaddress(String address){this.address=address;}
    public String getaddress(){return address;}
    
    public void setId(int user_id){this.user_id=user_id;}
    public int getId(){return user_id;}
    
    public void setrole(String role){this.role=role;}
    public String getrole(){return role;}
}
