package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.TimeZone;

@Entity
public class  Airport {

    @Id
    @NotBlank(message="Housenumber cannot be empty!")
    private String airportcode;

    @NotBlank(message="Housenumber cannot be empty!")
    private String airportname;

    @NotBlank(message="Housenumber cannot be empty!")
    private String timeZone;

    @NotBlank(message="Housenumber cannot be empty!")
    private String country;

    @NotBlank(message="Housenumber cannot be empty!")
    private String city;


    public Airport(){}

    public Airport(String airportcode, String timeZone, String country, String city, String airportname) {
        this.airportcode = airportcode;
        this.timeZone = timeZone;
        this.country = country;
        this.city = city;
        this.airportname = airportname;
    }

    public String getAirportcode() {
        return airportcode;
    }

    public void setAirportcode(String airportcode) {
        this.airportcode = airportcode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirportname() {
        return airportname;
    }

    public void setAirportname(String airportname) {
        this.airportname = airportname;
    }

}
