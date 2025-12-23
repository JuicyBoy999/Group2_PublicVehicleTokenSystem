/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.SearchDao;
import Model.SearchData;
import View.SearchTrips;
import View.ConfirmBooking;
import View.MyBooking;
import View.RouteManagement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hp
 */
public class SearchController {
    public final SearchDao searchdao = new SearchDao();
    public final SearchTrips searchView;
    
    private Timer refresh;
    private String currentVehicleType = "All"; // Tracks which vehicle to show
    
    public SearchController(SearchTrips searchView) {   // Constructor
        this.searchView = searchView;
        
        searchView.SearchListener(new SearchListener());
        searchView.AllListener(new AllListener());
        searchView.BusListener(new BusListener());
        searchView.MicroListener(new MicroListener());
        searchView.TempoListener(new TempoListener());
        
        // Mouse listener for Book column
        searchView.getResultTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = searchView.getResultTable().rowAtPoint(evt.getPoint());
                int col = searchView.getResultTable().columnAtPoint(evt.getPoint());

                if (col == 9 && row != -1) { // Book column index
                    DefaultTableModel model = (DefaultTableModel) searchView.getResultTable().getModel();

                    // Retrieve data from the selected row
                    int tripId = (int) model.getValueAt(row, 0);
                    String vNumber = (String) model.getValueAt(row, 1);
                    String vType = (String) model.getValueAt(row, 2);
                    String dest = (String) model.getValueAt(row, 4);
                    String deptTime = (String) model.getValueAt(row, 5);
                    
                    Object fareObj = model.getValueAt(row, 7);
                    double fare = 0.0;
                    if (fareObj instanceof Double) {
                        fare = (Double) fareObj;
                    } else if (fareObj instanceof String) {
                        try {
                            fare = Double.parseDouble((String) fareObj);
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing fare: " + e.getMessage());
                        }
                    } else if (fareObj instanceof Integer) {
                        fare = ((Integer) fareObj).doubleValue();
                    }

                    // Open ConfirmBooking frame
                    ConfirmBooking confirmBooking = new ConfirmBooking(tripId, vType, vNumber, dest, deptTime);
                    confirmBooking.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); 
                    confirmBooking.setVisible(true);
                }
            }
        });
        loadSearchTable("");  // Load all trips by default
        
        // Refresh every 10 seconds (10000 ms)
        refresh = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dest = searchView.getDestination().getText().trim();
                if (dest.isEmpty() || dest.equals("Enter Destination")) {
                    dest = "";
                }
                loadSearchTable(dest);
            }
        });
        
        refresh.start();
    }
    
    public void openSearchTrips() {    // Open UI
        this.searchView.setVisible(true);
        
        loadSearchTable("");    // Show all trips
    }
    
    public void closeSearchTrips() {   // Close UI
        // Stop timer when closing the window
        if (refresh != null && refresh.isRunning()) {
            refresh.stop();
        }
        
        this.searchView.dispose();
    }
    
    // Search Button
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentVehicleType = "All"; // Load all trips when clicking search
            String dest = searchView.getDestination().getText().trim();
            // If field is empty or has placeholder, treat as empty
            if (dest.isEmpty() || dest.equals("Enter Destination")) {
                dest = "";
            }
                
            loadSearchTable(dest);
        }
    }

    // Vehicle Filter Buttons
    class AllListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentVehicleType = "All";
            String dest = searchView.getDestination().getText().trim();
            if (dest.isEmpty() || dest.equals("Enter Destination")) {
                dest = "";
            }
                
            loadSearchTable(dest);
        }
    }

    class BusListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentVehicleType = "Bus";
            String dest = searchView.getDestination().getText().trim();
            if (dest.isEmpty() || dest.equals("Enter Destination")) {
                dest = "";
            }
                
            loadSearchTable(dest);
        }
    }

    class MicroListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentVehicleType = "Micro Bus";
            String dest = searchView.getDestination().getText().trim();
            if (dest.isEmpty() || dest.equals("Enter Destination")) {
                dest = "";
            }
                
            loadSearchTable(dest);
        }
    }

    class TempoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentVehicleType = "Tempo";
            String dest = searchView.getDestination().getText().trim();
            if (dest.isEmpty() || dest.equals("Enter Destination")) {
                dest = "";
            }
                
            loadSearchTable(dest);
        }
    }
    
    
    // Add results in JTable from database
    private void loadSearchTable(String destination) {
        
        ArrayList<SearchData> searchlist = searchdao.searchTrips(destination, currentVehicleType);
        DefaultTableModel tableModel = (DefaultTableModel) searchView.getResultTable().getModel();

        tableModel.setRowCount(0);  // Clear old rows
        
        if (searchlist.isEmpty()) {
            searchView.getNoData().setVisible(true);  // Show "no trips found"
        }
        else {
            searchView.getNoData().setVisible(false); // Hide label
            
            for (SearchData s : searchlist) {
                Object[] row = {
                    s.getTripId(),     // Hidden ID column
                    s.getVehicleNum(),
                    s.getVehicleType(),
                    s.getOrigin(),
                    s.getDestination(),
                    s.getDepartureTime(),
                    s.getArrivalTime(),
                    s.getFare(),
                    s.getAvailableSeats(),
                    "Book Now"
                };

                tableModel.addRow(row);
            }
        }
    }
}