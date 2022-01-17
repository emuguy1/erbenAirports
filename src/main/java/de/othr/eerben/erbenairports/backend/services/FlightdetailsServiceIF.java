package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlightdetailsDTO;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Optional;

public interface FlightdetailsServiceIF {

    Page<Flightdetails> getDeparturesPaginated(String airportcode, Pageable pageable) throws AirportException;

    Page<Flightdetails> getArrivalsPaginated(String airportcode, Pageable pageable) throws AirportException;

    Collection<Flightdetails> getFlightdetails(String flightnumber);

    Optional<Flightdetails> getFlightdetailsById(long flightid);

    boolean cancelFlight(User user, FlighttransactionDTO flightnumber) throws AirportException;

    Flightdetails updateFlight(Flightdetails flightdetails) throws AirportException;

    Flightdetails bookFlight(User user, FlightdetailsDTO flightdetails) throws AirportException;

    void deleteByAirportId(String airport) throws AirportException;

    void deleteById(long flightid) throws AirportException;
}
