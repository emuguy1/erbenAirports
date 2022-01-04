package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;

public class CustomerSetup extends AbstractUserSetup {


    @Override
    boolean setup() throws ApplicationException {
        userService.registerEmployee(new User("root2","123", AccountType.CUSTOMER));
        return true;
    }
}
