package de.othr.eerben.erbenairports.frontend.controller;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.exceptions.FlightdetailsException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriBuilder;

import java.util.Collection;
import java.util.Collections;

@Controller
public class FlightsController {

    @Autowired
    private FlightdetailsServiceIF flightdetailsServiceIF;

    @Autowired
    private AirportServiceIF airportServiceIF;

    @RequestMapping(value="/departures", method = RequestMethod.GET)
    public String departures(Model model, @RequestParam(value = "airport",required = false) String airportcode) throws Exception{
        System.out.println();
        Collection<Flightdetails> flights;
        if(airportcode!= null && !airportcode.isEmpty() && !airportcode.isBlank()){
            flights = flightdetailsServiceIF.getDepartures(airportcode);
                    }
        else{

            Collection<Airport> airports = airportServiceIF.getAllAirports().orElse(Collections.emptyList());
            if(!airports.isEmpty()){
                return "redirect:/departures?airport="+airports.stream().findFirst().get().getAirportcode();
            }
            else{
                throw new ApplicationException("No Airports were found!");
            }
        }
        System.out.println(flights);
        model.addAttribute("flights", flights);
        return "unauthenticated/departures";
    }


    @RequestMapping(value="/arrivals", method = RequestMethod.GET)
    public String arrivals(Model model, @RequestParam(value="airport",required = false) String airportcode){
        if(airportcode!= null &&!airportcode.isEmpty()&&!airportcode.isBlank()){
            Collection<Flightdetails> flights = flightdetailsServiceIF.getDepartures(airportcode);
            model.addAttribute("flights", flights);
        }
        return "unauthenticated/arrivals";
    }

    @RequestMapping(value="/flight/{flightnumber}", method = RequestMethod.GET)
    public String getFlightdetails(Model model, @PathVariable("flightnumber") String flightnumber){
        Collection<Flightdetails> flights = flightdetailsServiceIF.getDepartures(flightnumber);
        model.addAttribute("flights", flights);
        return "unauthenticated/arrivals";
    }


}
