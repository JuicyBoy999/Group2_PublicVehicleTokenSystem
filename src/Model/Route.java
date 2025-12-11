/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Nitro V 16
 */

public class Route {
    private int Route_ID;
    private String routeName;
    private String origin;
    private String destination;
    private int duration;
    private double fare;

    public Route(String routeName, String origin, String destination, int duration, double fare) {
        this.routeName = routeName;
        this.origin = origin;
        this.destination = destination;
        this.duration = duration;
        this.fare = fare;
    }

    public Route(int id, String routeName, String origin, String destination, int duration, double fare) {
        this(routeName, origin, destination, duration, fare);
        this.Route_ID = id;
    }

    public void setRouteID(int Route_ID){
        this.Route_ID = Route_ID;
    }
    public int getRoute_ID(){
        return Route_ID;
    }
    public void setRouteName(String routeName){
        this.routeName = routeName;
    }
    public String getRouteName(){
        return routeName;
    }
    public void setOrigin(String origin){
        this.origin = origin;
    }
    public String getOrigin(){
        return origin;
    }
    public void setDestination(String destination){
        this.destination = destination;
    }
    public String getDestination(){
        return destination;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }
    public int getDuration(){
        return duration;
    }
    public void setFare(double fare){
        this.fare = fare;
    }
    public double getFare(){
        return fare;
    }
}