package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Flightdetails {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long flightid;

    @NotNull
    private String flightnumber;

    @NotNull
    private double flightTimeHours;

    @NotNull
    private double maxCargo;

    @NotNull
    private int passengerCount;

    @NotNull
    @ManyToOne
    private Airport departureAirport;

    @NotNull
    @ManyToOne
    private Airport arrivalAirport;

    @NotNull
    @ManyToOne
    private User customer;

    @ManyToOne
    private User createdBy;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private BookedCalendarslot departureTime;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private BookedCalendarslot arrivalTime;

    public Flightdetails() {
    }

    public Flightdetails(String flightnumber, double flightTimeHours, double maxCargo, int passengerCount, Airport departureAirport, Airport arrivalAirport) {
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passengerCount = passengerCount;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }

    public Flightdetails(String flightnumber, double flightTimeHours, double maxCargo, int passengerCount, Airport departureAirport, Airport arrivalAirport, BookedCalendarslot departureTime, BookedCalendarslot arrivalTime) {
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passengerCount = passengerCount;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
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

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public BookedCalendarslot getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(BookedCalendarslot departureTime) {
        this.departureTime = departureTime;
    }

    public BookedCalendarslot getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(BookedCalendarslot arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getFlightid() {
        return flightid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flightdetails)) return false;
        Flightdetails that = (Flightdetails) o;
        return getFlightid() == that.getFlightid() && Double.compare(that.getFlightTimeHours(), getFlightTimeHours()) == 0 && Double.compare(that.getMaxCargo(), getMaxCargo()) == 0 && getPassengerCount() == that.getPassengerCount() && getFlightnumber().equals(that.getFlightnumber()) && getDepartureAirport().equals(that.getDepartureAirport()) && getArrivalAirport().equals(that.getArrivalAirport()) && getDepartureTime().equals(that.getDepartureTime()) && getArrivalTime().equals(that.getArrivalTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFlightid(), getFlightnumber(), getFlightTimeHours(), getMaxCargo(), getPassengerCount(), getDepartureAirport(), getArrivalAirport(), getDepartureTime(), getArrivalTime());
    }

    @Override
    public String toString() {
        return "Flightdetails{" +
                "flightid=" + flightid +
                ", flightnumber='" + flightnumber + '\'' +
                ", flightTimeHours=" + flightTimeHours +
                ", maxCargo=" + maxCargo +
                ", passengerCount=" + passengerCount +
                ", departureAirport=" + departureAirport.getAirportcode() +
                ", arrivalAirport=" + arrivalAirport.getAirportcode() +
                ", customer=" + customer +
                ", createdBy=" + createdBy +
                ", departureTime=" + departureTime.getStartTime() +
                ", arrivalTime=" + arrivalTime.getStartTime() +
                '}';
    }
}
