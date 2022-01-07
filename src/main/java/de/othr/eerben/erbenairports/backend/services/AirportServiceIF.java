package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

public interface AirportServiceIF {
    Airport getAirportByAirportcode(String airportcode)throws ApplicationException;

    @Transactional
    Airport updateAirport(Airport airport) throws ApplicationException;

    @Transactional
    void deleteAirport(String airport) throws ApplicationException;

    Airport addAirport(Airport airport);
    Optional<Collection<Airport>> getAllAirports();
}
