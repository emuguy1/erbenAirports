package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.repositories.AirportRepository;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import org.springframework.beans.factory.annotation.Autowired;


public class AirportSetupComponent extends AbstractSetupComponent {

    @Autowired
    AirportRepository airportRepo;

    @Override
    boolean setup() throws AirportException {
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
            throw new AirportException("Airport Setup failed. Couldnt create Airports");
        }
    }

}
