package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
public class Flightdetails {

    @Id
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
    private Customer customer;

    @ManyToOne
    private Employee createdBy;

    @OneToOne(mappedBy = "calendarId")
    private Calendarslot departureTime;

    @OneToOne(mappedBy = "calendarId")
    private Calendarslot arrivalTime;

    public Flightdetails(){}

    public Flightdetails(String flightnumber, double flightTimeHours, double maxCargo, int passangerCount, Airport departure, Airport origin) {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public Calendarslot getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Calendarslot departureTime) {
        this.departureTime = departureTime;
    }

    public Calendarslot getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Calendarslot arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flightdetails)) return false;
        Flightdetails that = (Flightdetails) o;
        return Double.compare(that.getFlightTimeHours(), getFlightTimeHours()) == 0 && Double.compare(that.getMaxCargo(), getMaxCargo()) == 0 && getPassangerCount() == that.getPassangerCount() && getFlightnumber().equals(that.getFlightnumber()) && getDeparture().equals(that.getDeparture()) && getOrigin().equals(that.getOrigin()) && getCustomer().equals(that.getCustomer()) && Objects.equals(getCreatedBy(), that.getCreatedBy()) && Objects.equals(getDepartureTime(), that.getDepartureTime()) && Objects.equals(getArrivalTime(), that.getArrivalTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFlightnumber(), getFlightTimeHours(), getMaxCargo(), getPassangerCount(), getDeparture(), getOrigin(), getCustomer(), getCreatedBy(), getDepartureTime(), getArrivalTime());
    }

    @Override
    public String toString() {
        return "Flightdetails{" +
                "flightnumber='" + flightnumber + '\'' +
                ", flightTimeHours=" + flightTimeHours +
                ", maxCargo=" + maxCargo +
                ", passangerCount=" + passangerCount +
                ", departure=" + departure +
                ", origin=" + origin +
                ", customer=" + customer +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}
