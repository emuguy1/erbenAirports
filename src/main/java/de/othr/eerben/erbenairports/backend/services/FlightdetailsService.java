package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;

import java.util.Collection;

public interface FlightdetailsService {

    Collection<Flightdetails> getDepartures(String airportcode);

}
