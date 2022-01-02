package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.BookedCalendarslot;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.repositories.BookedCalendarslotRepository;
import de.othr.eerben.erbenairports.backend.data.repositories.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FlightdetailsSetupComponent extends AbstractSetupComponent{

    @Autowired
    FlightdetailsRepository flightdetailsRepo;
    @Autowired
    BookedCalendarslotRepository calendarslotRepo;

    @Override
    boolean setup() throws ApplicationException {
        try{

            if(/*flightdetailsRepo.findByFlightnumber("LH3200").isPresent()*/false){
                return true;
            }
            Date now= Date.from(Instant.now().plusSeconds(7200));
            Date now12= Date.from(Instant.now().minusSeconds(720));
            Airport muc = new Airport("MUC", TimeZone.getTimeZone("Germany/Berlin"),"Germany","Munich");
            Airport lax = new Airport("LAX", TimeZone.getTimeZone("USA/LosAngeles"),"USA","Los Angeles");
            BookedCalendarslot calendarslotnowMUC= new BookedCalendarslot(now.getDay(),now.getMonth(),now.getYear(), 15,now,muc);
            BookedCalendarslot bookedCalendarslot12MUC = new BookedCalendarslot(now12.getDay(),now12.getMonth(),now12.getYear(), 15,now12, muc);
            BookedCalendarslot calendarslotnowLAX= new BookedCalendarslot(now.getDay(),now.getMonth(),now.getYear(), 15,now,lax);
            BookedCalendarslot bookedCalendarslot12LAX = new BookedCalendarslot(now12.getDay(),now12.getMonth(),now12.getYear(), 15,now12, lax);
            calendarslotRepo.save(calendarslotnowLAX);
            calendarslotRepo.save(bookedCalendarslot12LAX);
            calendarslotRepo.save(calendarslotnowMUC);
            calendarslotRepo.save(bookedCalendarslot12MUC);
            flightdetailsRepo.save(new Flightdetails("LH3200", 12.4,25.6,250, lax, muc, bookedCalendarslot12MUC,calendarslotnowLAX));
            flightdetailsRepo.save(new Flightdetails("LH3220", 12.4,25.6,250, muc, lax, bookedCalendarslot12LAX,calendarslotnowMUC));
            return true;
        }catch(Exception e){
            throw new ApplicationException("Flightdetails Setup failed. Couldnt create Airports");
        }
    }
}
