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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
        //Handling in this REST Methode has to be in German Time for Partnerprojekt
        //Input in German Time
        //Output in German Time
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
            User user=userServiceIF.getUserByUsername(flighttransactionDTO.getUsername());
            FlightdetailsDTO flightdetailsDTO=new FlightdetailsDTO(flighttransactionDTO.getFlightnumber(),flighttransactionDTO.getFlightTimeHours(),flighttransactionDTO.getMaxCargo(),flighttransactionDTO.getPassengerCount(),flighttransactionDTO.getDeparture(),flighttransactionDTO.getOrigin(), flighttransactionDTO.getDepartureTime());
            Flightdetails flightdetails = flightdetailsServiceIF.bookFlight(user,flightdetailsDTO);
            System.out.println("External creation of flight: " + flightdetails);

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
    @RequestMapping(value = "/flight/cancel", method = RequestMethod.POST)
    public boolean cancelFlightGermanTime(@Valid @RequestBody FlighttransactionDTO flighttransactionDTO) throws AirportException{
        User user=userServiceIF.getUserByUsername(flighttransactionDTO.getUsername());
        if(!userServiceIF.checkPassword(flighttransactionDTO.getPassword(),user)){
            throw new AirportException("Username or Password were incorrect!");
        }

        if (flighttransactionDTO.getFlightnumber()==null||flighttransactionDTO.getFlightnumber().isEmpty()) {
            throw new AirportException("Flightnumber empty!");
        }
        try{
            return flightdetailsServiceIF.cancelFlight(user,flighttransactionDTO);
        }catch(AirportException a){
            a.setErrortitel("Flight could not be canceled");
            throw a;
        }

    }
}
