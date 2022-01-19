package de.othr.eerben.erbenairports.backend.services.setup;


import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import de.othr.eerben.erbenairports.backend.data.repositories.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.data.repositories.UserRepository;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FlightdetailsSetupComponent extends AbstractSetupComponent {

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
        try {

            if (flightdetailsRepo.existsFlightdetailsByFlightnumber("LH123456")) {
                logger.info("Flightdetails Setup skiped, because flight with flightnumber LH123456 already exists ");
                return true;
            }

            //TODO:create more Example data and set Seconds to 0
            LocalDateTime now = LocalDateTime.ofInstant(Instant.now().plusSeconds(7200), ZoneId.systemDefault());

            FlighttransactionDTO flight = new FlighttransactionDTO("LH123456", 12.4, 25.6, 250, "LAX", "MUC", now);
            FlighttransactionDTO flight2 = new FlighttransactionDTO("LH3220", 14.2, 25.6, 250, "MUC", "LAX", now);
            User user = userRepository.findByUsername("root").orElseThrow(() -> new AirportException("User not found"));
            flightdetailsService.bookFlight(user, flight);
            flightdetailsService.bookFlight(user, flight2);
            return true;

        } catch (Exception e) {
            logger.error("Flightdetails Setup failed. Errormessage: " + e.getMessage());
            throw new AirportException("Flightdetails Setup failed. Could'nt create Flightdetails");
        }
    }
}
