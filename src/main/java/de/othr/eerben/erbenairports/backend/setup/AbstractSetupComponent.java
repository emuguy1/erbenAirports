package de.othr.eerben.erbenairports.backend.setup;

import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSetupComponent {

    Logger logger = LoggerFactory.getLogger(AbstractSetupComponent.class);

    abstract boolean setup() throws AirportException;
}
