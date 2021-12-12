package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;

public interface AirportServiceIF {
    Airport getAirportByAirportcode(String airportcode);
    Airport addAirport(Airport airport);
}
