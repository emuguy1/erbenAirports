package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;

public class EmployeeSetup extends AbstractUserSetup {


    @Override
    boolean setup() throws ApplicationException {
        try{
            if(userService.userExists("root")){
                return true;
            }
            userService.registerUser(new User("root","123","Matteo","Hoffman","DE12345678901234500001","Germany","60306","Frankfurt","Hauptstraße","1","Lufthansa", AccountType.EMPLOYEE));
            userService.registerUser(new User("Admin","root","Thomas","Mandl","Germany","93055","Regensburg","Albertus-Magnus-Str.","80",AccountType.EMPLOYEE));
            return true;
        }catch(Exception e){
            throw new ApplicationException("Customer Setup failed. Couldnt create Customers");
        }
    }
}
