package de.othr.eerben.erbenairports.backend.data.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ConnectionDTO {
    @NotNull
    private String departureAirportcode;
    @NotNull
    private String arrivalAirportcode;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime connectionTime;

    public ConnectionDTO() {
    }

    public ConnectionDTO(String departureAirportcode, String arrivalAirportcode, LocalDateTime connectionTime) {
        this.departureAirportcode = departureAirportcode;
        this.arrivalAirportcode = arrivalAirportcode;
        this.connectionTime = connectionTime;
    }

    public String getDepartureAirportcode() {
        return departureAirportcode;
    }

    public void setDepartureAirportcode(String departureAirportcode) {
        this.departureAirportcode = departureAirportcode;
    }

    public String getArrivalAirportcode() {
        return arrivalAirportcode;
    }

    public void setArrivalAirportcode(String arrivalAirportcode) {
        this.arrivalAirportcode = arrivalAirportcode;
    }

    public LocalDateTime getConnectionTime() {
        return connectionTime;
    }

    public Instant getConnectionTimeInstant() {
        return connectionTime.toInstant(ZoneOffset.UTC);
    }

    public void setConnectionTime(LocalDateTime connectionTime) {
        this.connectionTime = connectionTime;
    }
}
