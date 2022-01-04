package de.othr.eerben.erbenairports.frontend.controller;


import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlightdetailsDTO;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.exceptions.UIErrorMessage;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
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
        Collection<Airport> airports = airportServiceIF.getAllAirports().orElse(Collections.emptyList());
        try {
            if (airportcode != null && !airportcode.isEmpty() && !airportcode.isBlank() && !airportcode.equals("null")) {
                if(page.isEmpty() || size.isEmpty()){
                    return "redirect:/departures?airport="+airportcode+"&size="+pageSize+"&page="+currentPage;
                }
                else if (airportServiceIF.getAirportByAirportcode(airportcode) == null) {
                    model.addAttribute("uiErrorMessage", new UIErrorMessage("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
                    return "/unauthenticated/error-page";
                }
                flightPage = flightdetailsServiceIF.getDeparturesPaginated(airportcode, PageRequest.of(currentPage - 1, pageSize));

                model.addAttribute("flightPage", flightPage);
            } else {
                if (!airports.isEmpty()) {
                    return "redirect:/departures?airport=" + airports.stream().findFirst().get().getAirportcode()+"&size="+pageSize+"&page="+currentPage;
                } else {
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
        }
        catch (ApplicationException e){
            model.addAttribute("uiErrorMessage", new UIErrorMessage("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
            return "/unauthenticated/error-page";
        }
        model.addAttribute("flights", flightPage.stream().toList());
        model.addAttribute("airports", airports);
        model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
        model.addAttribute("selectedAirport", new Airport());
        return "/unauthenticated/departures";
    }

    @RequestMapping(value="/departure", method = RequestMethod.GET) //th:selected
    public String departuresSelected(Model model, @RequestParam(value = "airportcode",required = false) String airportcode) throws Exception{
        return "redirect:/departures?airport="+airportcode;
    }



    @RequestMapping(value="/arrivals", method = RequestMethod.GET)
    public String arrivals(Model model, @RequestParam(value = "airport",required = false) String airportcode,@RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size) throws ApplicationException{
        //TODO: rewrite for arrivals
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Flightdetails> flightPage;
        Collection<Airport> airports = airportServiceIF.getAllAirports().orElse(Collections.emptyList());
        try {
            if (airportcode != null && !airportcode.isEmpty() && !airportcode.isBlank() && !airportcode.equals("null")) {
                if(page.isEmpty() || size.isEmpty()){
                    return "redirect:/arrivals?airport="+airportcode+"&size="+pageSize+"&page="+currentPage;
                }
                else if (airportServiceIF.getAirportByAirportcode(airportcode) == null) {
                    model.addAttribute("uiErrorMessage", new UIErrorMessage("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
                    return "/unauthenticated/error-page";
                }
                flightPage = flightdetailsServiceIF.getArrivalsPaginated(airportcode, PageRequest.of(currentPage - 1, pageSize));

                model.addAttribute("flightPage", flightPage);
            } else {
                if (!airports.isEmpty()) {
                    return "redirect:/arrivals?airport=" + airports.stream().findFirst().get().getAirportcode()+"&size="+pageSize+"&page="+currentPage;
                } else {
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
        }
        catch (ApplicationException e){
            model.addAttribute("uiErrorMessage", new UIErrorMessage("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
            return "/unauthenticated/error-page";
        }
        model.addAttribute("flights", flightPage.stream().toList());
        model.addAttribute("airports", airports);
        model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
        model.addAttribute("selectedAirport", new Airport());
        return "/unauthenticated/arrivals";
    }

    @RequestMapping(value="/arrival", method = RequestMethod.GET) //th:selected
    public String arrivalsSelected(Model model, @RequestParam(value = "airportcode",required = false) String airportcode) throws Exception{
        return "redirect:/arrivals?airport="+airportcode;
    }

    @RequestMapping(value="/flight/new", method = RequestMethod.GET)
    public String bookFlight(Model model) throws ApplicationException {
        model.addAttribute("flightdetails", new FlightdetailsDTO());
        return "flight/new";
    }

    @Transactional
    @RequestMapping(value="/flight/new", method = RequestMethod.POST)//temp for testing
    public String addFlight(Model model, @ModelAttribute("flightdetails") FlightdetailsDTO flightdetailsdto) throws ApplicationException {
        Flightdetails flightdetails=flightdetailsServiceIF.bookFlight(flightdetailsdto);
        return "redirect:/flight/"+flightdetails.getFlightid();
    }

    @RequestMapping(value="/flight/{id}", method = RequestMethod.GET)
    public String getFlightdetails(Model model, @PathVariable("id") long flightid) throws ApplicationException {
        Flightdetails flightdetails=flightdetailsServiceIF.getFlightdetailsById(flightid).orElseThrow();
        model.addAttribute("flightdetails", flightdetails);
        return "/flight/details";
    }

}
