/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.Profile;

/**
 *
 * @author hp
 */
public class ProfileController {
    private final Profile profileView;
    private final int userId; // Track which user is logged in

    public ProfileController(Profile profileView, int userId) {
        this.profileView = profileView;
        this.userId = userId;
    }
    
    public void openProfile() {    // Open UI
        this.profileView.setVisible(true);
    }
    
    public void closeSearchTrips() {   // Close UI 
        this.profileView.dispose();
    }
}
