package de.othr.eerben.erbenairports.backend.services.setup;


import de.othr.eerben.erbenairports.backend.data.entities.dto.FlightdetailsDTO;
import de.othr.eerben.erbenairports.backend.data.repositories.BookedCalendarslotRepository;
import de.othr.eerben.erbenairports.backend.data.repositories.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class FlightdetailsSetupComponent extends AbstractSetupComponent{

    @Autowired
    FlightdetailsRepository flightdetailsRepo;

    @Autowired
    FlightdetailsServiceIF flightdetailsService;

    @Override
    boolean setup() throws ApplicationException {
        try{

            if(flightdetailsRepo.existsFlightdetailsByFlightnumber("LH3200")){
                return true;
            }
            LocalDateTime now= LocalDateTime.from(Instant.now().plusSeconds(7200));
            //Date now= Date.from(Instant.now().plusSeconds(7200));

            FlightdetailsDTO flight= new FlightdetailsDTO("LH3200", 12.4, 25.6, 250, "LAX", "MUC",now);
            FlightdetailsDTO flight2= new FlightdetailsDTO("LH3220", 14.2, 25.6, 250, "MUC", "LAX",now);
            flightdetailsService.bookFlight(flight);
            flightdetailsService.bookFlight(flight2);
            return true;

        }catch(Exception e){
            throw new ApplicationException("Flightdetails Setup failed. Could'nt create Airports");
        }
    }
}
