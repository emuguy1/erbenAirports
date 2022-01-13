package de.othr.eerben.erbenairports.frontend.controller;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlightdetailsDTO;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.exceptions.UIErrorMessage;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

@Scope("singleton")
@RestController
@Validated
@RequestMapping(value = "/api/rest")
public class FlightsRestController {
    @Autowired
    private FlightdetailsServiceIF flightdetailsServiceIF;

    @Transactional
    @RequestMapping(value = "/flight", method = RequestMethod.POST)
    public FlighttransactionDTO addFlight( @Valid @RequestBody FlighttransactionDTO flighttransactionDTO) throws Exception {
        try {
            if (flighttransactionDTO.getFlightnumber()==null||flighttransactionDTO.getFlightnumber().isEmpty()) {
                throw new ApplicationException("Flightnumber empty!");
            }

            FlightdetailsDTO flightdetailsDTO=new FlightdetailsDTO(flighttransactionDTO.getFlightnumber(),flighttransactionDTO.getFlightTimeHours(),flighttransactionDTO.getMaxCargo(),flighttransactionDTO.getPassangerCount(),flighttransactionDTO.getDeparture(),flighttransactionDTO.getOrigin(), flighttransactionDTO.getDepartureTime());
            Flightdetails flightdetails = flightdetailsServiceIF.bookFlight(flightdetailsDTO);
            System.out.println("External creation of flight: " + flightdetails);
            return new FlighttransactionDTO("Admin","123",flightdetails.getFlightnumber(),flightdetails.getFlightTimeHours(),flightdetails.getMaxCargo(),flightdetails.getPassangerCount(),flightdetails.getDeparture().getAirportcode(),flightdetails.getOrigin().getAirportcode(), LocalDateTime.ofInstant(flightdetails.getDepartureTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getDeparture().getTimeZone())),LocalDateTime.ofInstant(flightdetails.getArrivalTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getOrigin().getTimeZone())));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
