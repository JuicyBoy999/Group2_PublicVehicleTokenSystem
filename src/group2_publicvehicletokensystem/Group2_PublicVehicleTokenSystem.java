/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package group2_publicvehicletokensystem;

import Controller.SearchController;
import Controller.TripController;
import Controller.UserController;
import Database.Database;
import Database.MySqlConnection;
import View.SearchTrips;
import View.Signup;
import View.TripManagement;

/**
 *
 * @author Nitro V 16
 */
public class Group2_PublicVehicleTokenSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Database db = new MySqlConnection();
        if (db.openConnection() != null) {
            System.out.println("Connection successful");
        }
        else {
            System.out.println("Not successful");
        }
        Signup signup = new Signup();
        UserController usercontroller = new UserController(signup);

        usercontroller.open();
        
            SearchTrips view = new SearchTrips();
            SearchController controller = new SearchController(view);
            controller.openSearchTrips();
            
            TripManagement viewtrip = new TripManagement();
            TripController controllertrip = new TripController(viewtrip);
            controllertrip.openTripManagement();
    }
}
