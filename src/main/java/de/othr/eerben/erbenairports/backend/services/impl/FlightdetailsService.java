package de.othr.eerben.erbenairports.backend.services.impl;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.UserData;
import de.othr.eerben.erbenairports.backend.data.repositories.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FlightdetailsService implements FlightdetailsServiceIF {

    @Autowired
    private AirportServiceIF airportServiceIF;

    @Autowired
    private FlightdetailsRepository flightdetailsRepo;

    @Override
    public Collection<Flightdetails> getDepartures(String airportcode){
        //TODO: get departures sorted and after a specific time
        Airport airport= airportServiceIF.getAirportByAirportcode(airportcode);
        return flightdetailsRepo.findByDepartureOrderByDepartureTime(airport);
    }

    @Override
    public Collection<Flightdetails> getArrivals(String airportcode) {
        //TODO: get departures sorted and after a specific time
        Airport airport= airportServiceIF.getAirportByAirportcode(airportcode);
        return flightdetailsRepo.findByOriginOrderByArrivalTime(airport);
    }

    @Override
    public Flightdetails getFlightdetails(String flightnumber) {
        return flightdetailsRepo.findByFlightnumber(flightnumber);
    }

    @Override
    public boolean cancleFlight(UserData user, String flightnumber) {
        //TODO:
        return false;
    }

    @Override
    public Flightdetails bookFlight(UserData user, Flightdetails flightdetails) {
        //TODO:
        return null;
    }


}