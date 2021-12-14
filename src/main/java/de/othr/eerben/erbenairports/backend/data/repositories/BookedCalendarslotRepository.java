package de.othr.eerben.erbenairports.backend.data.repositories;

import de.othr.eerben.erbenairports.backend.data.entities.BookedCalendarslot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedCalendarslotRepository extends CrudRepository<BookedCalendarslot,Long>  {
}
