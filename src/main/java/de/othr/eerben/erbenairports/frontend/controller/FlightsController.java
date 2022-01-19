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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Scope("singleton")
public class FlightsController {

    @Autowired
    private FlightdetailsServiceIF flightdetailsServiceIF;

    @Autowired
    private AirportServiceIF airportServiceIF;

    @Autowired
    private UserServiceIF userServiceIF;

    @RequestMapping(value = "/departures", method = RequestMethod.GET)
    public String departures(Model model, @RequestParam(value = "airport", required = false) String airportcode, @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size) throws AirportException {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Flightdetails> flightPage;
        Collection<Airport> airports = airportServiceIF.getAllAirports().orElse(Collections.emptyList());
        try {
            if (airportcode != null && !airportcode.isEmpty() && !airportcode.isBlank() && !airportcode.equals("null")) {
                if (page.isEmpty() || size.isEmpty()) {
                    return "redirect:/departures?airport=" + airportcode + "&size=" + pageSize + "&page=" + currentPage;
                } else if (airportServiceIF.getAirportByAirportcode(airportcode) == null) {
                    model.addAttribute("UIerror", new AirportException("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
                    return "unauthenticated/error-page";
                }
                flightPage = flightdetailsServiceIF.getDeparturesPaginated(airportcode, PageRequest.of(currentPage - 1, pageSize));

                model.addAttribute("flightPage", flightPage);
            } else {
                if (!airports.isEmpty()) {
                    return "redirect:/departures?airport=" + airports.stream().findFirst().get().getAirportcode() + "&size=" + pageSize + "&page=" + currentPage;
                } else {
                    throw new AirportException("No Airports were found!");
                }
            }
            int totalPages = flightPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
        } catch (AirportException e) {
            //TODO: rephrase Error Message
            model.addAttribute("UIerror", new AirportException("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
            return "unauthenticated/error-page";
        }
        model.addAttribute("flights", flightPage.toList());
        model.addAttribute("airports", airports);
        model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
        model.addAttribute("selectedAirport", new Airport());
        return "unauthenticated/departures";
    }

    @RequestMapping(value = "/departure", method = RequestMethod.GET) //th:selected
    public String departuresSelected(Model model, @RequestParam(value = "airportcode", required = false) String airportcode) throws Exception {
        return "redirect:/departures?airport=" + airportcode;
    }


    @RequestMapping(value = "/arrivals", method = RequestMethod.GET)
    public String arrivals(Model model, @RequestParam(value = "airport", required = false) String airportcode, @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) throws AirportException {
        //TODO: rewrite for arrivals
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Flightdetails> flightPage;
        Collection<Airport> airports = airportServiceIF.getAllAirports().orElse(Collections.emptyList());
        try {
            if (airportcode != null && !airportcode.isEmpty() && !airportcode.isBlank() && !airportcode.equals("null")) {
                if (page.isEmpty() || size.isEmpty()) {
                    return "redirect:/arrivals?airport=" + airportcode + "&size=" + pageSize + "&page=" + currentPage;
                } else if (airportServiceIF.getAirportByAirportcode(airportcode) == null) {
                    model.addAttribute("UIerror", new AirportException("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
                    return "unauthenticated/error-page";
                }
                flightPage = flightdetailsServiceIF.getArrivalsPaginated(airportcode, PageRequest.of(currentPage - 1, pageSize));

                model.addAttribute("flightPage", flightPage);
            } else {
                if (!airports.isEmpty()) {
                    return "redirect:/arrivals?airport=" + airports.stream().findFirst().get().getAirportcode() + "&size=" + pageSize + "&page=" + currentPage;
                } else {
                    throw new AirportException("No Airports were found!");
                }
            }
            int totalPages = flightPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
        } catch (AirportException e) {
            model.addAttribute("UIerror", new AirportException("Wrong Airportnumber specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
            return "unauthenticated/error-page";
        }
        model.addAttribute("flights", flightPage.toList());
        model.addAttribute("airports", airports);
        model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
        model.addAttribute("selectedAirport", new Airport());
        return "unauthenticated/arrivals";
    }

    @RequestMapping(value = "/arrival", method = RequestMethod.GET) //th:selected
    public String arrivalsSelected(Model model, @RequestParam(value = "airportcode", required = false) String airportcode) throws Exception {
        return "redirect:/arrivals?airport=" + airportcode;
    }

    @RequestMapping(value = "/flight/new", method = RequestMethod.GET)
    public String bookFlight(Model model) throws AirportException {
        model.addAttribute("airports", airportServiceIF.getAllAirports().orElse(Collections.emptyList()));
        model.addAttribute("flightdetails", new FlighttransactionDTO());
        return "flight/new";
    }

    @Transactional
    @RequestMapping(value = "/flight/new", method = RequestMethod.POST)//temp for testing
    public String addFlight(Model model, @AuthenticationPrincipal User user, @ModelAttribute("flightdetails") FlighttransactionDTO flightdetailsdto) throws AirportException {
        try {
            if (flightdetailsdto.getFlightnumber().isEmpty()) {
                throw new AirportException("Flightnumber empty!");
            }
            Flightdetails flightdetails = flightdetailsServiceIF.bookFlight(user,flightdetailsdto);
            return "redirect:/flight/" + flightdetails.getFlightid() + "/details";
        } catch (AirportException e) {
            model.addAttribute("flightdetails", flightdetailsdto);
            model.addAttribute("airports", airportServiceIF.getAllAirports().orElse(Collections.emptyList()));
            model.addAttribute("UIerror", new AirportException(e.getMessage()));
            return "flight/new";
        }

    }

    @RequestMapping(value = "/flight/{id}/details", method = RequestMethod.GET)
    public String getFlightdetails(Model model, @PathVariable("id") long flightid) throws AirportException {
        Flightdetails flightdetails = flightdetailsServiceIF.getFlightdetailsById(flightid).orElseThrow();
        model.addAttribute("flightdetails", flightdetails);
        ZonedDateTime departureTimeUTC = ZonedDateTime.ofInstant(flightdetails.getDepartureTime().getStartTime().toInstant(), ZoneId.of("Etc/UTC"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm z");
        //Zoned date time at target timezone
        ZonedDateTime departureTimeDepartureAirport = departureTimeUTC.withZoneSameInstant(ZoneId.of(flightdetails.getDepartureAirport().getTimeZone()));
        model.addAttribute("departureTimeDepartureAirport", departureTimeDepartureAirport.format(formatter));

        ZonedDateTime departureTimeArrivalAirport = departureTimeUTC.withZoneSameInstant(ZoneId.of(flightdetails.getArrivalAirport().getTimeZone()));
        model.addAttribute("departureTimeArrivalAirport", departureTimeArrivalAirport.format(formatter));


        ZonedDateTime arrivalTimeUTC = ZonedDateTime.ofInstant(flightdetails.getArrivalTime().getStartTime().toInstant(), ZoneId.of("Etc/UTC"));

        //Zoned date time at target timezone
        ZonedDateTime arrivalTimeDepartureAirport = arrivalTimeUTC.withZoneSameInstant(ZoneId.of(flightdetails.getDepartureAirport().getTimeZone()));
        model.addAttribute("arrivalTimeDepartureAirport", arrivalTimeDepartureAirport.format(formatter));

        ZonedDateTime arrivalTimeArrivalAirport = arrivalTimeUTC.withZoneSameInstant(ZoneId.of(flightdetails.getArrivalAirport().getTimeZone()));
        model.addAttribute("arrivalTimeArrivalAirport", arrivalTimeArrivalAirport.format(formatter));

        String flighttimestring = ((int) flightdetails.getFlightTimeHours()) + "h " + (int) ((flightdetails.getFlightTimeHours() - ((int) (flightdetails.getFlightTimeHours()))) * 60) + "min";
        model.addAttribute("flighttime", flighttimestring);
        return "flight/details";
    }

    @Transactional
    @RequestMapping(value = "/flight/{id}/edit", method = RequestMethod.GET)
    public String editFlightdetails(Model model, @PathVariable("id") long flightid) throws AirportException {
        model.addAttribute("flightdetails", flightdetailsServiceIF.getFlightdetailsById(flightid));
        //TODO: flightdetailsobject should be converted to DTO and the id should be handled
        model.addAttribute("edit", true);
        return "airport/edit";
    }

    @Transactional
    @RequestMapping(value = "/flight/edit", method = RequestMethod.POST)
    public String saveEditedFlightdetails(Model model,  @ModelAttribute("airport") Airport airport) throws AirportException {
        airportServiceIF.updateAirport(airport);
        return "redirect:/airport/" + airport.getAirportcode() + "/details";
    }
    @Transactional
    @RequestMapping(value = "/flight/{id}/delete", method = RequestMethod.GET)
    public String deleteFlightdetails(Model model,  @PathVariable("id") long flightid) throws AirportException {
        //TODO: To be thought if only status is set to cancelled
        flightdetailsServiceIF.deleteById(flightid);
        return "redirect:/";
    }

    @RequestMapping(value = "/airport/new", method = RequestMethod.GET)
    public String addAirport(Model model) throws AirportException {
        model.addAttribute("timezoneIDs", TimeZone.getAvailableIDs());
        model.addAttribute("airport", new Airport());
        return "airport/new";
    }

    @RequestMapping(value = "/airport/new", method = RequestMethod.POST)
    public String saveAirport(Model model, @ModelAttribute("airport") Airport airport) throws AirportException {
        Airport savedAirport = airportServiceIF.addAirport(airport);
        return "redirect:/airport/" + savedAirport.getAirportcode() + "/details";
    }

    @Transactional
    @RequestMapping(value = "/airport/{id}/details", method = RequestMethod.GET)
    public String getAirportdetails(Model model, @PathVariable("id") String airportcode) throws AirportException {
        model.addAttribute("airport", airportServiceIF.getAirportByAirportcode(airportcode));
        return "airport/details";
    }

    @Transactional
    @RequestMapping(value = "/airport/{id}/edit", method = RequestMethod.GET)
    public String editAirport(Model model, @PathVariable("id") String airportcode) throws AirportException {
        model.addAttribute("airport", airportServiceIF.getAirportByAirportcode(airportcode));
        model.addAttribute("edit", true);
        model.addAttribute("timezoneIDs", TimeZone.getAvailableIDs());
        return "airport/edit";
    }

    @Transactional
    @RequestMapping(value = "/airport/edit", method = RequestMethod.POST)
    public String saveeditedAirport(Model model,  @ModelAttribute("airport") Airport airport) throws AirportException {
        airportServiceIF.updateAirport(airport);
        return "redirect:/airport/" + airport.getAirportcode() + "/details";
    }
    @Transactional
    @RequestMapping(value = "/airport/{id}/delete", method = RequestMethod.GET)
    public String delete(Model model,  @PathVariable("id") String airport) throws AirportException {
        flightdetailsServiceIF.deleteByAirportId(airport);
        airportServiceIF.deleteAirport(airport);
        return "redirect:/";
    }
}
