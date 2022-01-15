package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.repositories.AirportRepository;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TimeZone;


public class AirportSetupComponent extends AbstractSetupComponent {

    @Autowired
    AirportRepository airportRepo;

    @Override
    boolean setup() throws ApplicationException {
        try{
            if(airportRepo.existsAirportByAirportcode("MUC")){
                return true;
            }
            airportRepo.save(new Airport("MUC", "Europe/Berlin","Germany","Munich","Flughafen München „Franz Josef Strauß“"));
            airportRepo.save(new Airport("LAX", "America/Los_Angeles","USA","Los_Angeles","Los Angeles International Airport"));
            airportRepo.save(new Airport("BER", "Europe/Berlin","Germany","Berlin","Flughafen Berlin Brandenburg „Willy Brandt“"));
            airportRepo.save(new Airport("FRA", "Europe/Berlin","Germany","Frankfurt","Flughafen Frankfurt Main"));
            return true;
        }catch(Exception e){
            throw new ApplicationException("Airport Setup failed. Couldnt create Airports");
        }
    }

}
