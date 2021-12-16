package de.othr.eerben.erbenairports.backend.services.setup;

import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;

public abstract class AbstractSetupComponent {

    abstract boolean setup() throws ApplicationException;
}
