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
    private int passangerCount;

    @NotNull
    @ManyToOne
    private Airport departure;

    @NotNull
    @ManyToOne
    private Airport origin;

    @NotNull
    @ManyToOne
    private User customer;

    @ManyToOne
    private User createdBy;

    @OneToOne //(mappedBy = "de")
    private BookedCalendarslot departureTime;

    @OneToOne //(mappedBy = "flightdetails")
    private BookedCalendarslot arrivalTime;

    public Flightdetails(){}

    public Flightdetails(String flightnumber, double flightTimeHours, double maxCargo, int passangerCount, Airport departure, Airport origin) {
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passangerCount = passangerCount;
        this.departure = departure;
        this.origin = origin;
    }

    public Flightdetails(String flightnumber, double flightTimeHours, double maxCargo, int passangerCount, Airport departure, Airport origin, BookedCalendarslot departureTime, BookedCalendarslot arrivalTime) {
        this.flightnumber = flightnumber;
        this.flightTimeHours = flightTimeHours;
        this.maxCargo = maxCargo;
        this.passangerCount = passangerCount;
        this.departure = departure;
        this.origin = origin;
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

    public int getPassangerCount() {
        return passangerCount;
    }

    public void setPassangerCount(int passangerCount) {
        this.passangerCount = passangerCount;
    }

    public Airport getDeparture() {
        return departure;
    }

    public void setDeparture(Airport departure) {
        this.departure = departure;
    }

    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
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

    public long getFlightid() {return flightid;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flightdetails)) return false;
        Flightdetails that = (Flightdetails) o;
        return getFlightid() == that.getFlightid() && Double.compare(that.getFlightTimeHours(), getFlightTimeHours()) == 0 && Double.compare(that.getMaxCargo(), getMaxCargo()) == 0 && getPassangerCount() == that.getPassangerCount() && Objects.equals(getFlightnumber(), that.getFlightnumber()) && Objects.equals(getDeparture(), that.getDeparture()) && Objects.equals(getOrigin(), that.getOrigin()) && Objects.equals(getCustomer(), that.getCustomer()) && Objects.equals(getCreatedBy(), that.getCreatedBy()) && Objects.equals(getDepartureTime(), that.getDepartureTime()) && Objects.equals(getArrivalTime(), that.getArrivalTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFlightid(), getFlightnumber(), getFlightTimeHours(), getMaxCargo(), getPassangerCount(), getDeparture(), getOrigin(), getCustomer(), getCreatedBy(), getDepartureTime(), getArrivalTime());
    }

    @Override
    public String toString() {
        return "Flightdetails{" +
                "flightid=" + flightid +
                ", flightnumber='" + flightnumber + '\'' +
                ", flightTimeHours=" + flightTimeHours +
                ", maxCargo=" + maxCargo +
                ", passangerCount=" + passangerCount +
                ", departure=" + departure +
                ", origin=" + origin +
                ", customer=" + customer +
                ", createdBy=" + createdBy +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}
