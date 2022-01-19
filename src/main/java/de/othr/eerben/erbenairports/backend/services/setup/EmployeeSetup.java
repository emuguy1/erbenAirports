package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;

public class EmployeeSetup extends AbstractUserSetup {


    @Override
    boolean setup() throws AirportException {
        try {
            if (userService.userExists("root")) {
                logger.info("Employee Setup skipped. As root user already exists");
                return true;
            }
            userService.registerUser(new User("root", "123passwort123", "Andreas", "Weiß", "Germany", "60306", "Frankfurt", "Weinweg", "27", AccountType.EMPLOYEE));
            userService.registerUser(new User("Admin", "123passwort123", "Thomas", "Mandl", "Germany", "93055", "Regensburg", "Albertus-Magnus-Str.", "80", AccountType.EMPLOYEE));
            return true;
        } catch (Exception e) {
            logger.error("Employee Setup failed. Error Message: " + e.getMessage());
            throw new AirportException("Employee Setup failed. Couldnt create Employees");
        }
    }
}
