package de.othr.eerben.erbenairports.backend.services.impl;

import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.data.entities.UserData;
import de.othr.eerben.erbenairports.backend.data.repositories.UserRepository;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements UserServiceIF {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User registerCustomer(User user) throws ApplicationException {
        //rewrite statment to something like exists
        if(userRepo.findByUsername(user.getUsername()).isPresent()){
            throw new ApplicationException("Error: This User already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return saveUser(user);
    }

    @Transactional
    @Override
    public User saveUser(User user){
        return userRepo.save(user);
    }

    @Override
    public User registerEmployee(User user) {
        //userRepo.save(user);
        return null;
    }

    @Override
    public User loginEmployee(UserData userData) {
        return null;
    }

    @Override
    public User loginCustomer(UserData userData) {
        return null;
    }
}
