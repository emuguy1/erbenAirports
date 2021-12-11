package de.othr.eerben.erbenairports.backend.services.impl;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.repositorys.AirportRepository;
import de.othr.eerben.erbenairports.backend.services.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirportServiceIF implements AirportService {

    @Autowired
    private AirportRepository airportRepo;

    @Override
    public Airport getAirportByAirportcode(String airportcode){
        return airportRepo.findByAirportcode(airportcode);
    }
}
