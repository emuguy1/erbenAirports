package de.othr.eerben.erbenairports.backend.services.impl;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.repositories.AirportRepository;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AirportService implements AirportServiceIF {

    @Autowired
    private AirportRepository airportRepo;

    Logger logger = LoggerFactory.getLogger(AirportService.class);

    @Override
    public Airport getAirportByAirportcode(String airportcode) throws AirportException {
        return airportRepo.findByAirportcode(airportcode).orElseThrow(() -> new AirportException("ERROR: Airport with airportcode not found: " + airportcode));
    }

    @Override
    @Transactional
    public Airport updateAirport(Airport airport) throws AirportException {
        try{
            return airportRepo.save(airport);
        } catch(Exception e) {
            logger.error("Airport not found " + airport.getAirportcode());
            throw new AirportException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteAirport(String airport) throws AirportException {
        try {
            airportRepo.deleteById(airport);
        } catch (Exception e){
            logger.error("Airport not found " + airport);
            throw new AirportException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Airport addAirport(Airport airport) {
        return airportRepo.save(airport);
    }

    @Override
    public List<Airport> getAllAirports() {
        return airportRepo.findDistinctByAirportcodeIsNotNull();
    }


}
