package de.othr.eerben.erbenairports.frontend.controller;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
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


import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public FlighttransactionDTO addFlightGermanTime(@RequestBody FlighttransactionDTO flighttransactionDTO) throws AirportException {
        //Handling in this REST Methode has to be in German Time for Partnerprojekt
        //Input in German Time
        //Output in German Time
        Airport Departure = airportServiceIF.getAirportByAirportcode(flighttransactionDTO.getDepartureAirport());
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
        FlighttransactionDTO flightdetailsDTO=new FlighttransactionDTO(flighttransactionDTO.getFlightnumber(),flighttransactionDTO.getFlightTimeHours(),flighttransactionDTO.getMaxCargo(),flighttransactionDTO.getPassengerCount(),flighttransactionDTO.getDepartureAirport(),flighttransactionDTO.getArrivalAirport(), flighttransactionDTO.getDepartureTime());
            Flightdetails flightdetails = flightdetailsServiceIF.bookFlight(user,flightdetailsDTO);
            System.out.println("External creation of flight: " + flightdetails);

        LocalDateTime departure = LocalDateTime.ofInstant(flightdetails.getDepartureTime().getStartTime().toInstant(),ZoneId.of("Europe/Berlin"));

        LocalDateTime arrivalTime =LocalDateTime.ofInstant(flightdetails.getArrivalTime().getStartTime().toInstant(),ZoneId.of("Europe/Berlin"));

            return new FlighttransactionDTO(flighttransactionDTO.getUsername(), flighttransactionDTO.getPassword(), flightdetails.getFlightnumber(),flightdetails.getFlightTimeHours(),flightdetails.getMaxCargo(),flightdetails.getPassengerCount(),flightdetails.getDepartureAirport().getAirportcode(),flightdetails.getArrivalAirport().getAirportcode(), departure, arrivalTime);
    }

    @Transactional
    @RequestMapping(value = "/flight/international", method = RequestMethod.POST)
    public FlighttransactionDTO addFlightInternational(@RequestBody FlighttransactionDTO flighttransactionDTO) throws AirportException {

        if (flighttransactionDTO.getFlightnumber()==null||flighttransactionDTO.getFlightnumber().isEmpty()) {
            throw new AirportException("Flightnumber empty!");
        }
        User user=userServiceIF.getUserByUsername(flighttransactionDTO.getUsername());
        FlighttransactionDTO flightdetailsDTO=new FlighttransactionDTO(flighttransactionDTO.getFlightnumber(),flighttransactionDTO.getFlightTimeHours(),flighttransactionDTO.getMaxCargo(),flighttransactionDTO.getPassengerCount(),flighttransactionDTO.getDepartureAirport(),flighttransactionDTO.getArrivalAirport(), flighttransactionDTO.getDepartureTime());
        Flightdetails flightdetails = flightdetailsServiceIF.bookFlight(user,flightdetailsDTO);
        System.out.println("External creation of flight: " + flightdetails);
        return new FlighttransactionDTO(flighttransactionDTO.getUsername(), flighttransactionDTO.getPassword(),flightdetails.getFlightnumber(),flightdetails.getFlightTimeHours(),flightdetails.getMaxCargo(),flightdetails.getPassengerCount(),flightdetails.getDepartureAirport().getAirportcode(),flightdetails.getArrivalAirport().getAirportcode(), LocalDateTime.ofInstant(flightdetails.getDepartureTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getDepartureAirport().getTimeZone())),LocalDateTime.ofInstant(flightdetails.getArrivalTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getArrivalAirport().getTimeZone())));
    }

    @Transactional
    @RequestMapping(value = "/flight/cancel", method = RequestMethod.POST)
    public boolean cancelFlightGermanTime(@RequestBody FlighttransactionDTO flighttransactionDTO) throws AirportException{
        System.out.println(flighttransactionDTO);
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
