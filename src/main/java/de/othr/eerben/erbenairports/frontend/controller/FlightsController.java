package de.othr.eerben.erbenairports.frontend.controller;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.exceptions.UIErrorMessage;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        Collection<Flightdetails> flights;
        Collection<Airport> airports = airportServiceIF.getAllAirports().orElse(Collections.emptyList());
        if(airportcode!= null && !airportcode.isEmpty() && !airportcode.isBlank() && !airportcode.equals("null")){
            if(airportServiceIF.getAirportByAirportcode(airportcode)==null){
                model.addAttribute("errorMessage", new UIErrorMessage("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
                return "unauthenticated/error-page";
            }
            flights = flightdetailsServiceIF.getDepartures(airportcode);
        }
        else{
            if(!airports.isEmpty()){
                return "redirect:/departures?airport="+airports.stream().findFirst().get().getAirportcode();
            }
            else{
                throw new ApplicationException("No Airports were found!");
            }
        }
        model.addAttribute("flights", flights);
        model.addAttribute("airports", airports);
        model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
        model.addAttribute("selectedAirport", new Airport());
        return "unauthenticated/departures";
    }

    @RequestMapping(value="/departure", method = RequestMethod.GET) //th:selected
    public String departuresSelected(Model model, @RequestParam(value = "airportcode",required = false) String airportcode) throws Exception{
        System.out.println("Action ausgelöst:");
        System.out.println(airportcode);
        return "redirect:/departures?airport="+airportcode;
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
