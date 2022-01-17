package de.othr.eerben.erbenairports.backend.data.repositories;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightdetailsRepository extends CrudRepository<Flightdetails, Long> {
    Collection<Flightdetails> findByDepartureOrderByDepartureTime(Airport airport);
    @Query("select f from Flightdetails f where f.departure = ?1 and f.departureTime.startTime > ?2 order by f.departureTime.startTime ASC")
    List<Flightdetails> getAllByDepartureAndDepartureTimeIsAfterOrderByDepartureTime(Airport departure, Timestamp departureTime);
    @Query("select f from Flightdetails f where f.origin = ?1 and f.arrivalTime.startTime > ?2 order by f.arrivalTime.startTime")
    Optional<List<Flightdetails>> getAllByOriginAndArrivalTimeIsAfterOrderByArrivalTime(Airport departure, Timestamp arrivalTime);
    Collection<Flightdetails> findByOriginOrderByArrivalTime(Airport airport);
    Optional<List<Flightdetails>> findByFlightnumber(String flightnumber);
    Optional<Flightdetails> findByFlightid(long flightid);
    boolean existsFlightdetailsByFlightnumber(String flightnumber);

    @Query("select f from Flightdetails f where f.origin.airportcode = ?1 or f.departure.airportcode = ?1")
    Optional<List<Flightdetails>> getAllByAirport(String airport);

    @Query("select (count(f) > 0) from Flightdetails f where f.arrivalTime.startTime < ?1 or f.departureTime.startTime > ?1 and f.departure.airportcode = ?2 or f.origin.airportcode = ?2")
    boolean getAllByAirportWhereArrivalTimeAfterAndDepartureTimeBefore(Date date, String airport);

    @Query("select f from Flightdetails f where f.departureTime.startTime = ?1 and f.departure.airportcode = ?2 and f.origin.airportcode = ?3 and f.flightnumber = ?4 and f.arrivalTime.startTime = ?5 and f.customer = ?6")
    Optional<Flightdetails> getFlightdetailsByDepartureTimeAndDepartureAndOriginAndFlightnumberAndAndArrivalTimeAndCustomer(Date departureTime, String departure, String origin, String flightnumber, Date ArrivalTime, User customer);

    @Query("select f from Flightdetails f where f.departureTime.startTime = ?1 and f.departure.airportcode = ?2 and f.origin.airportcode = ?3 and f.flightnumber = ?4 and f.arrivalTime.startTime = ?5")
    Optional<Flightdetails> getFlightdetailsByDepartureTimeAndDepartureAndOriginAndFlightnumberAndAndArrivalTime(Date departureTime, String departure, String origin, String flightnumber, Date ArrivalTime);
}
