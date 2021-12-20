package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.UserData;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;

public interface UserServiceIF {
    User registerCustomer(User user) throws ApplicationException;
    User registerEmployee(User user);
    User loginEmployee(UserData userData);
    User loginCustomer(UserData userData);
    User saveUser(User user);
}
