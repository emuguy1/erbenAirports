package de.othr.eerben.erbenairports.backend.data.repositories;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FlightdetailsRepository extends CrudRepository<Flightdetails,String> {
    Collection<Flightdetails> findByDepartureOrderByDepartureTime(Airport airport);
    //Collection<Flightdetails> findByDepartureAndDepartureTimeIsAfterWithOrderByDepartureTime(Airport airport, LocalDateTime departureTime);
    Collection<Flightdetails> findByOriginOrderByArrivalTime(Airport airport);
    Flightdetails findByFlightnumber(String flightnumber);
}
