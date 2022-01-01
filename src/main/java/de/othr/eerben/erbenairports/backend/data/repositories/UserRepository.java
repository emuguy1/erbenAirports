package de.othr.eerben.erbenairports.backend.data.repositories;


import de.othr.eerben.erbenairports.backend.data.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,String> {
    Optional<User> findByUsername(String username);
}
