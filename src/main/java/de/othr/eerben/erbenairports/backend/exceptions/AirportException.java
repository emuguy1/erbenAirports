package de.othr.eerben.erbenairports.backend.exceptions;
/**This is a class for handing down error informations to an UI component,
 that can be specified in the Controllers in order for the user to help handling it*/
public class AirportException extends Exception{
    public String errortitel;
    public String errormessage;
    public String suggestions;

    public AirportException(String errormessage){
        super(errormessage);
        this.errormessage=errormessage;
    }

    public AirportException(String errormessage, String suggestions){
        super(errormessage);
        this.errormessage=errormessage;
        this.suggestions=suggestions;
    }

    public AirportException(String errortitel, String errormessage, String suggestions){
        super(errormessage);
        this.errortitel=errortitel;
        this.errormessage=errormessage;
        this.suggestions=suggestions;
    }

    public void setErrortitel(String errortitel) {
        this.errortitel = errortitel;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public String getErrortitel(){ return  errortitel; }

    @Override
    public String toString() {
        return "UIErrorMessage{" +
                "errortitel='" + errortitel + '\'' +
                ", errormessage='" + errormessage + '\'' +
                ", suggestions='" + suggestions + '\'' +
                '}';
    }
}
