/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.BookingDAO;
import DAO.SearchDao;
import Model.Booking;
import Model.SearchData;
import View.ConfirmBooking;
import View.MyBooking;
import View.Profile;
import View.SearchTrips;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Nitro V 16
 */
public class BookingController {
    private MyBooking bookingView;
    private ConfirmBooking confirmBookingView;
    private final BookingDAO bookingDAO;
    public final SearchDao searchdao = new SearchDao();
    private SearchController searchController;
    private int userId; 
    
    public BookingController(int userId, SearchController searchController) {
        this.userId = userId;
        this.searchController = searchController;
        this.bookingDAO = new BookingDAO();
    }
    
    public BookingController(MyBooking bookingView, int userId) {
        this.bookingView = bookingView;
        this.userId = userId;
        this.bookingDAO = new BookingDAO();
        
        bookingView.AllListener(new AllListener());
        bookingView.PendingListener(new PendingListener());
        bookingView.BoardedListener(new BoardedListener());
        bookingView.CompletedListener(new CompletedListener());
        bookingView.SearchTripsListener(new SearchListener());
        bookingView.ProfileListener(new ProfileListener());

    }
    
    public void openConfirmBooking(int tripId) {
        SearchData trip = searchdao.getTripById(tripId);

        if (trip == null) {
            JOptionPane.showMessageDialog(null, "Trip not found!");
            return;
        }

        confirmBookingView = new ConfirmBooking(
            tripId,
            trip.getVehicleType(),
            trip.getVehicleNum(),
            trip.getDestination(),
            trip.getDepartureTime()
        );
        confirmBookingView.getSeatComboBox().addActionListener(new SeatSelectionListener());
        confirmBookingView.getConfirmButton().addActionListener(new ConfirmBookingListener());
        confirmBookingView.setTotalFare(trip.getFare());
        confirmBookingView.setVisible(true);
    }

    class SeatSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int seats = confirmBookingView.getSelectedSeats();
            SearchData trip = searchdao.getTripById(confirmBookingView.getTripId());
            double totalFare = trip.getFare() * seats;
            confirmBookingView.setTotalFare(totalFare);
        }
    }

    class ConfirmBookingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int tripId = confirmBookingView.getTripId();
            int seats = confirmBookingView.getSelectedSeats();
            System.out.println("Confirm clicked");

            try {
                SearchData trip = searchdao.getTripById(tripId);
                
                if (trip == null) {
                    JOptionPane.showMessageDialog(confirmBookingView, "Trip not found!");
                    return;
                }
                
                if (trip.getAvailableSeats() < seats) {
                    JOptionPane.showMessageDialog(confirmBookingView, "Not enough seats available!");
                    return;
                }
                double totalFare = trip.getFare() * seats;

                Booking booking = new Booking();
                booking.setUserId(userId);
                booking.setTripId(tripId);
                booking.setNumberOfSeats(seats);
                booking.setTotalFare(totalFare);
                booking.setStatus("Pending");

                String token = "BKT-" + tripId + "-" + System.currentTimeMillis();
                booking.setBookingToken(token);

                boolean saved = bookingDAO.createBooking(booking);

                if (!saved) {
                    JOptionPane.showMessageDialog(confirmBookingView, "Booking failed. Please try again.",
                                                  "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                searchdao.updateAvailableSeats(tripId, seats);  // Reduce seats
                searchController.loadSearchTable("");   // Reload table with updates seats
                JOptionPane.showMessageDialog(confirmBookingView, "Booking successful!\nToken: " + token);
                confirmBookingView.dispose();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(confirmBookingView, "Unexpected error occurred.");
            }
        }
    }
    
    public void openMyBooking() {    // Open UI
        this.bookingView.setVisible(true);
        
        loadBookings("All");    // Show all bookings
    }
    
    public void closeMyBooking() {   // Close UI
        this.bookingView.dispose();
    }
    
    private void loadBookings(String status) {
       List<Booking> bookinglist;
       if (status.equals("All")) {
           bookinglist = bookingDAO.getUserBookings(userId);
       }
       else {
           bookinglist = bookingDAO.getUserBookingsByStatus(userId, status);           
       }
        DefaultTableModel tableModel = (DefaultTableModel) bookingView.getResultTable().getModel();
        tableModel.setRowCount(0);  // Clear old rows
        
        if (bookinglist.isEmpty()) {
            bookingView.getNoData().setVisible(true);  // Show "no trips found"
        }
        else {
            bookingView.getNoData().setVisible(false); // Hide label
            
            for (Booking b : bookinglist) {
                Object[] row = {
                    b.getOrigin(),
                    b.getDestination(),
                    b.getTotalFare(),
                    b.getBookingToken(),
                    b.getStatus()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    // Filter Buttons
    class AllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadBookings("All");
        }
    }
    
    class PendingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadBookings("Pending");
        }
    }
    
    class BoardedListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadBookings("Boarded");
        }
    }
    
    class CompletedListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadBookings("Completed");
        }
    }
    
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SearchTrips st = new SearchTrips(); // Create view
            SearchController sc = new SearchController(st, userId); // Create controller
            sc.openSearchTrips();   // Open Search Trips page
            closeMyBooking();  // Close My Bookings page
        }
    }
    
        // Open Profile
    class ProfileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Profile p = new Profile();  // Create view
            ProfileController pc = new ProfileController(p, userId);    // Create controller and passes logged-in user's ID
            pc.openProfile();    // Open Profile page
            closeMyBooking();   // Close My Bookings page
        }
    }
}