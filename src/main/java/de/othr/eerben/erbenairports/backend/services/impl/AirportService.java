package de.othr.eerben.erbenairports.backend.services.impl;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.repositories.AirportRepository;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
public class AirportService implements AirportServiceIF {

    @Autowired
    private AirportRepository airportRepo;

    @Override
    @Transactional
    public Airport getAirportByAirportcode(String airportcode) throws ApplicationException{
        return airportRepo.findByAirportcode(airportcode).orElseThrow(()-> new ApplicationException("ERROR: Airport with airportcode not found: "+airportcode));
    }

    @Override
    @Transactional
    public Airport addAirport(Airport airport) {
        return airportRepo.save(airport);
    }

    @Override
    @Transactional
    public Optional<Collection<Airport>> getAllAirports(){
        return airportRepo.findDistinctByAirportcodeIsNotNull();
    }


}
