package de.othr.eerben.erbenairports.backend.data.repositories;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.BookedCalendarslot;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Repository
public interface FlightdetailsRepository extends CrudRepository<Flightdetails,String> {
    Collection<Flightdetails> findByDepartureOrderByDepartureTime(Airport airport);
    @Query("select f from Flightdetails f where f.departure = ?1 and f.departureTime.startTime > ?2 order by f.departureTime.startTime")
    Optional<Collection<Flightdetails>> getAllByDepartureAndDepartureTimeIsAfterOrderByDepartureTime(Airport departure, Timestamp departureTime);
    //Collection<Flightdetails> findByDepartureOrderByDepartureTimeAfter(Airport airport, Date date);
    //Collection<Flightdetails> findByDepartureAndDepartureTimeIsAfterWithOrderByDepartureTime(Airport airport, LocalDateTime departureTime);
    Collection<Flightdetails> findByOriginOrderByArrivalTime(Airport airport);
    Optional<Collection<Flightdetails>> findByFlightnumber(String flightnumber);
}
