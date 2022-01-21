package de.othr.eerben.erbenairports.backend.setup;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
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
import java.util.List;

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
            LocalDateTime now = LocalDateTime.ofInstant(Instant.now().plusSeconds(7200), ZoneId.systemDefault());

            FlighttransactionDTO flight = new FlighttransactionDTO("LH123456", 12.4, 25.6, 250, "MUC", "LAX", now);
            FlighttransactionDTO flight2 = new FlighttransactionDTO("LH3220", 14.2, 25.6, 250, "MUC", "LAX", now);
            User user = userRepository.findByUsername("root").orElseThrow(() -> new AirportException("User not found"));
            flightdetailsService.bookFlight(user, flight);
            flightdetailsService.bookFlight(user, flight2);
            List<Airport> airportList = airportServiceIF.getAllAirports();
            for (Airport airport: airportList) {
                if(!airport.getAirportcode().equals("MUC"))
                for(int i=0; i<40; i++){
                    LocalDateTime time = LocalDateTime.ofInstant(Instant.now().plusSeconds((int) (Math.random() * 999999)), ZoneId.systemDefault());
                    FlighttransactionDTO flightDTO = new FlighttransactionDTO("LH"+(int)(Math.random() * 9999), Math.random()*100, 25.6, 250, airport.getAirportcode(), airportList.get((int)(Math.random()*airportList.size())).getAirportcode(), time);
                    flightdetailsService.bookFlight(user, flightDTO);
                }
            }
            return true;

        } catch (Exception e) {
            logger.error("Flightdetails Setup failed. Errormessage: " + e.getMessage());
            throw new AirportException("Flightdetails Setup failed. Could'nt create Flightdetails");
        }
    }
}
