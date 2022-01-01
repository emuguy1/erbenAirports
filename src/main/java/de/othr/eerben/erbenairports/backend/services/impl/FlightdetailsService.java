package de.othr.eerben.erbenairports.backend.services.impl;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.repositories.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class FlightdetailsService implements FlightdetailsServiceIF {

    @Autowired
    private AirportServiceIF airportServiceIF;

    @Autowired
    private FlightdetailsRepository flightdetailsRepo;

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

//    @Override
//    public Collection<Flightdetails> getArrivalsPaginated(String airportcode) {
//        //TODO: get departures sorted and after a specific time
//        Airport airport= airportServiceIF.getAirportByAirportcode(airportcode);
//        return flightdetailsRepo.findByOriginOrderByArrivalTime(airport);
//    }

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
        //TODO:
        return null;
    }


}
