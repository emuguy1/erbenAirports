package de.othr.eerben.erbenairports.backend.data.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class FlighttransactionDTO {


    private  String username;


    private  String password;

    @NotNull
    private String flightnumber;

    @NotNull
    private double flightTimeHours;

    @NotNull
    private double maxCargo;

    @NotNull
    private int passengerCount;

    @NotNull
    private String departureAirport;

    @NotNull
    private String arrivalAirport;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrivalTime;

    private String userenameCreatedFor;


    public FlighttransactionDTO() {}

    public FlighttransactionDTO(String username, String password, String flightnumber, double flightTimeHours, double maxCargo, int passengerCount, String departureAirport, String arrivalAirport, LocalDateTime departureTime, LocalDateTime arrivalTime, String userenameCreatedFor) {
        this.username = username;
        this.password = password;
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passengerCount = passengerCount;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.userenameCreatedFor = userenameCreatedFor;
    }

    public FlighttransactionDTO(String flightnumber, double flightTimeHours, double maxCargo, int passengerCount, String departureAirport, String arrivalAirport, LocalDateTime departureTime, String userenameCreatedFor) {
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passengerCount = passengerCount;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.userenameCreatedFor = userenameCreatedFor;
    }

    public FlighttransactionDTO(String flightnumber, double flightTimeHours, double maxCargo, int passengerCount, String departureAirport, String arrivalAirport, LocalDateTime departureTime) {
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passengerCount = passengerCount;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
    }

    public FlighttransactionDTO(String username, String password, String flightnumber, double flightTimeHours, double maxCargo, int passengerCount, String departureAirport, String arrivalAirport, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.username = username;
        this.password = password;
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passengerCount = passengerCount;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }


    public FlighttransactionDTO(String username, String password, String flightnumber, double flightTimeHours, double maxCargo, int passengerCount, String departureAirport, String arrivalAirport, LocalDateTime departureTime) {
        this.username = username;
        this.password = password;
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passengerCount = passengerCount;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getUserenameCreatedFor() {
        return userenameCreatedFor;
    }

    public void setUserenameCreatedFor(String userenameCreatedFor) {
        this.userenameCreatedFor = userenameCreatedFor;
    }
}
