package de.othr.eerben.erbenairports.backend.services.impl;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.repositories.AirportRepository;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirportService implements AirportServiceIF {

    @Autowired
    private AirportRepository airportRepo;

    @Override
    public Airport getAirportByAirportcode(String airportcode){
        return airportRepo.findByAirportcode(airportcode);
    }

    @Override
    public Airport addAirport(Airport airport) {
        return airportRepo.save(airport);
    }


}
