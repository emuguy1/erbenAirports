package de.othr.eerben.erbenairports.backend.services.impl;

import de.othr.eerben.erbenairports.backend.data.entities.*;
import de.othr.eerben.erbenairports.backend.data.entities.dto.FlighttransactionDTO;
import de.othr.eerben.erbenairports.backend.data.repositories.BookedCalendarslotRepository;
import de.othr.eerben.erbenairports.backend.data.repositories.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.FlightdetailsServiceIF;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import de.othr.sw.TRBank.entity.dto.RestDTO;
import de.othr.sw.TRBank.entity.dto.TransaktionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class FlightdetailsService implements FlightdetailsServiceIF {

    @Autowired
    RestTemplate restClient;
    Logger logger = LoggerFactory.getLogger(FlightdetailsServiceIF.class);
    @Autowired
    private AirportServiceIF airportServiceIF;
    @Autowired
    private UserServiceIF userServiceIF;
    @Autowired
    private FlightdetailsRepository flightdetailsRepo;
    @Autowired
    private BookedCalendarslotRepository calendarslotRepository;
    @Value("${trBank.url}")
    private String bankingURL;
    @Value("${trBank.iban}")
    private String bankingSelfIBAN;
    @Value("${trBank.username}")
    private String bankingUsername;
    @Value("${trBank.password}")
    private String bankingPassword;

    @Override
    public Page<Flightdetails> getDeparturesPaginated(String airportcode, Pageable pageable) throws AirportException {
        //TODO: get departures after a specific time
        Airport airport = airportServiceIF.getAirportByAirportcode(airportcode);
        return flightdetailsRepo.getAllByDepartureAirportAndDepartureTimeIsAfterOrderByDepartureTime(airport, Timestamp.from(Instant.now()), pageable);
    }

    @Override
    public Page<Flightdetails> getArrivalsPaginated(String airportcode, Pageable pageable) throws AirportException {
        //TODO: get arrivals after a specific time
        Airport airport = airportServiceIF.getAirportByAirportcode(airportcode);
        return flightdetailsRepo.getAllByArrivalAirportAndArrivalTimeIsAfterOrderByArrivalTime(airport, Timestamp.from(Instant.now()),pageable);
    }

    @Override
    public Flightdetails getFlightdetailsById(long flightid) throws AirportException {
        return flightdetailsRepo.findByFlightid(flightid).orElseThrow(() -> new AirportException("Flight could not be found!"));
    }

    @Transactional
    @Override
    public boolean cancelFlight(User user, FlighttransactionDTO flight) throws AirportException {
        try {
            Flightdetails flightdetails;
            if (user.getAccountType() == AccountType.CUSTOMER) {
                flightdetails = flightdetailsRepo.
                        getFlightdetailsByDepartureTimeAndDepartureAirportAndArrivalAirportAndFlightnumberAndAndArrivalTimeAndCustomer(
                                Date.from(flight.getDepartureTime().toInstant(ZoneId.of("Europe/Berlin").getRules().getOffset(LocalDateTime.now()))),
                                flight.getDepartureAirport(), flight.getArrivalAirport(), flight.getFlightnumber(),
                                Date.from(flight.getArrivalTime().toInstant(ZoneId.of("Europe/Berlin").getRules().getOffset(LocalDateTime.now()))), user
                        ).orElseThrow(() -> new AirportException("Flight could not be found!"));

            } else {
                flightdetails = flightdetailsRepo.
                        getFlightdetailsByDepartureTimeAndDepartureAirportAndArrivalAirportAndFlightnumberAndAndArrivalTime(
                                Date.from(flight.getDepartureTime().toInstant(ZoneId.of("Europe/Berlin").getRules().getOffset(LocalDateTime.now()))),
                                flight.getDepartureAirport(), flight.getArrivalAirport(), flight.getFlightnumber(),
                                Date.from(flight.getArrivalTime().toInstant(ZoneId.of("Europe/Berlin").getRules().getOffset(LocalDateTime.now())))
                        ).orElseThrow(() -> new AirportException("Flight could not be found!"));
            }

            if (flight.getDepartureTime().isAfter(LocalDateTime.now()) || flight.getArrivalTime().isBefore(LocalDateTime.now())) {
                if (flightdetails.getCustomer() != null) {
                    //TODO:I dont know
                    FlighttransactionDTO flightDTO = getFlighttransactionDTO(flightdetails);
                    performBankingTransaction(user, true, flightDTO);
                }
                deleteById(flightdetails.getFlightid());
            } else {
                logger.warn("Flight is currently in the Air! Cannot be canncled!");
                throw new AirportException("Flight is currently in the Air! Cannot be canncled!");
            }
            return true;
        } catch (AirportException e) {
            logger.error("Flight could'nt be canncled! " + flight);
            e.setErrortitle("Flight could'nt be canncled! Please check the credentials!");
            throw e;
        }
    }

    @Transactional
    @Override
    public Flightdetails bookFlight(User user, FlighttransactionDTO flightdetails) throws AirportException {
        //In Case of exception we have to check if Transaction with bank was performed. Therefore we have an boolean flag
        boolean moneyTransfered = false;
        User bankingCustomer = null;
        try {
            //Check if this user exists this early, so that Exception is thrown early
            if (flightdetails.getUserenameCreatedFor() != null) {
                bankingCustomer = userServiceIF.getUserByUsername(flightdetails.getUserenameCreatedFor());
            } else if (user.getAccountType() == AccountType.CUSTOMER) {
                bankingCustomer = user;
            }

            //check necessary inputs
            Airport departureAirport = airportServiceIF.getAirportByAirportcode(flightdetails.getDepartureAirport());
            Airport arrivalAirport = airportServiceIF.getAirportByAirportcode(flightdetails.getArrivalAirport());

            //get departure time
            Instant departureInstant = flightdetails.getDepartureTime().atZone(ZoneId.of(departureAirport.getTimeZone())).toInstant();

            //Time is safed in local timezone time on Database and then displayed in Airport localtimezone
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
            calendar.setTime(Date.from(departureInstant));
            calendar.set(Calendar.SECOND, 0);


            //round to full 5 min upwards
            int cleanedMinutes = (int) Math.floor((calendar.get(Calendar.MINUTE) + 2.5) / 5) * 5;
            calendar.set(Calendar.MINUTE, cleanedMinutes);
            Date wishedDeparture = calendar.getTime();

            //get approximated Arrivaltime rounded up to full 5min slot
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(wishedDeparture);
            calendar1.add(Calendar.MINUTE, (int) Math.ceil(flightdetails.getFlightTimeHours() * 60));
            calendar1.set(Calendar.MINUTE, (int) Math.floor((calendar1.get(Calendar.MINUTE) + 2.5) / 5) * 5);
            Date approximatedArrivalTime = calendar1.getTime();

            //check for available timeslot at departureAirport and arrivalAirport and reshedule if necessary max to 60 min after/before wished

            //check if the time is not the same and the airports are not the same. Else, the slot would be booked twice which leads to errors
            if (wishedDeparture == approximatedArrivalTime && flightdetails.getDepartureAirport().equals(flightdetails.getArrivalAirport())) {
                calendar1.add(Calendar.MINUTE, 5);
                approximatedArrivalTime = calendar1.getTime();
            }

            boolean departurefree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getDepartureAirport(), wishedDeparture).isEmpty();
            boolean arrivalfree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getArrivalAirport(), approximatedArrivalTime).isEmpty();

            int i = 0;
            while ((!departurefree || !arrivalfree) && i < 12) {
                i++;
                calendar.add(Calendar.MINUTE, 5);
                wishedDeparture = calendar.getTime();
                calendar1.add(Calendar.MINUTE, 5);
                approximatedArrivalTime = calendar.getTime();
                departurefree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getDepartureAirport(), wishedDeparture).isEmpty();
                arrivalfree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getArrivalAirport(), approximatedArrivalTime).isEmpty();
            }
            if (!departurefree || !arrivalfree) {
                i = 0;
                calendar.add(Calendar.MINUTE, -60);
                calendar1.add(Calendar.MINUTE, -60);
                while ((!departurefree || !arrivalfree) && i > -12) {
                    i--;
                    calendar.add(Calendar.MINUTE, -5);
                    wishedDeparture = calendar.getTime();
                    calendar1.add(Calendar.MINUTE, -5);
                    approximatedArrivalTime = calendar.getTime();
                    departurefree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getDepartureAirport(), wishedDeparture).isEmpty();
                    arrivalfree = calendarslotRepository.getBookedCalendarslotByAirportAndStartTime(flightdetails.getArrivalAirport(), approximatedArrivalTime).isEmpty();
                }
            }
            if (!departurefree || !arrivalfree) {
                throw new AirportException("Found no possible timeslot  in plus/minus 1 hour of wished flightslot");
            }
            flightdetails.setDepartureTime(LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.of(departureAirport.getTimeZone())));
            flightdetails.setArrivalTime(LocalDateTime.ofInstant(calendar1.toInstant(), ZoneId.of(arrivalAirport.getTimeZone())));
            //create bookedTimeslot
            BookedCalendarslot calendarslotDeparture = new BookedCalendarslot(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), 5, calendar.getTime(), airportServiceIF.getAirportByAirportcode(flightdetails.getDepartureAirport()));
            BookedCalendarslot calendarslotArrival = new BookedCalendarslot(calendar1.get(Calendar.DAY_OF_MONTH), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.YEAR), 5, calendar1.getTime(), airportServiceIF.getAirportByAirportcode(flightdetails.getArrivalAirport()));


            //only transfer money if it was done by a customer or a employee created it for an customer
            if (bankingCustomer != null) {
                performBankingTransaction(bankingCustomer, false, flightdetails);
                moneyTransfered = true;
            }

            //Save found Calendarslots
            calendarslotRepository.save(calendarslotDeparture);
            calendarslotRepository.save(calendarslotArrival);


            //create flightdetails and add additional informations
            Flightdetails flightdetails1 = new Flightdetails(flightdetails.getFlightnumber(), flightdetails.getFlightTimeHours(), flightdetails.getMaxCargo(), flightdetails.getPassengerCount(), departureAirport, arrivalAirport, calendarslotDeparture, calendarslotArrival);
            if (user.getAccountType() == AccountType.CUSTOMER) {
                flightdetails1.setCustomer(user);
            } else {
                flightdetails1.setCreatedBy(user);
                if (bankingCustomer != null) {
                    flightdetails1.setCustomer(bankingCustomer);
                }
            }
            //save flightdetails
            flightdetails1 = flightdetailsRepo.save(flightdetails1);
            return flightdetails1;
        } catch (Exception e) {
            if (moneyTransfered) {
                performBankingTransaction(bankingCustomer, true, flightdetails);
            }
            throw new AirportException(e.getMessage());
        }
    }

    @Override
    public void deleteByAirportId(String airport) throws AirportException {
        if (!flightdetailsRepo.getAllByAirportWhereArrivalTimeAfterAndDepartureTimeBefore(Date.from(Instant.now()), airport)) {
            throw new AirportException("At least one Flight exists with Arrivaltime after now");
        } else {
            List<Flightdetails> toBeCanceledFlights = flightdetailsRepo.getAllByAirport(airport);
            flightdetailsRepo.deleteAll(toBeCanceledFlights);
        }
    }

    @Override
    @Transactional
    public void deleteById(long flightid) throws AirportException {
        Flightdetails flight = flightdetailsRepo.findByFlightid(flightid).orElseThrow(() -> new AirportException("Error, no Departures for airport after now could be found"));
        if (flight.getArrivalTime().getStartTime().after(Date.from(Instant.now())) && flight.getDepartureTime().getStartTime().before(Date.from(Instant.now()))) {
            throw new AirportException("Flight is in the air!");
        } else {
            flightdetailsRepo.deleteById(flightid);
        }

    }

    @Override
    public FlighttransactionDTO getFlighttransactionDTO(Flightdetails flightdetails) {
        return new FlighttransactionDTO("", "", flightdetails.getFlightnumber(), flightdetails.getFlightTimeHours(), flightdetails.getMaxCargo(), flightdetails.getPassengerCount(), flightdetails.getDepartureAirport().getAirportcode(), flightdetails.getArrivalAirport().getAirportcode(), LocalDateTime.ofInstant(flightdetails.getDepartureTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getDepartureAirport().getTimeZone())), LocalDateTime.ofInstant(flightdetails.getArrivalTime().getStartTime().toInstant(), ZoneId.of(flightdetails.getArrivalAirport().getTimeZone())));
    }

    @Override
    public Page<Flightdetails> getAllFlights(Pageable pageable) {
        return flightdetailsRepo.findAll(pageable);
    }

    @Override
    public Page<Flightdetails> getAllByUsername(String username, Pageable pageable) {
        return flightdetailsRepo.getAllByUsername(username, pageable);
    }

    public void performBankingTransaction(User user, boolean bookBack, FlighttransactionDTO flightdetails) throws AirportException {

        TransaktionDTO bankingTransaction = new TransaktionDTO(user.getIban(), bankingSelfIBAN,
                new BigDecimal("4000.00"), "Usage of airport for " + flightdetails.getFlightnumber()
                + " on Airports: " + flightdetails.getDepartureAirport() + " and " + flightdetails.getArrivalAirport()
                + " starting at :" + flightdetails.getDepartureTime());
        if (bookBack) {
            //In this case we have to swap own with customer iban
            bankingTransaction.setZielIban(user.getIban());
            bankingTransaction.setQuellIban(bankingSelfIBAN);
            bankingTransaction.setVerwendungszweck("Book money back for:".concat(bankingTransaction.getVerwendungszweck()));
        } else {
            bankingTransaction.setVerwendungszweck("Book money for:".concat(bankingTransaction.getVerwendungszweck()));
        }
        RestDTO bankingDTO = new RestDTO(bankingUsername, bankingPassword, bankingTransaction);
        TransaktionDTO response;
        try {
            response = restClient.postForObject(bankingURL, bankingDTO, TransaktionDTO.class);
            logger.info("Bankingresponse: " + response);
        } catch (Exception e) {
            logger.error("Bankingtransaction could not be performed!");
            throw new AirportException("Bankingtransaction could not be performed!");
        }
        if (response == null) {
            logger.error("Bankingtransaction could not be performed!");
            throw new AirportException("Bankingtransaction could not be performed!");
        }
    }


}
