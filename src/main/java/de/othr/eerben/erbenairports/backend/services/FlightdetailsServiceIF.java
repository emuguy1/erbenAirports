package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface FlightdetailsServiceIF {

    Collection<Flightdetails> getDepartures(String airportcode);

    Page<Flightdetails> getDeparturesPaginated(String airportcode, Pageable pageable);

    Collection<Flightdetails> getArrivals(String airportcode);

    Flightdetails getFlightdetails(String flightnumber);

    boolean cancleFlight(User user, String flightnumber);

    Flightdetails bookFlight(User user, Flightdetails flightdetails);

}
