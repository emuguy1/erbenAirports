package de.othr.eerben.erbenairports.frontend.controller;


import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import de.othr.eerben.erbenairports.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Controller
@Scope("singleton")
public class FlightsController {

    Logger logger = LoggerFactory.getLogger(FlightsRestController.class);
    @Autowired
    private FlightdetailsServiceIF flightdetailsServiceIF;
    @Autowired
    private AirportServiceIF airportServiceIF;

    @RequestMapping(value = "/departures", method = RequestMethod.GET)
    public String departures(Model model, @RequestParam(value = "airport", required = false) String airportcode, @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size) {
        logger.info("GET /departures/" + airportcode);
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
                model.addAttribute("pageNumbers", Helper.pageNumbers(currentPage, totalPages));
            }
            model.addAttribute("flights", flightPage.toList());
            model.addAttribute("airports", airports);
            model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
            model.addAttribute("selectedAirport", new Airport());
            return "unauthenticated/departures";
        } catch (AirportException e) {
            logger.error("Error at departures. Message: " + e);
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
        logger.info("GET /arrivals/" + airportcode);
        try {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(5);

            Page<Flightdetails> flightPage;
            List<Airport> airports = airportServiceIF.getAllAirports();
            if (airportcode != null && !airportcode.isEmpty() && !airportcode.isBlank() && !airportcode.equals("null")) {
                if (page.isEmpty() || size.isEmpty()) {
                    return "redirect:/arrivals?airport=" + airportcode + "&size=" + pageSize + "&page=" + currentPage;
                } else if (airportServiceIF.getAirportByAirportcode(airportcode) == null) {
                    model.addAttribute("UIerror", new AirportException("Wrong Airportnumber specified", "Try clicking on arrivals and then select your wanted airport from the dropdown list."));
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
                model.addAttribute("pageNumbers", Helper.pageNumbers(currentPage, totalPages));
            }
            model.addAttribute("flights", flightPage.toList());
            model.addAttribute("airports", airports);
            model.addAttribute("currentAirport", airportServiceIF.getAirportByAirportcode(airportcode));
            model.addAttribute("selectedAirport", new Airport());
            return "unauthenticated/arrivals";
        } catch (AirportException e) {
            logger.error("Error at arrivals. Message: " + e);
            model.addAttribute("UIerror", new AirportException("Wrong Airportcode specified", "Try clicking on arrivals and then select your wanted airport from the dropdown list."));
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
        logger.info("GET /flight/new");
        model.addAttribute("airports", airportServiceIF.getAllAirports());
        model.addAttribute("flightdetails", new FlighttransactionDTO());
        return "flight/new";
    }

    @RequestMapping(value = "/flight/new", method = RequestMethod.POST)
    public String addFlight(Model model, @AuthenticationPrincipal User user, @ModelAttribute("flightdetails") FlighttransactionDTO flightdetailsdto) {
        logger.info("POST /flight/new");
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
            logger.error("Error at creating new Flight: " + e);
            return "flight/new";
        }

    }

    @RequestMapping(value = "/flight/{id}/details", method = RequestMethod.GET)
    public String getFlightdetails(Model model, @PathVariable("id") long flightid) {
        logger.info("GET /flight/" + flightid + "/details");
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
            logger.error("error at getting flightdetails: " + a);
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @RequestMapping(value = "/flight/{id}/delete", method = RequestMethod.GET)
    public String deleteFlightdetails(Model model, @AuthenticationPrincipal User user, @PathVariable("id") long flightid) {
        logger.info("GET /flight/" + flightid + "/delete");
        try {
            Flightdetails flight = flightdetailsServiceIF.getFlightdetailsById(flightid);
            flightdetailsServiceIF.cancelFlight(user, Helper.getFlighttransactionDTO(flight));
            return "redirect:/";
        } catch (AirportException a) {
            logger.error("Flight couldnt be deleted. Message: " + a);
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @RequestMapping(value = "/airport/new", method = RequestMethod.GET)
    public String addAirport(Model model) {
        logger.info("GET /airport/new");
        model.addAttribute("timezoneIDs", TimeZone.getAvailableIDs());
        model.addAttribute("airport", new Airport());
        return "airport/new";
    }

    @RequestMapping(value = "/airport/new", method = RequestMethod.POST)
    public String saveAirport(Model model, @ModelAttribute("airport") Airport airport) {
        logger.info("POST /airport/new for: " + airport.getAirportcode());
        try {
            Airport savedAirport = airportServiceIF.addAirport(airport);
            return "redirect:/airport/" + savedAirport.getAirportcode() + "/details";
        } catch (AirportException a) {
            logger.error("Airport couldnt be created. Message: " + a);
            model.addAttribute("UIerror", a);
            return "error";
        }

    }

    @RequestMapping(value = "/airport/{id}/details", method = RequestMethod.GET)
    public String getAirportdetails(Model model, @PathVariable("id") String airportcode) {
        logger.info("GET /airport/" + airportcode + "/details");
        try {
            model.addAttribute("airport", airportServiceIF.getAirportByAirportcode(airportcode));
            return "airport/details";
        } catch (AirportException a) {
            logger.error("Error at Airportdetails. Message: " + a);
            model.addAttribute("UIerror", a);
            return "error";
        }

    }

    @RequestMapping(value = "/airport/{id}/edit", method = RequestMethod.GET)
    public String editAirport(Model model, @PathVariable("id") String airportcode) {
        logger.info("GET /airport/" + airportcode + "/edit");
        try {
            model.addAttribute("airport", airportServiceIF.getAirportByAirportcode(airportcode));
            model.addAttribute("edit", true);
            model.addAttribute("timezoneIDs", TimeZone.getAvailableIDs());
            return "airport/edit";
        } catch (AirportException a) {
            logger.error("Couldnt call Airportedit for " + airportcode);
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @RequestMapping(value = "/airport/edit", method = RequestMethod.POST)
    public String saveeditedAirport(Model model, @ModelAttribute("airport") Airport airport) {
        logger.info("POST /airport/edit for " + airport.getAirportcode());
        try {
            airportServiceIF.updateAirport(airport);
            return "redirect:/airport/" + airport.getAirportcode() + "/details";
        } catch (AirportException a) {
            logger.error("Error at saving edited airport: " + airport.getAirportcode());
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @RequestMapping(value = "/airport/{id}/delete", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") String airport) {
        logger.info("GET /airport/" + airport + "/delete");
        try {
            flightdetailsServiceIF.deleteByAirportId(airport);
            return "redirect:/";
        } catch (AirportException a) {
            logger.error("Couldnt delete airport:" + airport);
            model.addAttribute("UIerror", a);
            return "error";
        }
    }

    @RequestMapping(value = "/myFlights", method = RequestMethod.GET)
    public String getAuthenticatedFlightlistPageable(Model model, @AuthenticationPrincipal User user, @RequestParam("page") Optional<Integer> page,
                                                     @RequestParam("size") Optional<Integer> size) {
        logger.info("GET /myFlights");
        try {
            int currentPage = page.orElse(1);
            int pageSize = size.orElse(25);
            Page<Flightdetails> flightPage;

            if (page.isEmpty() || size.isEmpty()) {
                return "redirect:/myFlights?size=" + pageSize + "&page=" + currentPage;
            }

            if (user.getAccountType().equals(AccountType.EMPLOYEE)) {
                flightPage = flightdetailsServiceIF.getAllFlights(PageRequest.of(currentPage - 1, pageSize));
            } else {
                flightPage = flightdetailsServiceIF.getAllByUsername(user.getUsername(), PageRequest.of(currentPage - 1, pageSize));
            }
            model.addAttribute("flightPage", flightPage);

            int totalPages = flightPage.getTotalPages();
            if (totalPages > 0) {
                model.addAttribute("pageNumbers", Helper.pageNumbers(currentPage, totalPages));
            }


            model.addAttribute("flights", flightPage.toList());
            return "authenticated/myFlights";
        } catch (Exception e) {
            logger.error("Couldnt call myFlights. Message: " + e.getMessage());
            model.addAttribute("UIerror", e);
            return "error";
        }
    }
}
