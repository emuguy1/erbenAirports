package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlightdetailsDTO;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

public interface FlightdetailsServiceIF {

    Page<Flightdetails> getDeparturesPaginated(String airportcode, Pageable pageable) throws ApplicationException;

    Page<Flightdetails> getArrivalsPaginated(String airportcode, Pageable pageable) throws ApplicationException;

    Collection<Flightdetails> getFlightdetails(String flightnumber);

    Optional<Flightdetails> getFlightdetailsById(long flightid);

    boolean cancleFlight(User user, String flightnumber);

    Flightdetails bookFlight(User user, Flightdetails flightdetails);

    Flightdetails bookFlight(FlightdetailsDTO flightdetails) throws ApplicationException;
}
