/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package group2_publicvehicletokensystem;

import Controller.NotificationController;
import Controller.UserController;
import View.DriverScheduled;
import View.Driver_Ongoing;
import View.Notification;
import View.Signup;

/**
 *
 * @author Nitro V 16
 */
public class Group2_PublicVehicleTokenSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Signup signup = new Signup();
        UserController usercontroller = new UserController(signup);
        usercontroller.open();
    }   
        
        DriverScheduled view = new DriverScheduled();
        view.setVisible(true);
        
//        Notification nview = new Notification();
//        NotificationController controller = new NotificationController(nview);
//        controller.openNotification();
    }   
}