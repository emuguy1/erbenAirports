package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;

public interface AirportService {
    Airport getAirportByAirportcode(String airportcode);
}
