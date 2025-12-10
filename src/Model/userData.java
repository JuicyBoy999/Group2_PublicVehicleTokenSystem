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
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    
    public userData(String name, String password, String email,String phone, String address){
    this.name = name;
    this.password = email;
    this.email = password;
    this.phone = phone;
    this.address = address;
    }
    public userData(String password, String email){
   
    this.password = email;
    this.email = password;
    
    }
    
    public void setname(String name){
        this.name=name;
    }
    public String getname(){
        return name;
    }
    public void setemail(String email){
        this.email=email;
    }
    public String getemail(){
        return email;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getPassword(){
        return password;
    }
    public void setphone(String phone){
        this.phone=phone;
    }
    public String getphone(){
        return phone;
    }
    public void setaddress(String address){
        this.address=address;
    }
    public String getaddress(){
        return address;
    }
    
    public void setId(int user_id){
        this.user_id=user_id;
    }
    public int getId(){
        return user_id;
    }
}
