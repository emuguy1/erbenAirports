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
import java.time.*;
import java.util.*;

@Service
public class FlightdetailsService implements FlightdetailsServiceIF {

    @Autowired
    private AirportServiceIF airportServiceIF;

    @Autowired
    private UserServiceIF userServiceIF;

    @Autowired
    private FlightdetailsRepository flightdetailsRepo;

    @Autowired
    private BookedCalendarslotRepository calendarslotRepository;

    @Autowired
    RestTemplate restClient;

    @Value("${trBank.url}")
    private String bankingURL;

    @Value("${trBank.iban}")
    private String bankingSelfIBAN;

    @Value("${trBank.username}")
    private String bankingUsername;

    @Value("${trBank.password}")
    private String bankingPassword;

    Logger logger= LoggerFactory.getLogger(FlightdetailsServiceIF.class);


    @Override
    public Page<Flightdetails> getDeparturesPaginated(String airportcode, Pageable pageable) throws AirportException {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        //TODO: get departures after a specific time
        Airport airport = airportServiceIF.getAirportByAirportcode(airportcode);
        List<Flightdetails> flights = flightdetailsRepo.getAllByDepartureAirportAndDepartureTimeIsAfterOrderByDepartureTime(airport, Timestamp.from(Instant.now()));
        System.out.println(flights);
        List<Flightdetails> list;

        if (flights.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, flights.size());
            list = flights.subList(startItem, toIndex);
        }

        Page<Flightdetails> flightsPage
                = new PageImpl<Flightdetails>(list, PageRequest.of(currentPage, pageSize), flights.size());

        return flightsPage;
    }

    @Override
    public Page<Flightdetails> getArrivalsPaginated(String airportcode, Pageable pageable) throws AirportException {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        //TODO: get arrivals after a specific time
        Airport airport = airportServiceIF.getAirportByAirportcode(airportcode);
        List<Flightdetails> flights = flightdetailsRepo.getAllByArrivalAirportAndArrivalTimeIsAfterOrderByArrivalTime(airport, Timestamp.from(Instant.now())).orElseThrow(() -> new AirportException("Error, no Arrivals for airport after now could be found"));
        System.out.println(flights);
        List<Flightdetails> list;

        if (flights.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, flights.size());
            list = flights.subList(startItem, toIndex);
        }

        Page<Flightdetails> flightsPage
                = new PageImpl<Flightdetails>(list, PageRequest.of(currentPage, pageSize), flights.size());

        return flightsPage;
    }

    @Override
    public Collection<Flightdetails> getFlightdetails(String flightnumber) {
        return flightdetailsRepo.findByFlightnumber(flightnumber).orElseThrow();
    }

    @Override
    public Optional<Flightdetails> getFlightdetailsById(long flightid) {
        return flightdetailsRepo.findByFlightid(flightid);
    }

    @Transactional
    @Override
    public boolean cancelFlight(User user, FlighttransactionDTO flight) throws AirportException {
        //TODO:Repo Input has to be Date and not LocalDateTime
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
                if(flightdetails.getCustomer() != null){
                    TransaktionDTO bankingTransaction = new TransaktionDTO(bankingSelfIBAN, user.getIban(),
                            new BigDecimal("4000.00"), "Cancellation of: Usage of airport for " + flightdetails.getFlightnumber()
                            + " on Airports: " + flightdetails.getDepartureAirport() + " and " + flightdetails.getArrivalAirport()
                            + " departing at :" + flightdetails.getDepartureTime().getStartTime());//Source, target, amount, purpose
                    RestDTO bankingDTO = new RestDTO(bankingUsername, bankingPassword, bankingTransaction);
                    TransaktionDTO response;

                    //UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(bankingURL).queryParam("value", 100.00);
                    try {
                        //for null Object insert filled Object of TRBank
                        response = restClient.postForObject(bankingURL, bankingDTO, TransaktionDTO.class);//String.class has to be class of TRBank response
                        System.out.println(response);
                    } catch (Exception e) {
                        logger.error("Could not perform transaction!");
                        throw new AirportException("Transaction could not be performed!");
                    }
                    if (response == null) {
                        logger.error("Could not perform transaction!");
                        throw new AirportException("Transaction could not be performed!");
                    }
                }
                //TODO:Book back to Customer via TRBank
                deleteById(flightdetails.getFlightid());
            } else {
                logger.warn("Flight is currently in the Air! Cannot be canncled!");
                throw new AirportException("Flight is currently in the Air! Cannot be canncled!");
            }
            return true;
        } catch (AirportException e) {
            logger.error("Flight could'nt be canncled! "+flight);
            e.setErrortitle("Flight could'nt be canncled! Please check the credentials!");
            throw e;
        }
    }

    @Transactional
    @Override
    public Flightdetails updateFlight(Flightdetails flightdetails) throws AirportException {
        try{
            Flightdetails oldFlightdetailsOptional = flightdetailsRepo.findById(flightdetails.getFlightid()).orElseThrow(() -> new AirportException("Flight not found " + flightdetails.getFlightid()));
            return flightdetailsRepo.save(flightdetails);
        }catch(AirportException e){
            logger.info("Flight not found and not updated " + flightdetails.getFlightid());
            return null;
        }catch(Exception b){
            logger.error("Flight could not be updated! Errormessage: "+b.getMessage());
            throw new AirportException("Flight could not be updated!",b.getMessage());
        }
    }


    @Transactional
    @Override
    public Flightdetails bookFlight(User user, FlighttransactionDTO flightdetails) throws AirportException {
        //In Case of exception we have to check if Transaction with bank was performed. Therefore we have an boolean flag
        boolean moneyTransfered = false;
        try {

            User createdForCustomer=null;
            //Check if this user exists this early, so that Exception is thrown early
            if(flightdetails.getUserenameCreatedFor()!=null){
                createdForCustomer = userServiceIF.getUserByUsername(flightdetails.getUserenameCreatedFor());
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
            if(wishedDeparture==approximatedArrivalTime&&flightdetails.getDepartureAirport().equals(flightdetails.getArrivalAirport())){
                calendar1.add(Calendar.MINUTE, 5);
                approximatedArrivalTime=calendar1.getTime();
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
            //create bookedTimeslot
            BookedCalendarslot calendarslotDeparture = new BookedCalendarslot(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), 5, calendar.getTime(), airportServiceIF.getAirportByAirportcode(flightdetails.getDepartureAirport()));
            BookedCalendarslot calendarslotArrival = new BookedCalendarslot(calendar1.get(Calendar.DAY_OF_MONTH), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.YEAR), 5, calendar1.getTime(), airportServiceIF.getAirportByAirportcode(flightdetails.getArrivalAirport()));


            //only transfer money if it was done by a customer or a employee created it for an customer
            if (user.getAccountType().equals(AccountType.CUSTOMER)||createdForCustomer!=null) {
                TransaktionDTO bankingTransaction = new TransaktionDTO(user.getIban(), bankingSelfIBAN,
                        new BigDecimal("4000.00"), "Usage of airport for " + flightdetails.getFlightnumber()
                        + " on Airports: " + flightdetails.getDepartureAirport() + " and " + flightdetails.getArrivalAirport()
                        + " starting at :" + calendarslotDeparture.getStartTime());
                if(createdForCustomer!=null){
                    //In case that it was booked by an employee for an customer
                    bankingTransaction.setQuellIban(createdForCustomer.getIban());
                }
                RestDTO bankingDTO = new RestDTO(bankingUsername, bankingPassword, bankingTransaction);
                TransaktionDTO response;
                try {
                    response = restClient.postForObject(bankingURL, bankingDTO, TransaktionDTO.class);
                    moneyTransfered=true;
                    logger.info("Bankingresponse: "+response);
                } catch (Exception e) {
                    logger.error("Bankingtransaction could not be performed!");
                    throw new AirportException("Bankingtransaction could not be performed!");
                }
                System.out.println(response);
                if (response == null) {
                    logger.error("Bankingtransaction could not be performed!");
                    throw new AirportException("Bankingtransaction could not be performed!");
                }
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
                if(createdForCustomer!=null){
                    flightdetails1.setCustomer(createdForCustomer);
                }
            }

            //save flightdetails
            flightdetails1 = flightdetailsRepo.save(flightdetails1);
            return flightdetails1;
        } catch (AirportException e) {
            if(moneyTransfered){
                //TODO:wire the money back to customer
                moneyTransfered=false;
            }
            throw new AirportException(e.getMessage());
        }
    }

    @Override
    public void deleteByAirportId(String airport) throws AirportException {
        if (!flightdetailsRepo.getAllByAirportWhereArrivalTimeAfterAndDepartureTimeBefore(Date.from(Instant.now()), airport)) {
            throw new AirportException("At least one Flight exists with Arrivaltime after now");
        } else {
            List<Flightdetails> toBeCanceledFlights = flightdetailsRepo.getAllByAirport(airport).orElseThrow(() -> new AirportException("Error, no Departures for airport after now could be found"));
            flightdetailsRepo.deleteAll(toBeCanceledFlights);
        }
    }

    @Override
    @Transactional
    public void deleteById(long flightid) throws AirportException {
        Flightdetails flight = flightdetailsRepo.findByFlightid(flightid).orElseThrow(() -> new AirportException("Error, no Departures for airport after now could be found"));
        if (flight.getArrivalTime().getStartTime().after(Date.from(Instant.now()))&&flight.getDepartureTime().getStartTime().before(Date.from(Instant.now()))) {
            throw new AirportException("Flight is in the air!");
        } else {
            flightdetailsRepo.deleteById(flightid);
        }

    }


}
