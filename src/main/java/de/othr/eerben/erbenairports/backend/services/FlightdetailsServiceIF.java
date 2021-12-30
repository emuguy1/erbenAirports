package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.UserData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface FlightdetailsServiceIF {

    Collection<Flightdetails> getDepartures(String airportcode);

    Page<Flightdetails> getDeparturesPaginated(String airportcode, Pageable pageable);

    Collection<Flightdetails> getArrivals(String airportcode);

    Flightdetails getFlightdetails(String flightnumber);

    boolean cancleFlight(UserData user, String flightnumber);

    Flightdetails bookFlight(UserData user, Flightdetails flightdetails);

}
