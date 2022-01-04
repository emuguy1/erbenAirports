package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;

import java.util.Collection;
import java.util.Optional;

public interface AirportServiceIF {
    Airport getAirportByAirportcode(String airportcode)throws ApplicationException;
    Airport addAirport(Airport airport);
    Optional<Collection<Airport>> getAllAirports();
}
