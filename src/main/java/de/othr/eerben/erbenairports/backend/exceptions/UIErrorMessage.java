package de.othr.eerben.erbenairports.backend.exceptions;
/**This is a class for handing down error informations to an UI component,
 that can be specified in the Controllers in order for the user to help handling it*/
public class UIErrorMessage {
    public String errormessage;
    public String suggestions;

    public UIErrorMessage(String errormessage){
        this.errormessage=errormessage;
    }

    public UIErrorMessage(String errormessage, String suggestions){
        this.errormessage=errormessage;
        this.suggestions=suggestions;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public String getSuggestions() {
        return suggestions;
    }
}
