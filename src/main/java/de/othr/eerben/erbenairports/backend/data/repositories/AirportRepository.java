package de.othr.eerben.erbenairports.backend.data.repositories;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AirportRepository extends CrudRepository<Airport,Long> {
        Optional<Airport> findByAirportcode(String airportcode);
        Optional<Collection<Airport>> findDistinctByAirportcodeIsNotNull();
        boolean existsAirportByAirportcode(String airportcode);
}