package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractUserSetup {
    @Autowired
    UserService userService;
}
