package de.othr.eerben.erbenairports.backend.data.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;
import java.util.TimeZone;

@Entity
public class Airport {

    @Id
    @NotNull
    private String airportcode;

    @Column(nullable = true)
    private String timeZone;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false,unique = true)
    private String city;


    public Airport(){}

    public Airport(String airportcode, String timeZone, String country, String city) {
        this.airportcode = airportcode;
        this.timeZone = timeZone;
        this.country = country;
        this.city = city;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return airportcode.equals(airport.airportcode) && timeZone.equals(airport.timeZone) && country.equals(airport.country) && city.equals(airport.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportcode, timeZone, country, city);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "airportcode='" + airportcode + '\'' +
                ", timeZone=" + timeZone +
                ", country='" + country + '\'' +
                ", city='" + city +
                '}';
    }
}
