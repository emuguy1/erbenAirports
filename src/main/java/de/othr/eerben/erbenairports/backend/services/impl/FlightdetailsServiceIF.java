package de.othr.eerben.erbenairports.backend.services.impl;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.repositorys.AirportRepository;
import de.othr.eerben.erbenairports.backend.data.repositorys.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.services.AirportService;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FlightdetailsServiceIF implements FlightdetailsService {

    @Autowired
    private AirportService airportService;

    @Autowired
    private FlightdetailsRepository flightdetailsRepo;

    @Override
    public Collection<Flightdetails> getDepartures(String airportcode){
        Airport airport=airportService.getAirportByAirportcode(airportcode);
        return flightdetailsRepo.findByDepartureOrderByDepartureTime(airport);
    }

}
