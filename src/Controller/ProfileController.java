/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.userDAO;
import Model.userData;
import View.Profile;

/**
 *
 * @author Nitro V 16
 */
public class ProfileController {
    private final Profile profileView;
    private final int userId; // Track which user is logged in

    public ProfileController(Profile profileView, int userId) {
        this.profileView = profileView;
        this.userId = userId;
    }
    
    public void openProfile() {    // Open UI
        userDAO dao = new userDAO();
        userData user = dao.getUserById(userId);
        
        if (user != null) {
            profileView.setUserData(user);
        }
        
        this.profileView.setVisible(true);
    }
    
    public void closeSearchTrips() {   // Close UI 
        this.profileView.dispose();
    }
}
