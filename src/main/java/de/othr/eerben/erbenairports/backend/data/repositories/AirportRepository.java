package de.othr.eerben.erbenairports.backend.data.repositories;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends CrudRepository<Airport,String> {
        Optional<Airport> findByAirportcode(String airportcode);
        List<Airport> findDistinctByAirportcodeIsNotNull();
        boolean existsAirportByAirportcode(String airportcode);
}