package de.othr.eerben.erbenairports.util;

import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Helper {

    public static List<Integer> pageNumbers(int currentPage, int totalPages) {
        List<Integer> pageNumbers;
        if (currentPage <= 3 && totalPages >6) {
            pageNumbers = IntStream.rangeClosed(1, 6)
                    .boxed()
                    .collect(Collectors.toList());
            pageNumbers.add(totalPages);
        }
        else if( totalPages <= 6){
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }
        else {

            pageNumbers = IntStream.rangeClosed(1, 1)
                    .boxed()
                    .collect(Collectors.toList());
            if (currentPage >= totalPages - 2) {
                for (int i = totalPages - 6; i <= totalPages; i++) {
                    pageNumbers.add(i);
                }
            } else {
                for (int i = currentPage - 2; (i <= currentPage + 3) && i<totalPages; i++) {
                    pageNumbers.add(i);
                }
                pageNumbers.add(totalPages);
            }
        }
        return pageNumbers;
    }

    public static FlighttransactionDTO getFlighttransactionDTO(Flightdetails flightdetails) {
        return new FlighttransactionDTO("", "", flightdetails.getFlightnumber(), flightdetails.getFlightTimeHours(), flightdetails.getMaxCargo(), flightdetails.getPassengerCount(), flightdetails.getDepartureAirport().getAirportcode(), flightdetails.getArrivalAirport().getAirportcode(), LocalDateTime.ofInstant(flightdetails.getDepartureTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getDepartureAirport().getTimeZone())), LocalDateTime.ofInstant(flightdetails.getArrivalTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getArrivalAirport().getTimeZone())));
    }
}
