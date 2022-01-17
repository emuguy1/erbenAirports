package de.othr.eerben.erbenairports.backend.services.impl;

import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.repositories.UserRepository;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserServiceIF{

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User registerUser(User user) throws AirportException {
        //TODO: rewrite statment to something like exists
        //TODO: validation after Role Employee or Customer
        if(userRepo.findByUsername(user.getUsername()).isPresent()){
            throw new AirportException("Error: This User already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            return userRepo.save(user);
        }
        catch (Exception e){
            throw new AirportException(e.getMessage());
        }
    }

    @Override
    public boolean userExists(String username){
        return  userRepo.existsById(username);
    }

    @Override
    public boolean checkPassword(String password, User user) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    @Override
    public User saveUser(User user){
        return userRepo.save(user);
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
