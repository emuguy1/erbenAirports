package de.othr.eerben.erbenairports.backend.services.impl;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.BookedCalendarslot;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlightdetailsDTO;
import de.othr.eerben.erbenairports.backend.data.repositories.BookedCalendarslotRepository;
import de.othr.eerben.erbenairports.backend.data.repositories.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

@Service
public class FlightdetailsService implements FlightdetailsServiceIF {

    @Autowired
    private AirportServiceIF airportServiceIF;

    @Autowired
    private UserServiceIF userServiceIF;

    @Autowired
    private FlightdetailsRepository flightdetailsRepo;

    @Autowired
    private BookedCalendarslotRepository calendarslotRepository;


    @Override
    public Page<Flightdetails> getDeparturesPaginated(String airportcode, Pageable pageable) throws ApplicationException {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        //TODO: get departures after a specific time
        Airport airport = airportServiceIF.getAirportByAirportcode(airportcode);
        Collection<Flightdetails> flights = flightdetailsRepo.getAllByDepartureAndDepartureTimeIsAfterOrderByDepartureTime(airport, Timestamp.from(Instant.now())).orElseThrow(() -> new ApplicationException("Error, no Departures for airport after now could be found"));
        System.out.println(flights);
        List<Flightdetails> list;

        if (flights.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, flights.size());
            list = flights.stream().toList().subList(startItem, toIndex);
        }

        Page<Flightdetails> flightsPage
                = new PageImpl<Flightdetails>(list, PageRequest.of(currentPage, pageSize), flights.size());

        return flightsPage;
    }

    @Override
    public Page<Flightdetails> getArrivalsPaginated(String airportcode, Pageable pageable) throws ApplicationException {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        //TODO: get arrivals after a specific time
        Airport airport = airportServiceIF.getAirportByAirportcode(airportcode);
        Collection<Flightdetails> flights = flightdetailsRepo.getAllByOriginAndArrivalTimeIsAfterOrderByArrivalTime(airport, Timestamp.from(Instant.now())).orElseThrow(() -> new ApplicationException("Error, no Arrivals for airport after now could be found"));
        System.out.println(flights);
        List<Flightdetails> list;

        if (flights.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, flights.size());
            list = flights.stream().toList().subList(startItem, toIndex);
        }

        Page<Flightdetails> flightsPage
                = new PageImpl<Flightdetails>(list, PageRequest.of(currentPage, pageSize), flights.size());

        return flightsPage;
    }

    @Override
    public Collection<Flightdetails> getFlightdetails(String flightnumber) {
        return flightdetailsRepo.findByFlightnumber(flightnumber).orElseThrow();
    }

    @Override
    public Optional<Flightdetails> getFlightdetailsById(long flightid) {
        return flightdetailsRepo.findByFlightid(flightid);
    }


    @Override
    public boolean cancleFlight(User user, String flightnumber) {
        //TODO:
        return false;
    }

    @Override
    public Flightdetails bookFlight(User user, Flightdetails flightdetails) {
        //userServiceIF.
        //TODO:
        return null;
    }

    @Override
    public Flightdetails updateFlight(Flightdetails flightdetails) throws ApplicationException {
        Optional<Flightdetails> oldAirportOptional = flightdetailsRepo.findById(flightdetails.getFlightid());
        if (oldAirportOptional.isPresent()) {
            Flightdetails oldFlightdetails = oldAirportOptional.get();
            //TODO notify customers, call airport etc.
            return flightdetailsRepo.save(flightdetails);
        } else {
            System.out.println("Flight not found " + flightdetails.getFlightid());
            return null;
        }
    }



    @Override
    public Flightdetails bookFlight(FlightdetailsDTO flightdetails) throws ApplicationException {

        try {
            //TODO: try-catch
            //check necessary inputs

            Airport departureAirport = airportServiceIF.getAirportByAirportcode(flightdetails.getDeparture());
            Airport originAirport = airportServiceIF.getAirportByAirportcode(flightdetails.getOrigin());

            //get departure time
            Instant departureInstant=flightdetails.getDepartureTime().atZone(ZoneId.of(departureAirport.getTimeZone())).toInstant();

            //Time is safed in local timezone time on Database and then displayed in Airport localtimezone
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
            calendar.setTime(Date.from(departureInstant));
            calendar.set(Calendar.SECOND, 0);


            //round to full 5 min upwards
            int cleanedMinutes = (int) Math.floor((calendar.get(Calendar.MINUTE) + 2.5) / 5) * 5;
            calendar.set(Calendar.MINUTE, cleanedMinutes);
            Date wishedDeparture = calendar.getTime();

            //get approximated Arrivaltime rounded up to full 5min slot
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(wishedDeparture);
            calendar1.add(Calendar.MINUTE, (int) Math.ceil(flightdetails.getFlightTimeHours() * 60));
            calendar1.set(Calendar.MINUTE, (int) Math.floor((calendar1.get(Calendar.MINUTE) + 2.5) / 5) * 5);
            Date approximatedArrivalTime = calendar1.getTime();

            //check for available timeslot at departure and origin and reshedule if necessary max to 60 min after/before wished

            boolean departurefree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getDeparture(), wishedDeparture).isEmpty();
            boolean arrivalfree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getOrigin(), approximatedArrivalTime).isEmpty();

            int i = 0;
            while ((!departurefree || !arrivalfree) && i < 12) {
                i++;
                calendar.add(Calendar.MINUTE, 5);
                wishedDeparture = calendar.getTime();
                calendar1.add(Calendar.MINUTE, 5);
                approximatedArrivalTime = calendar.getTime();
                departurefree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getDeparture(), wishedDeparture).isEmpty();
                arrivalfree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getOrigin(), approximatedArrivalTime).isEmpty();
            }
            if (!departurefree || !arrivalfree) {
                i = 0;
                calendar.add(Calendar.MINUTE, -60);
                calendar1.add(Calendar.MINUTE, -60);
                while ((!departurefree || !arrivalfree) && i > -12) {
                    i--;
                    calendar.add(Calendar.MINUTE, -5);
                    wishedDeparture = calendar.getTime();
                    calendar1.add(Calendar.MINUTE, -5);
                    approximatedArrivalTime = calendar.getTime();
                    departurefree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getDeparture(), wishedDeparture).isEmpty();
                    arrivalfree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getOrigin(), approximatedArrivalTime).isEmpty();
                }
            }
            if (!departurefree || !arrivalfree) {
                throw new ApplicationException("Found no possible timeslot  in plus/minus 1 hour of wished flightslot");
            }
            //create bokedTimeslot
            BookedCalendarslot calendarslotDeparture = new BookedCalendarslot(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), 5, calendar.getTime(), airportServiceIF.getAirportByAirportcode(flightdetails.getDeparture()));
            BookedCalendarslot calendarslotArrival = new BookedCalendarslot(calendar1.get(Calendar.DAY_OF_MONTH), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.YEAR), 5, calendar1.getTime(), airportServiceIF.getAirportByAirportcode(flightdetails.getOrigin()));

            calendarslotRepository.save(calendarslotDeparture);
            calendarslotRepository.save(calendarslotArrival);
            //TODO:add user who created it/ created it for

            //save flightdetails
            Flightdetails flightdetails1 = new Flightdetails(flightdetails.getFlightnumber(), flightdetails.getFlightTimeHours(), flightdetails.getMaxCargo(), flightdetails.getPassangerCount(), departureAirport, originAirport, calendarslotDeparture, calendarslotArrival);
            flightdetails1 = flightdetailsRepo.save(flightdetails1);
            //TODO:add transaktion in trbank

            return flightdetails1;
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    @Override
    public void deleteByAirportId(String airport) throws ApplicationException {
        Collection<Flightdetails> toBeCanceledFlights = flightdetailsRepo.getAllByAirport(airport).orElseThrow(() -> new ApplicationException("Error, no Departures for airport after now could be found"));
        Collection<BookedCalendarslot> toBeDeletedCalendarslots = new ArrayList<>();
        toBeCanceledFlights.stream().toList().forEach(flight -> toBeDeletedCalendarslots.add(flight.getArrivalTime()));
        toBeCanceledFlights.stream().toList().forEach(flight -> toBeDeletedCalendarslots.add(flight.getDepartureTime()));
        flightdetailsRepo.deleteAll(toBeCanceledFlights);
        calendarslotRepository.deleteAll(toBeDeletedCalendarslots);
    }


}
