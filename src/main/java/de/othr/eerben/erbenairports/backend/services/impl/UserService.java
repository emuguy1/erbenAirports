package de.othr.eerben.erbenairports.backend.services.impl;

import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.repositories.UserRepository;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService implements UserServiceIF {

    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User registerUser(User user) throws AirportException {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            logger.error("Error: User already exists! " + user);
            throw new AirportException("Error: This User already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            return userRepo.save(user);
        } catch (Exception e) {
            logger.error("Error creating user. Exception " + e);
            throw new AirportException(e.getMessage());
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepo.existsById(username);
    }

    @Override
    public boolean checkPassword(String password, User user) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public List<User> getAllCustomers() {
        return userRepo.getAllCustomers();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(
                () -> new ServiceException("User with username " + username + " not found")
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username " + username + " not found")
        );
    }
}
