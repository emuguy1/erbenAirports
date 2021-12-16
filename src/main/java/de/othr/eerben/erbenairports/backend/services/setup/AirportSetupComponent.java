package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.repositories.AirportRepository;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TimeZone;

@Service
public class AirportSetupComponent extends AbstractSetupComponent {

    @Autowired
    AirportRepository airportRepo;

    @Override
    boolean setup() throws ApplicationException {
        try{
            if(airportRepo.findByAirportcode("MUC") != null){
                return true;
            }
            airportRepo.save(new Airport("MUC", TimeZone.getTimeZone("Germany/Berlin"),"Germany","Munich",48.21,11.46));
            //TODO: ändern Koordinaten
            airportRepo.save(new Airport("LAX", TimeZone.getTimeZone("USA/LosAngeles"),"USA","Los Angeles",23.11,44.56));
            return true;
        }catch(Exception e){
            throw new ApplicationException("Airport Setup failed. Couldnt create Airports");
        }
    }

}
