package de.othr.eerben.erbenairports.backend.data.repositories;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface FlightdetailsRepository extends CrudRepository<Flightdetails, Long> {
    Collection<Flightdetails> findByDepartureOrderByDepartureTime(Airport airport);
    @Query("select f from Flightdetails f where f.departure = ?1 and f.departureTime.startTime > ?2 order by f.departureTime.startTime ASC")
    Optional<Collection<Flightdetails>> getAllByDepartureAndDepartureTimeIsAfterOrderByDepartureTime(Airport departure, Timestamp departureTime);
    @Query("select f from Flightdetails f where f.origin = ?1 and f.arrivalTime.startTime > ?2 order by f.arrivalTime.startTime")
    Optional<Collection<Flightdetails>> getAllByOriginAndArrivalTimeIsAfterOrderByArrivalTime(Airport departure, Timestamp arrivalTime);
    Collection<Flightdetails> findByOriginOrderByArrivalTime(Airport airport);
    Optional<Collection<Flightdetails>> findByFlightnumber(String flightnumber);
    Optional<Flightdetails> findByFlightid(long flightid);
    boolean existsFlightdetailsByFlightnumber(String flightnumber);

    @Query("select f from Flightdetails f where f.origin.airportcode = ?1 or f.departure.airportcode = ?1")
    Optional<Collection<Flightdetails>> getAllByAirport(String airport);
}
