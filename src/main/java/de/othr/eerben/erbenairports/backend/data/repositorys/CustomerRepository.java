package de.othr.eerben.erbenairports.backend.data.repositorys;

import de.othr.eerben.erbenairports.backend.data.entities.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer,Long> {
    Optional<Customer> findBySurname(String surname);
}