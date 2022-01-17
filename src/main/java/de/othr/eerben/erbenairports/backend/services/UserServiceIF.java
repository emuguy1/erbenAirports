package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceIF extends UserDetailsService {
    User registerUser(User user) throws AirportException;;
    User getUserByUsername(String username);

    boolean userExists(String username);

    boolean checkPassword(String password, User user);

    User saveUser(User user);
}
