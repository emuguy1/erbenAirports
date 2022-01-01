package de.othr.eerben.erbenairports.frontend.controller;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.exceptions.UIErrorMessage;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class FlightsController {

    @Autowired
    private FlightdetailsServiceIF flightdetailsServiceIF;

    @Autowired
    private AirportServiceIF airportServiceIF;

    @RequestMapping(value="/departures", method = RequestMethod.GET)
    public String departures(Model model, @RequestParam(value = "airport",required = false) String airportcode,@RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size) throws ApplicationException{
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Flightdetails> flightPage;
        Collection<Flightdetails> flights;
        Collection<Airport> airports = airportServiceIF.getAllAirports().orElse(Collections.emptyList());
        if(airportcode!= null && !airportcode.isEmpty() && !airportcode.isBlank() && !airportcode.equals("null")){
            if(airportServiceIF.getAirportByAirportcode(airportcode)==null){
                model.addAttribute("errorMessage", new UIErrorMessage("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
                return "unauthenticated/error-page";
            }
            flightPage = flightdetailsServiceIF.getDeparturesPaginated(airportcode,PageRequest.of(currentPage - 1, pageSize));

            model.addAttribute("flightPage", flightPage);
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
        int totalPages = flightPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("flights", flights);
        model.addAttribute("airports", airports);
        model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
        model.addAttribute("selectedAirport", new Airport());
        return "unauthenticated/departures";
    }

    @RequestMapping(value="/departure", method = RequestMethod.GET) //th:selected
    public String departuresSelected(Model model, @RequestParam(value = "airportcode",required = false) String airportcode) throws Exception{
        return "redirect:/departures?airport="+airportcode;
    }


    @RequestMapping(value="/arrivals", method = RequestMethod.GET)
    public String arrivals(Model model, @RequestParam(value="airport",required = false) String airportcode) throws ApplicationException {
        if(airportcode!= null &&!airportcode.isEmpty()&&!airportcode.isBlank()){
            Collection<Flightdetails> flights = flightdetailsServiceIF.getDepartures(airportcode);
            model.addAttribute("flights", flights);
        }
        return "unauthenticated/arrivals";
    }

    @RequestMapping(value="/flight/{flightnumber}", method = RequestMethod.GET)
    public String getFlightdetails(Model model, @PathVariable("flightnumber") String flightnumber) throws ApplicationException {
        Collection<Flightdetails> flights = flightdetailsServiceIF.getDepartures(flightnumber);
        model.addAttribute("flights", flights);
        return "unauthenticated/arrivals";
    }


}
