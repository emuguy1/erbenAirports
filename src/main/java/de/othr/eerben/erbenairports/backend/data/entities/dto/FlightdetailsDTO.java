package de.othr.eerben.erbenairports.backend.data.entities.dto;

import com.sun.istack.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.Date;

public class FlightdetailsDTO {

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

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureTime;


    public FlightdetailsDTO() {
    }

    public FlightdetailsDTO(String flightnumber, double flightTimeHours, double maxCargo, int passangerCount, String departure, String origin, LocalDateTime departureTime) {
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

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }
}
