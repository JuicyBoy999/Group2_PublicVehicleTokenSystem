/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.BookingDAO;
import DAO.TripDao;
import Model.Booking;
import Model.TripData;
import View.ConfirmBooking;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


/**
 *
 * @author Nitro V 16
 */
public class BookingController {
     private ConfirmBooking confirmBookingView;
    private BookingDAO bookingDAO;
    private TripDao tripDAO;
    private int userId; 
    
    public BookingController(int userId) {
        this.userId = userId;
        this.bookingDAO = new BookingDAO();
        this.tripDAO = new TripDao();
    }
    
public void openConfirmBooking(int tripId) {
    TripData trip = tripDAO.getTripById(tripId);

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

    confirmBookingView.getSeatComboBox()
        .addActionListener(new SeatSelectionListener());

    confirmBookingView.getConfirmButton()
        .addActionListener(new ConfirmBookingListener());

    confirmBookingView.setVisible(true);
}

class SeatSelectionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int seats = confirmBookingView.getSelectedSeats();
        double farePerSeat = tripDAO.getFareByTripId(confirmBookingView.getTripId());
        double totalFare = farePerSeat * seats;
        confirmBookingView.setTotalFare(totalFare);
        System.out.println("Seat changed");
    }
}


class ConfirmBookingListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        int tripId = confirmBookingView.getTripId();
        int seats = confirmBookingView.getSelectedSeats();
        System.out.println("Confirm clicked");

        try {
            if (!tripDAO.checkSeatAvailability(tripId, seats)) {
                JOptionPane.showMessageDialog(
                    confirmBookingView,
                    "Not enough seats available!",
                    "Booking Failed",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            double farePerSeat = tripDAO.getFareByTripId(tripId);
            double totalFare = farePerSeat * seats;

            Booking booking = new Booking();
            booking.setUserId(userId);
            booking.setTripId(tripId);
            booking.setNumberOfSeats(seats);
            booking.setTotalFare(totalFare);
            booking.setStatus("PENDING");

            String token = "BKT-" + tripId + "-" + System.currentTimeMillis();
            booking.setBookingToken(token);

            boolean saved = bookingDAO.createBooking(booking);

            if (!saved) {
                JOptionPane.showMessageDialog(
                    confirmBookingView,
                    "Booking failed. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            tripDAO.updateAvailableSeats(tripId, seats);

            JOptionPane.showMessageDialog(
                confirmBookingView,
                "Booking successful!\nToken: " + token,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            confirmBookingView.dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                confirmBookingView,
                "Unexpected error occurred.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

}
