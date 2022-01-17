package de.othr.eerben.erbenairports.frontend.controller;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlightdetailsDTO;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Scope("singleton")
@RestController
@Validated
@RequestMapping(value = "/api/rest")
public class FlightsRestController {
    @Autowired
    private FlightdetailsServiceIF flightdetailsServiceIF;

    @Autowired
    private UserServiceIF userServiceIF;

    @Autowired
    private AirportServiceIF airportServiceIF;

    @Transactional
    @RequestMapping(value = "/flight", method = RequestMethod.POST)
    public FlighttransactionDTO addFlightGermanTime( @Valid @RequestBody FlighttransactionDTO flighttransactionDTO) throws AirportException {
            //Handling in REST muss immer nach deutscher Zeit passieren
            //Input nur in Deutscher Zeit
            //Output nur in Deutscher Zeit

        Airport Departure = airportServiceIF.getAirportByAirportcode(flighttransactionDTO.getDeparture());
        //Set Departure Time from German Local Time to Departureairport Local Time so the service function works as wanted
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        calendar.setTime(Date.from(flighttransactionDTO.getDepartureTime().toInstant(ZoneId.of("Europe/Berlin").getRules().getOffset(LocalDateTime.now()))));
        calendar.setTimeZone(TimeZone.getTimeZone(Departure.getTimeZone()));
        flighttransactionDTO.setDepartureTime(LocalDateTime.ofInstant(calendar.toInstant(),ZoneId.of(Departure.getTimeZone())));
        if (flighttransactionDTO.getFlightnumber()==null||flighttransactionDTO.getFlightnumber().isEmpty()) {
                throw new AirportException("Flightnumber empty!");
            }
            //TODO: Make Input from German Time to TimeZone Time
            User user=userServiceIF.getUserByUsername(flighttransactionDTO.getUsername());
            FlightdetailsDTO flightdetailsDTO=new FlightdetailsDTO(flighttransactionDTO.getFlightnumber(),flighttransactionDTO.getFlightTimeHours(),flighttransactionDTO.getMaxCargo(),flighttransactionDTO.getPassengerCount(),flighttransactionDTO.getDeparture(),flighttransactionDTO.getOrigin(), flighttransactionDTO.getDepartureTime());
            Flightdetails flightdetails = flightdetailsServiceIF.bookFlight(user,flightdetailsDTO);
            System.out.println("External creation of flight: " + flightdetails);
            //We have to transform the Departure and arrival time back to LocalDateTime for

        LocalDateTime departure = LocalDateTime.ofInstant(flightdetails.getDepartureTime().getStartTime().toInstant(),ZoneId.of("Europe/Berlin"));

        LocalDateTime arrivalTime =LocalDateTime.ofInstant(flightdetails.getArrivalTime().getStartTime().toInstant(),ZoneId.of("Europe/Berlin"));

            return new FlighttransactionDTO("Admin","123",flightdetails.getFlightnumber(),flightdetails.getFlightTimeHours(),flightdetails.getMaxCargo(),flightdetails.getPassengerCount(),flightdetails.getDeparture().getAirportcode(),flightdetails.getOrigin().getAirportcode(), departure, arrivalTime);
    }

    @Transactional
    @RequestMapping(value = "/flight/international", method = RequestMethod.POST)
    public FlighttransactionDTO addFlightInternational( @Valid @RequestBody FlighttransactionDTO flighttransactionDTO) throws AirportException {

        if (flighttransactionDTO.getFlightnumber()==null||flighttransactionDTO.getFlightnumber().isEmpty()) {
            throw new AirportException("Flightnumber empty!");
        }
        User user=userServiceIF.getUserByUsername(flighttransactionDTO.getUsername());
        FlightdetailsDTO flightdetailsDTO=new FlightdetailsDTO(flighttransactionDTO.getFlightnumber(),flighttransactionDTO.getFlightTimeHours(),flighttransactionDTO.getMaxCargo(),flighttransactionDTO.getPassengerCount(),flighttransactionDTO.getDeparture(),flighttransactionDTO.getOrigin(), flighttransactionDTO.getDepartureTime());
        Flightdetails flightdetails = flightdetailsServiceIF.bookFlight(user,flightdetailsDTO);
        System.out.println("External creation of flight: " + flightdetails);
        return new FlighttransactionDTO("Admin","123",flightdetails.getFlightnumber(),flightdetails.getFlightTimeHours(),flightdetails.getMaxCargo(),flightdetails.getPassengerCount(),flightdetails.getDeparture().getAirportcode(),flightdetails.getOrigin().getAirportcode(), LocalDateTime.ofInstant(flightdetails.getDepartureTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getDeparture().getTimeZone())),LocalDateTime.ofInstant(flightdetails.getArrivalTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getOrigin().getTimeZone())));
    }
    @Transactional
    @RequestMapping(value = "/flight/cancle", method = RequestMethod.POST)
    public FlighttransactionDTO cancleFlight( @Valid @RequestBody FlighttransactionDTO flighttransactionDTO) throws AirportException {
        //Handling in REST muss immer nach deutscher Zeit passieren
        //Input nur in Deutscher Zeit
        //Output nur in Deutscher Zeit
        if (flighttransactionDTO.getFlightnumber()==null||flighttransactionDTO.getFlightnumber().isEmpty()) {
            throw new AirportException("Flightnumber empty!");
        }
        //TODO: Make Input from German Time to TimeZone Time
        User user=userServiceIF.getUserByUsername(flighttransactionDTO.getUsername());
        FlightdetailsDTO flightdetailsDTO=new FlightdetailsDTO(flighttransactionDTO.getFlightnumber(),flighttransactionDTO.getFlightTimeHours(),flighttransactionDTO.getMaxCargo(),flighttransactionDTO.getPassengerCount(),flighttransactionDTO.getDeparture(),flighttransactionDTO.getOrigin(), flighttransactionDTO.getDepartureTime());
        Flightdetails flightdetails = flightdetailsServiceIF.bookFlight(user,flightdetailsDTO);
        System.out.println("External creation of flight: " + flightdetails);
        return new FlighttransactionDTO("Admin","123",flightdetails.getFlightnumber(),flightdetails.getFlightTimeHours(),flightdetails.getMaxCargo(),flightdetails.getPassengerCount(),flightdetails.getDeparture().getAirportcode(),flightdetails.getOrigin().getAirportcode(), LocalDateTime.ofInstant(flightdetails.getDepartureTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getDeparture().getTimeZone())),LocalDateTime.ofInstant(flightdetails.getArrivalTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getOrigin().getTimeZone())));
    }
}
