package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SetupExecutor {
    @Autowired
    AirportSetupComponent airportSetupComponent;

    @Autowired
    FlightdetailsSetupComponent flightdetailsSetupComponent;

    @PostConstruct
    public void executeSetup(){
        try {
            airportSetupComponent.setup();
            flightdetailsSetupComponent.setup();
            System.out.println("Setup finished!");
        }
        catch(ApplicationException exception){
            System.out.println("Setup failed!");
        }

    }

}
