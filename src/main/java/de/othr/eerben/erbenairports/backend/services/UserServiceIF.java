package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.Customer;
import de.othr.eerben.erbenairports.backend.data.entities.Employee;
import de.othr.eerben.erbenairports.backend.data.entities.UserData;

public interface UserServiceIF {
    Customer registerCustomer(Customer customer);
    Employee registerEmployee(Employee employee);
    Employee loginEmployee(UserData userData);
    Customer loginCustomer(UserData userData);
}
