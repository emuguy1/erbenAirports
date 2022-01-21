package de.othr.eerben.erbenairports.frontend.controller;


import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
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

    @RequestMapping(value = "/departures", method = RequestMethod.GET)
    public String departures(Model model, @RequestParam(value = "airport", required = false) String airportcode, @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size) {
        try {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(5);
            Page<Flightdetails> flightPage;
            List<Airport> airports = airportServiceIF.getAllAirports();
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
            model.addAttribute("flights", flightPage.toList());
            model.addAttribute("airports", airports);
            model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
            model.addAttribute("selectedAirport", new Airport());
            return "unauthenticated/departures";
        } catch (AirportException e) {
            model.addAttribute("UIerror", new AirportException("Wrong Airportcode.", "Try clicking on departures and then select your wanted airport from the dropdown list."));
            return "unauthenticated/error-page";
        }

    }

    //For changing the airport when selected
    @RequestMapping(value = "/departure", method = RequestMethod.GET)
    public String departuresSelected(@RequestParam(value = "airportcode", required = false) String airportcode) {
        return "redirect:/departures?airport=" + airportcode;
    }

    @RequestMapping(value = "/arrivals", method = RequestMethod.GET)
    public String arrivals(Model model, @RequestParam(value = "airport", required = false) String airportcode, @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {
        //TODO: maybe rewrite for timeselect
        try {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(5);

            Page<Flightdetails> flightPage;
            List<Airport> airports = airportServiceIF.getAllAirports();
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
            model.addAttribute("flights", flightPage.toList());
            model.addAttribute("airports", airports);
            model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
            model.addAttribute("selectedAirport", new Airport());
            return "unauthenticated/arrivals";
        } catch (AirportException e) {
            model.addAttribute("UIerror", new AirportException("Wrong Airportcode specified", "Try clicking on departures and then select your wanted airport from the dropdown list."));
            return "unauthenticated/error-page";
        }
    }

    //For changing the airport when selected
    @RequestMapping(value = "/arrival", method = RequestMethod.GET)
    public String arrivalsSelected(@RequestParam(value = "airportcode", required = false) String airportcode) {
        return "redirect:/arrivals?airport=" + airportcode;
    }

    @RequestMapping(value = "/flight/new", method = RequestMethod.GET)
    public String bookFlight(Model model) {
        model.addAttribute("airports", airportServiceIF.getAllAirports());
        model.addAttribute("flightdetails", new FlighttransactionDTO());
        return "flight/new";
    }

    @Transactional
    @RequestMapping(value = "/flight/new", method = RequestMethod.POST)
    public String addFlight(Model model, @AuthenticationPrincipal User user, @ModelAttribute("flightdetails") FlighttransactionDTO flightdetailsdto) {
        try {
            if (flightdetailsdto.getFlightnumber().isEmpty()) {
                throw new AirportException("Flightnumber empty!");
            }
            Flightdetails flightdetails = flightdetailsServiceIF.bookFlight(user, flightdetailsdto);
            return "redirect:/flight/" + flightdetails.getFlightid() + "/details";
        } catch (AirportException e) {
            model.addAttribute("flightdetails", flightdetailsdto);
            model.addAttribute("airports", airportServiceIF.getAllAirports());
            model.addAttribute("UIerror", new AirportException(e.getMessage()));
            return "flight/new";
        }

    }

    @RequestMapping(value = "/flight/{id}/details", method = RequestMethod.GET)
    public String getFlightdetails(Model model, @PathVariable("id") long flightid) {
        try {
            Flightdetails flightdetails = flightdetailsServiceIF.getFlightdetailsById(flightid);
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
        } catch (AirportException a) {
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @Transactional
    @RequestMapping(value = "/flight/{id}/delete", method = RequestMethod.GET)
    public String deleteFlightdetails(Model model, @AuthenticationPrincipal User user, @PathVariable("id") long flightid) {
        try {
            Flightdetails flight = flightdetailsServiceIF.getFlightdetailsById(flightid);
            flightdetailsServiceIF.cancelFlight(user, flightdetailsServiceIF.getFlighttransactionDTO(flight));
            return "redirect:/";
        } catch (AirportException a) {
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @RequestMapping(value = "/airport/new", method = RequestMethod.GET)
    public String addAirport(Model model) {
        model.addAttribute("timezoneIDs", TimeZone.getAvailableIDs());
        model.addAttribute("airport", new Airport());
        return "airport/new";
    }

    @RequestMapping(value = "/airport/new", method = RequestMethod.POST)
    public String saveAirport(@ModelAttribute("airport") Airport airport) {
        Airport savedAirport = airportServiceIF.addAirport(airport);
        return "redirect:/airport/" + savedAirport.getAirportcode() + "/details";
    }

    @Transactional
    @RequestMapping(value = "/airport/{id}/details", method = RequestMethod.GET)
    public String getAirportdetails(Model model, @PathVariable("id") String airportcode) {
        try {
            model.addAttribute("airport", airportServiceIF.getAirportByAirportcode(airportcode));
            return "airport/details";
        } catch (AirportException a) {
            model.addAttribute("UIerror", a);
            return "error";
        }

    }

    @Transactional
    @RequestMapping(value = "/airport/{id}/edit", method = RequestMethod.GET)
    public String editAirport(Model model, @PathVariable("id") String airportcode) {
        try {
            model.addAttribute("airport", airportServiceIF.getAirportByAirportcode(airportcode));
            model.addAttribute("edit", true);
            model.addAttribute("timezoneIDs", TimeZone.getAvailableIDs());
            return "airport/edit";
        } catch (AirportException a) {
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @Transactional
    @RequestMapping(value = "/airport/edit", method = RequestMethod.POST)
    public String saveeditedAirport(Model model, @ModelAttribute("airport") Airport airport) {
        try {
            airportServiceIF.updateAirport(airport);
            return "redirect:/airport/" + airport.getAirportcode() + "/details";
        } catch (AirportException a) {
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @Transactional
    @RequestMapping(value = "/airport/{id}/delete", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") String airport) {
        try {
            flightdetailsServiceIF.deleteByAirportId(airport);
            airportServiceIF.deleteAirport(airport);
            return "redirect:/";
        } catch (AirportException a) {
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @Transactional
    @RequestMapping(value = "/myFlights", method = RequestMethod.GET)
    public String getAuthenticatedFlightlist(Model model, @AuthenticationPrincipal User user) {
        try {
            List<Flightdetails> flights;
            if(user.getAccountType().equals(AccountType.EMPLOYEE)){
                flights=flightdetailsServiceIF.getAllFlights();
            }
            else{
                flights=flightdetailsServiceIF.getAllByUsername(user.getUsername());
            }
            model.addAttribute("flights",flights);
            return "authenticated/myFlights";
        } catch (Exception e) {
            model.addAttribute("UIerror", e);
            return "error";
        }
    }
}
