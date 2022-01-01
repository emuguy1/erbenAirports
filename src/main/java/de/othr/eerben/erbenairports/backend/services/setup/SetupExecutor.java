package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SetupExecutor {

    @Autowired @Qualifier("customer")
    AbstractUserSetup customerSetupComponent;

    @Autowired @Qualifier("employee")
    AbstractUserSetup employeeSetupComponent;

    @Autowired
    AirportSetupComponent airportSetupComponent;

    @Autowired
    FlightdetailsSetupComponent flightdetailsSetupComponent;

    @PostConstruct
    public void executeSetup(){
        try {
            airportSetupComponent.setup();
            flightdetailsSetupComponent.setup();
            customerSetupComponent.setup();
            employeeSetupComponent.setup();

            System.out.println("Setup finished!");
        }
        catch(ApplicationException exception){
            System.out.println("Setup failed!");
        }

    }

}
