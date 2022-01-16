package de.othr.eerben.erbenairports.backend.services.impl;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.repositories.AirportRepository;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
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
    public Airport getAirportByAirportcode(String airportcode) throws AirportException{
        return airportRepo.findByAirportcode(airportcode).orElseThrow(()-> new AirportException("ERROR: Airport with airportcode not found: "+airportcode));
    }

    @Override
    @Transactional
    public Airport updateAirport(Airport airport) throws AirportException{
        Optional<Airport> oldAirportOptional = airportRepo.findByAirportcode(airport.getAirportcode());
        if (oldAirportOptional.isPresent()) {
            Airport oldAirport = oldAirportOptional.get();
            return airportRepo.save(airport);
        } else {
            System.out.println("Airport not found " + airport.getAirportcode());
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteAirport(String airport) throws AirportException{
        Optional<Airport> oldAirportOptional = airportRepo.findByAirportcode(airport);

        if (oldAirportOptional.isPresent()) {
            airportRepo.deleteById(airport);
        } else {
            System.out.println("Airport not found " + airport);
        }
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
