package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlightdetailsDTO;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
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

    boolean cancleFlight(User user, FlighttransactionDTO flightnumber);

    Flightdetails updateFlight(Flightdetails flightdetails) throws ApplicationException;

    Flightdetails bookFlight(User user, FlightdetailsDTO flightdetails) throws ApplicationException;

    void deleteByAirportId(String airport) throws ApplicationException;

    void deleteById(long flightid) throws ApplicationException;
}
