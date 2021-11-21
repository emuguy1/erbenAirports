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

    @Column(nullable = false)
    private TimeZone timeZone;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false,unique = true)
    private String city;

    @Column(nullable = false)
    private double longitudinal;

    @Column(nullable = false)
    private double latitudinal;


    public Airport(){}

    public Airport(String airportcode, TimeZone timeZone, String country, String city, double longitudinal, double latitudinal) {
        this.airportcode = airportcode;
        this.timeZone = timeZone;
        this.country = country;
        this.city = city;
        this.longitudinal = longitudinal;
        this.latitudinal = latitudinal;
    }

    public String getAirportcode() {
        return airportcode;
    }

    public void setAirportcode(String airportcode) {
        this.airportcode = airportcode;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
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

    public double getLongitudinal() {
        return longitudinal;
    }

    public void setLongitudinal(double longitudinal) {
        this.longitudinal = longitudinal;
    }

    public double getLatitudinal() {
        return latitudinal;
    }

    public void setLatitudinal(double latitudinal) {
        this.latitudinal = latitudinal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Double.compare(airport.longitudinal, longitudinal) == 0 && Double.compare(airport.latitudinal, latitudinal) == 0 && airportcode.equals(airport.airportcode) && timeZone.equals(airport.timeZone) && country.equals(airport.country) && city.equals(airport.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportcode, timeZone, country, city, longitudinal, latitudinal);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "airportcode='" + airportcode + '\'' +
                ", timeZone=" + timeZone +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", longitudinal=" + longitudinal +
                ", latitudinal=" + latitudinal +
                '}';
    }
}
