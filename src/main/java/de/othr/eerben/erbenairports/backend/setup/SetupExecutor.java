package de.othr.eerben.erbenairports.backend.setup;

import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SetupExecutor {

    @Autowired
    @Qualifier("customer")
    AbstractUserSetup customerSetupComponent;

    @Autowired
    @Qualifier("employee")
    AbstractUserSetup employeeSetupComponent;

    @Autowired
    AirportSetupComponent airportSetupComponent;

    @Autowired
    FlightdetailsSetupComponent flightdetailsSetupComponent;

    Logger logger = LoggerFactory.getLogger(SetupExecutor.class);

    @PostConstruct
    public void executeSetup() {
        try {
            airportSetupComponent.setup();
            logger.info("Airport Setup successfully completed!");

            customerSetupComponent.setup();
            logger.info("Customer Setup successfully completed!");

            employeeSetupComponent.setup();
            logger.info("Employee Setup successfully completed!");

            flightdetailsSetupComponent.setup();
            logger.info("Flightdetails Setup successfully completed!");

            logger.info("Application Setup completly finished!");
        } catch (AirportException exception) {
            logger.error("Setup failed!");
        }

    }

}
