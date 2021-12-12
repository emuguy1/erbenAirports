package de.othr.eerben.erbenairports.backend.services.impl;

import de.othr.eerben.erbenairports.backend.data.entities.Customer;
import de.othr.eerben.erbenairports.backend.data.entities.Employee;
import de.othr.eerben.erbenairports.backend.data.entities.UserData;
import de.othr.eerben.erbenairports.backend.data.repositories.CustomerRepository;
import de.othr.eerben.erbenairports.backend.data.repositories.EmployeeRepository;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceIF {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private EmployeeRepository employeeRepo;


    @Override
    public Customer registerCustomer(Customer customer) {
        //TODO:Mit Rückgabeparameter usw. noch schauen
        customerRepo.save(customer);
        return customer;
    }

    @Override
    public Employee registerEmployee(Employee employee) {
        employeeRepo.save(employee);
        return employee;
    }

    @Override
    public Employee loginEmployee(UserData userData) {
        return null;
    }

    @Override
    public Customer loginCustomer(UserData userData) {
        return null;
    }
}
