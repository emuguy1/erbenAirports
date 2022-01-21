package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AirportServiceIF {
    Airport getAirportByAirportcode(String airportcode) throws AirportException;

    @Transactional
    Airport updateAirport(Airport airport) throws AirportException;

    @Transactional
    void deleteAirport(String airport) throws AirportException;

    Airport addAirport(Airport airport);

    List<Airport> getAllAirports();
}
