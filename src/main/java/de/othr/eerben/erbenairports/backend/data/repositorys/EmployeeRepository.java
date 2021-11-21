package de.othr.eerben.erbenairports.backend.data.repositorys;

import de.othr.eerben.erbenairports.backend.data.entities.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmployeeRepository extends CrudRepository<Employee,Long> {
        Optional<Employee> findByUserName(String username);
        }