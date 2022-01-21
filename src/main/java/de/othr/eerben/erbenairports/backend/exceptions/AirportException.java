package de.othr.eerben.erbenairports.backend.exceptions;

/**
 * Exception class with specifications for UI.
 * With the help of suggestions or message and title, the user can get a better understanding of the Problem
 * and maybe fix it.
 */
public class AirportException extends Exception {
    public String errortitle;
    public String errormessage;
    public String suggestions;

    public AirportException(String errormessage) {
        super(errormessage);
        this.errormessage = errormessage;
    }

    public AirportException(String errormessage, String suggestions) {
        super(errormessage);
        this.errormessage = errormessage;
        this.suggestions = suggestions;
    }

    public AirportException(String errortitle, String errormessage, String suggestions) {
        super(errormessage);
        this.errortitle = errortitle;
        this.errormessage = errormessage;
        this.suggestions = suggestions;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getErrortitle() {
        return errortitle;
    }

    public void setErrortitle(String errortitle) {
        this.errortitle = errortitle;
    }

    @Override
    public String toString() {
        return "UIErrorMessage{" +
                "errortitle='" + errortitle + '\'' +
                ", errormessage='" + errormessage + '\'' +
                ", suggestions='" + suggestions + '\'' +
                '}';
    }
}
