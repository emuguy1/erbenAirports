package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;

public class CustomerSetup extends AbstractUserSetup {

    @Override
    boolean setup() throws AirportException {
        try{
            if(userService.userExists("root2")){
                return true;
            }
            userService.registerUser(new User("root2","123", "Matteo","Hoffmann","DE12345678901234500902","Germany","60306","Frankfurt","Hauptstraße","1","Lufthansa",AccountType.CUSTOMER));
            userService.registerUser(new User("Lufthansa","lufthansa123","Lufthansa","GmbH","DE12345678901234500903","Germany","60306","Frankfurt","Hauptstraße","1","Lufthansa",AccountType.CUSTOMER));
            userService.registerUser(new User("Ryanair","ryanair123","Ryanair","Trademark","DE12345678901234500901","Irland","D01","Dublin","Oxfordstreet","20","Ryanair",AccountType.CUSTOMER));
            userService.registerUser(new User("HoffmannAirways","matteo","Matteo","Hoffmann","DE12345678901234500007","Germany","93055","Regensburg","Maximilianstraße","12A","Hoffmann Airways",AccountType.CUSTOMER));
            return true;
        }catch(Exception e){
            throw new AirportException("Customer Setup failed. Couldnt create Customers");
        }
    }
}
