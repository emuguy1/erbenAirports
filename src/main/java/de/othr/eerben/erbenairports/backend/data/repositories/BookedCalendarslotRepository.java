package de.othr.eerben.erbenairports.backend.data.repositories;

import de.othr.eerben.erbenairports.backend.data.entities.BookedCalendarslot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface BookedCalendarslotRepository extends CrudRepository<BookedCalendarslot, Long> {
    @Query("select b from BookedCalendarslot b where b.airport.airportcode = ?1 and b.startTime = ?2")
    Optional<BookedCalendarslot> getBookedCalendarslotByAirportAndStartTime(String airportcode, Date starttime);

}
