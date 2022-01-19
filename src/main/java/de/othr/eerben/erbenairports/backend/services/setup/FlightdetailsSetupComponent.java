package de.othr.eerben.erbenairports.backend.services.setup;


import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import de.othr.eerben.erbenairports.backend.data.repositories.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.data.repositories.UserRepository;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FlightdetailsSetupComponent extends AbstractSetupComponent{

    @Autowired
    FlightdetailsRepository flightdetailsRepo;

    @Autowired
    FlightdetailsServiceIF flightdetailsService;

    @Autowired
    AirportServiceIF airportServiceIF;

    @Autowired
    UserRepository userRepository;

    @Override
    boolean setup() throws AirportException {
        try{

            if(flightdetailsRepo.existsFlightdetailsByFlightnumber("LH3200")){
                return true;
            }
            LocalDateTime now= LocalDateTime.ofInstant(Instant.now().plusSeconds(7200), ZoneId.systemDefault());
            //Date now= Date.from(Instant.now().plusSeconds(7200));

            FlighttransactionDTO flight= new FlighttransactionDTO("LH3200", 12.4, 25.6, 250, "LAX", "MUC",now);
            FlighttransactionDTO flight2= new FlighttransactionDTO("LH3220", 14.2, 25.6, 250, "MUC", "LAX",now);
            User user=userRepository.findByUsername("root").orElseThrow(() -> new AirportException("User not found"));
            flightdetailsService.bookFlight(user,flight);
            flightdetailsService.bookFlight(user,flight2);
            return true;

        }catch(Exception e){
            throw new AirportException("Flightdetails Setup failed. Could'nt create Flightdetails");
        }
    }
}
