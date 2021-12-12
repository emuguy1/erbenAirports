package de.othr.eerben.erbenairports.backend.exceptions;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;

public class FlightdetailsException extends RuntimeException{
    public FlightdetailsException(Flightdetails flightdetails, String errorMessage){
        super(errorMessage);
    }
}
