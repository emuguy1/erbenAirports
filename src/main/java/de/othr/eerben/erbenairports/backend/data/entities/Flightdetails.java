package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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

    @OneToOne
    private Calendarslot departureTime;

    @OneToOne
    private Calendarslot arrivalTime;

    public Flightdetails(){}



}
