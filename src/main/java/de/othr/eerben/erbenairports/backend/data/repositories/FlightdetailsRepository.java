package de.othr.eerben.erbenairports.backend.data.repositories;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//Chose PagingAndSortingRepository as extension for CrudRepository, as it is a native extension for pageable support
@Repository
public interface FlightdetailsRepository extends PagingAndSortingRepository<Flightdetails, Long> {
    @Query("select f from Flightdetails f where f.departureAirport = ?1 and f.departureTime.startTime > ?2 order by f.departureTime.startTime ASC")
    List<Flightdetails> getAllByDepartureAirportAndDepartureTimeIsAfterOrderByDepartureTime(Airport departureAirport, Timestamp departureTime);

    @Query("select f from Flightdetails f where f.arrivalAirport = ?1 and f.arrivalTime.startTime > ?2 order by f.arrivalTime.startTime")
    List<Flightdetails> getAllByArrivalAirportAndArrivalTimeIsAfterOrderByArrivalTime(Airport departureAirport, Timestamp arrivalTime);

    Optional<Flightdetails> findByFlightid(long flightid);

    boolean existsFlightdetailsByFlightnumber(String flightnumber);

    @Query("select f from Flightdetails f where f.arrivalAirport.airportcode = ?1 or f.departureAirport.airportcode = ?1")
    List<Flightdetails> getAllByAirport(String airport);


    @Query("select f from Flightdetails f")
    Page<Flightdetails> findAll(Pageable pageable);

    @Query("select f from Flightdetails f where f.customer.username = ?1")
    Page<Flightdetails> getAllByUsername(String username, Pageable pageable);

    @Query("select (count(f) > 0) from Flightdetails f where f.arrivalTime.startTime < ?1 or f.departureTime.startTime > ?1 and f.departureAirport.airportcode = ?2 or f.arrivalAirport.airportcode = ?2")
    boolean getAllByAirportWhereArrivalTimeAfterAndDepartureTimeBefore(Date date, String airport);

    @Query("select f from Flightdetails f where f.departureTime.startTime = ?1 and f.departureAirport.airportcode = ?2 and f.arrivalAirport.airportcode = ?3 and f.flightnumber = ?4 and f.arrivalTime.startTime = ?5 and f.customer = ?6")
    Optional<Flightdetails> getFlightdetailsByDepartureTimeAndDepartureAirportAndArrivalAirportAndFlightnumberAndAndArrivalTimeAndCustomer(Date departureTime, String departureAirport, String arrivalAirport, String flightnumber, Date ArrivalTime, User customer);

    @Query("select f from Flightdetails f where f.departureTime.startTime = ?1 and f.departureAirport.airportcode = ?2 and f.arrivalAirport.airportcode = ?3 and f.flightnumber = ?4 and f.arrivalTime.startTime = ?5")
    Optional<Flightdetails> getFlightdetailsByDepartureTimeAndDepartureAirportAndArrivalAirportAndFlightnumberAndAndArrivalTime(Date departureTime, String departureAirport, String arrivalAirport, String flightnumber, Date ArrivalTime);
}
