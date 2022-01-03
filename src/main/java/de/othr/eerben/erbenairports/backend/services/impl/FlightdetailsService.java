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

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
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
    public Collection<Flightdetails> getDepartures(String airportcode) throws ApplicationException{
        //TODO: get departures sorted and after a specific time
        Airport airport= airportServiceIF.getAirportByAirportcode(airportcode);
        return flightdetailsRepo.getAllByDepartureAndDepartureTimeIsAfterOrderByDepartureTime(airport, Timestamp.from(Instant.now())).orElseThrow(()-> new ApplicationException("Error, no Departures for airport after now could be found"));
    }

    @Override
    public Page<Flightdetails> getDeparturesPaginated(String airportcode, Pageable pageable) throws ApplicationException {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;


        //TODO: get departures sorted and after a specific time
        Airport airport= airportServiceIF.getAirportByAirportcode(airportcode);
        Collection<Flightdetails> flights= flightdetailsRepo.getAllByDepartureAndDepartureTimeIsAfterOrderByDepartureTime(airport, Timestamp.from(Instant.now())).orElseThrow(()-> new ApplicationException("Error, no Departures for airport after now could be found"));
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
    public Collection<Flightdetails> getArrivals(String airportcode) throws ApplicationException {
        //TODO: get departures sorted and after a specific time
        Airport airport= airportServiceIF.getAirportByAirportcode(airportcode);
        return flightdetailsRepo.findByOriginOrderByArrivalTime(airport);
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
    public Flightdetails getFlightdetails(String flightnumber) {
        return flightdetailsRepo.findByFlightnumber(flightnumber).orElseThrow().stream().findFirst().get();
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
    public Flightdetails bookFlight(FlightdetailsDTO flightdetails) throws ApplicationException {

        //TODO: try-catch
        Airport departureAirport = airportServiceIF.getAirportByAirportcode(flightdetails.getDeparture());
        Airport originAirport = airportServiceIF.getAirportByAirportcode(flightdetails.getOrigin());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(flightdetails.getDepartureTime());
        calendar.set(Calendar.SECOND,0);
        //calendar.add(Calendar.MINUTE, (int) (connection.getFlightTimeHours() * 60));

        //check necessary inputs
        //get departure time
        //round to full 5 min upwards

        int cleanedMinutes=(int)Math.floor((calendar.get(Calendar.MINUTE) + 2.5) / 5) * 5;
        calendar.set(Calendar.MINUTE,cleanedMinutes);
        Date wishedDeparture=calendar.getTime();

        Date approximatedArrivalTime=Date.from(wishedDeparture.toInstant().plus(Duration.ofMinutes((long)(flightdetails.getFlightTimeHours()*60))));
        Calendar calendar1= Calendar.getInstance();
        calendar1.setTime(approximatedArrivalTime);

        //check for available timeslot at departure and origin and reshedule if necessary max to 60 min after/before wished

        boolean departurefree=calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getDeparture(), wishedDeparture).isEmpty();

        boolean arrivalfree=calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getOrigin(), approximatedArrivalTime).isEmpty();
        //create bokedTimeslot
        BookedCalendarslot calendarslotDeparture= new BookedCalendarslot(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR),5,calendar.getTime(),airportServiceIF.getAirportByAirportcode(flightdetails.getDeparture()));
        BookedCalendarslot calendarslotArrival= new BookedCalendarslot(calendar1.get(Calendar.DAY_OF_MONTH), calendar1.get(Calendar.MONTH),calendar1.get(Calendar.YEAR),5,calendar1.getTime(),airportServiceIF.getAirportByAirportcode(flightdetails.getOrigin()));
        if(departurefree && arrivalfree){
            calendarslotDeparture=calendarslotRepository.save(calendarslotDeparture);
            calendarslotArrival=calendarslotRepository.save(calendarslotArrival);
        }
        //TODO:add user who created it/ created it for

        //save flightdetails
        Flightdetails flightdetails1= new Flightdetails(flightdetails.getFlightnumber(), flightdetails.getFlightTimeHours(), flightdetails.getMaxCargo(), flightdetails.getPassangerCount(), departureAirport,originAirport,calendarslotDeparture,calendarslotArrival);
        flightdetails1=flightdetailsRepo.save(flightdetails1);
        //TODO:add transaktion in trbank

        //return flightdetails

        return flightdetails1;
    }


}
