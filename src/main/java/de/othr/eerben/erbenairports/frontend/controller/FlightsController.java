package de.othr.eerben.erbenairports.frontend.controller;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

@Controller
public class FlightsController {

    @Autowired
    private FlightdetailsServiceIF flightdetailsServiceIF;

    @Autowired
    private AirportServiceIF airportServiceIF;

    @RequestMapping(value="/departures/{airportcode}", method = RequestMethod.GET)
    public String departures(Model model, @PathVariable("airportcode") String airportcode){
        Collection<Flightdetails> flights = flightdetailsServiceIF.getDepartures(airportcode);
        model.addAttribute("flights", flights);
        return "unauthenticated/departures";
    }

    @RequestMapping(value="/departures_airport", method = RequestMethod.GET)
    public String departuresSelectAirport(Model model){
        Collection<Airport> airports = airportServiceIF.getAllAirports();
        model.addAttribute("airports", airports);
        return "unauthenticated/departure_airport";
    }

    @RequestMapping(value="/arrivals_airport", method = RequestMethod.GET)
    public String arrivalsSelectAirport(Model model){
        Collection<Airport> airports = airportServiceIF.getAllAirports();
        model.addAttribute("airports", airports);
        return "unauthenticated/arrivals_airport";
    }
    @RequestMapping(value="/arrivals/{airportcode}", method = RequestMethod.GET)
    public String arrivals(Model model, @PathVariable("airportcode") String airportcode){
        Collection<Flightdetails> flights = flightdetailsServiceIF.getDepartures(airportcode);
        model.addAttribute("flights", flights);
        return "unauthenticated/arrivals";
    }


}
