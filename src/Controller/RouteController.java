/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.RouteDAO;
import Model.Route;
import View.RouteManagement;
import View.VehicleManagement;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nitro V 16
 */
public class RouteController {
    RouteDAO dao = new RouteDAO();
    public void addRoute(String name, String origin, String destination, int duration, double fare) {
        Route route = new Route(name, origin, destination, duration, fare);
        dao.addRoute(route);
    }

    public void openVehicleManagement() {
        VehicleManagement vm = new VehicleManagement();
        vm.setVisible(true);
    }
    
    public void closeRouteManagement(RouteManagement rm) {
        rm.dispose();
    }
    
    public void loadRoutes(RouteManagement view) {
        RouteDAO dao = new RouteDAO();
        ResultSet rs = dao.getAllRoutes();
        
        DefaultTableModel model = (DefaultTableModel) view.getRouteTable().getModel();
        model.setRowCount(0);
        
        try {
            while (rs != null && rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("Route_ID"),
                    rs.getString("route_name"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getDouble("fare"),
                    rs.getInt("duration"),
                    "Edit/Delete"
                });
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void deleteRoute(int id) {
        dao.deleteRoute(id);
    }
    
    public void updateRoute(int id, String name, String origin, String destination, int duration, double fare) {
        dao.updateRoute(new Route(id, name, origin, destination, duration, fare));
    }
}