package de.othr.eerben.erbenairports.backend.services;

import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServiceIF extends UserDetailsService {
    User registerUser(User user) throws ApplicationException;;
    User getUserByUsername(String username);

    boolean userExists(String username);

    User saveUser(User user);
}
