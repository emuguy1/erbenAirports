package de.othr.eerben.erbenairports.backend.setup;

import de.othr.eerben.erbenairports.backend.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractUserSetup extends AbstractSetupComponent {
    @Autowired
    UserService userService;
}
