package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.UserData;

import java.util.Collection;

public interface FlightdetailsService {

    Collection<Flightdetails> getDepartures(String airportcode);

    Collection<Flightdetails> getArrivals(String airportcode);

    Flightdetails getFlightdetails(String flightnumber);

    boolean cancleFlight(UserData user, String flightnumber);

    Flightdetails bookFlight(UserData user, Flightdetails flightdetails);

}
