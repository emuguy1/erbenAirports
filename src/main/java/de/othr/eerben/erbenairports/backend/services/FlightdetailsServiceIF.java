package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightdetailsServiceIF {

    Page<Flightdetails> getDeparturesPaginated(String airportcode, Pageable pageable) throws AirportException;

    Page<Flightdetails> getArrivalsPaginated(String airportcode, Pageable pageable) throws AirportException;

    Flightdetails getFlightdetailsById(long flightid) throws AirportException;

    boolean cancelFlight(User user, FlighttransactionDTO flightnumber) throws AirportException;

    Flightdetails bookFlight(User user, FlighttransactionDTO flightdetails) throws AirportException;

    void deleteByAirportId(String airport) throws AirportException;

    void deleteById(long flightid) throws AirportException;

    Page<Flightdetails> getAllFlights(Pageable pageable);

    Page<Flightdetails> getAllByUsername(String username, Pageable pageable);
}
