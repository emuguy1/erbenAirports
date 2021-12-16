package de.othr.eerben.erbenairports.backend.services.setup;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SetupFactory {


    @Bean
    public AirportSetupComponent createAirportService(){
        return new AirportSetupComponent();
    }

    @Bean
    public FlightdetailsSetupComponent createFlightdetailsService(){
        return new FlightdetailsSetupComponent();
    }

    //@Bean @Qualifier("employee")
    //public AbstractUserSetup createEmployeeSetup(){
    //    return new EmployeeAccountSetup();
    //}

    //@Bean @Qualifier("customer")
    //public AbstractUserSetup createCustomerSetup(){
    //    return new CustomerAccountSetup();
    //}
}
