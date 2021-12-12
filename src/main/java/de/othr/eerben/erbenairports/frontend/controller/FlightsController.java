package de.othr.eerben.erbenairports.frontend.controller;


import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
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

    @RequestMapping(value="/departures/{airportcode}", method = RequestMethod.GET)
    public String departures(Model model, @PathVariable("airportcode") String airportcode){
        Collection<Flightdetails> flights = flightdetailsServiceIF.getDepartures(airportcode);
        model.addAttribute("flights", flights);
        return "unauthenticated/departures";
    }


}
