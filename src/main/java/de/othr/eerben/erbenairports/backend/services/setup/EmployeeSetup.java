package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;

public class EmployeeSetup extends AbstractUserSetup {
    @Override
    boolean setup() throws ApplicationException {
        System.out.println(AccountType.EMPLOYEE.name());
        userService.registerEmployee(new User("root","123", AccountType.EMPLOYEE));
        return true;
    }
}
