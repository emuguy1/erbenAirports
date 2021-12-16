package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.entities.BookedCalendarslot;
import de.othr.eerben.erbenairports.backend.data.entities.Flightdetails;
import de.othr.eerben.erbenairports.backend.data.repositories.BookedCalendarslotRepository;
import de.othr.eerben.erbenairports.backend.data.repositories.FlightdetailsRepository;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.TimeZone;

public class FlightdetailsSetupComponent extends AbstractSetupComponent{

    @Autowired
    FlightdetailsRepository flightdetailsRepo;
    @Autowired
    BookedCalendarslotRepository calendarslotRepo;

    @Override
    boolean setup() throws ApplicationException {
        try{
            if(flightdetailsRepo.findByFlightnumber("LH320") != null){
                return true;
            }
            LocalDateTime now= LocalDateTime.now();
            LocalDateTime now12= LocalDateTime.now().plusHours(12);
            Airport muc = new Airport("MUC", TimeZone.getTimeZone("Germany/Berlin"),"Germany","Munich");
            Airport lax = new Airport("LAX", TimeZone.getTimeZone("USA/LosAngeles"),"USA","Los Angeles");
            BookedCalendarslot calendarslotnowMUC= new BookedCalendarslot(now.getDayOfMonth(),now.getMonthValue(),now.getYear(), 15,now,muc);
            BookedCalendarslot bookedCalendarslot12MUC = new BookedCalendarslot(now12.getDayOfMonth(),now12.getMonthValue(),now12.getYear(), 15,now12, muc);
            BookedCalendarslot calendarslotnowLAX= new BookedCalendarslot(now.getDayOfMonth(),now.getMonthValue(),now.getYear(), 15,now,lax);
            BookedCalendarslot bookedCalendarslot12LAX = new BookedCalendarslot(now12.getDayOfMonth(),now12.getMonthValue(),now12.getYear(), 15,now12, lax);
            calendarslotRepo.save(calendarslotnowLAX);
            calendarslotRepo.save(bookedCalendarslot12LAX);
            calendarslotRepo.save(calendarslotnowMUC);
            calendarslotRepo.save(bookedCalendarslot12MUC);
            flightdetailsRepo.save(new Flightdetails("LH3200", 12.4,25.6,250, lax, muc,calendarslotnowLAX, bookedCalendarslot12MUC));
            flightdetailsRepo.save(new Flightdetails("LH3220", 12.4,25.6,250, muc, lax,calendarslotnowMUC, bookedCalendarslot12LAX));
            return true;
        }catch(Exception e){
            throw new ApplicationException("Flightdetails Setup failed. Couldnt create Airports");
        }
    }
}
