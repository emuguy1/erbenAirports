package de.othr.eerben.erbenairports.backend.data.repositorys;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import org.springframework.data.repository.CrudRepository;

public interface AirportRepository extends CrudRepository<Airport,Long> {

        }