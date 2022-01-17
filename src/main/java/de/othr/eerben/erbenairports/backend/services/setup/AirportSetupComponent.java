package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.data.entities.Airport;
import de.othr.eerben.erbenairports.backend.data.repositories.AirportRepository;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import org.springframework.beans.factory.annotation.Autowired;


public class AirportSetupComponent extends AbstractSetupComponent {

    @Autowired
    AirportRepository airportRepo;

    @Override
    boolean setup() throws AirportException {
        try{
            if(airportRepo.existsAirportByAirportcode("MUC")){
                return true;
            }
            airportRepo.save(new Airport("MUC", "Europe/Berlin","Germany","Munich","Flughafen München „Franz Josef Strauß“"));
            airportRepo.save(new Airport("LAX", "America/Los_Angeles","USA","Los_Angeles","Los Angeles International Airport"));
            airportRepo.save(new Airport("BER", "Europe/Berlin","Germany","Berlin","Flughafen Berlin Brandenburg „Willy Brandt“"));
            airportRepo.save(new Airport("FRA", "Europe/Berlin","Germany","Frankfurt","Flughafen Frankfurt Main"));
            airportRepo.save(new Airport("DUB", "Europe/Dublin","Ireland","Dublin","Aerfort Bhaile Átha Cliath"));
            airportRepo.save(new Airport("ATL", "America/New_York","USA","Atlanta","Hartsfield–Jackson Atlanta International Airport"));
            airportRepo.save(new Airport("PEK", "Asia/Shanghai","China","Beijing","Beijing Capital International Airport"));
            airportRepo.save(new Airport("DXB", "Asia/Dubai","United Arab Emirates","Dubai","Dubai International Airport"));
            airportRepo.save(new Airport("HND", "Japan","Japan","Tokyo","Tokyo Haneda Airport"));
            airportRepo.save(new Airport("LHR", "Europe/London","United Kingdom","London","Heathrow Airport"));
            airportRepo.save(new Airport("CDG", "Europe/Paris","France","Paris","Charles de Gaulle Airport"));
            airportRepo.save(new Airport("AMS", "Europe/Amsterdam","Netherlands","Amsterdam","Amsterdam Airport Schiphol"));
            airportRepo.save(new Airport("HKG", "Hongkong","Hongkong","Hongkong","Hong Kong International Airport"));
            airportRepo.save(new Airport("ICN", "Asia/Seoul","South Korea","Seoul","Seoul Incheon International Airport"));
            airportRepo.save(new Airport("DEL", "IST","India","Delhi","Indira Gandhi International Airport"));
            airportRepo.save(new Airport("SIN", "Singapore","Singapore","Singapore","Singapore Changi Airport"));
            airportRepo.save(new Airport("JFK", "America/New_York","USA","New_York","John F. Kennedy International Airport"));
            airportRepo.save(new Airport("CPT", "Africa/Johannesburg","South Africa","Capetown","Cape Town International Airport"));
            airportRepo.save(new Airport("SJO", "America/Costa_Rica","Costa Rica","San José","Juan Santamaría International Airport"));
            airportRepo.save(new Airport("FCO", "Europe/Rome","Italy","Rome","Rome–Fiumicino International Airport \"Leonardo da Vinci\""));


            return true;
        }catch(Exception e){
            throw new AirportException("Airport Setup failed. Couldnt create Airports");
        }
    }

}
