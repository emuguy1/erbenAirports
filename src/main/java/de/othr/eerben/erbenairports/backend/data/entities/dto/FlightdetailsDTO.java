package de.othr.eerben.erbenairports.backend.data.entities.dto;

import com.sun.istack.NotNull;

import java.util.Date;

public class FlightdetailsDTO {

    private long flightid;

    @NotNull
    private String flightnumber;

    @NotNull
    private double flightTimeHours;

    @NotNull
    private double maxCargo;

    @NotNull
    private int passangerCount;

    @NotNull
    private String departure;

    @NotNull
    private String origin;

    private Date departureTime;

    private Date arrivalTime;

    public FlightdetailsDTO() {
    }

    public FlightdetailsDTO(String flightnumber, double flightTimeHours, double maxCargo, int passangerCount, String departure, String origin, Date departureTime) {
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passangerCount = passangerCount;
        this.departure = departure;
        this.origin = origin;
        this.departureTime = departureTime;
    }

    public FlightdetailsDTO(String flightnumber, double flightTimeHours, double maxCargo, int passangerCount, String departure, String origin) {
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passangerCount = passangerCount;
        this.departure = departure;
        this.origin = origin;
    }

    public long getFlightid() {
        return flightid;
    }

    public void setFlightid(long flightid) {
        this.flightid = flightid;
    }

    public String getFlightnumber() {
        return flightnumber;
    }

    public void setFlightnumber(String flightnumber) {
        this.flightnumber = flightnumber;
    }

    public double getFlightTimeHours() {
        return flightTimeHours;
    }

    public void setFlightTimeHours(double flightTimeHours) {
        this.flightTimeHours = flightTimeHours;
    }

    public double getMaxCargo() {
        return maxCargo;
    }

    public void setMaxCargo(double maxCargo) {
        this.maxCargo = maxCargo;
    }

    public int getPassangerCount() {
        return passangerCount;
    }

    public void setPassangerCount(int passangerCount) {
        this.passangerCount = passangerCount;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
