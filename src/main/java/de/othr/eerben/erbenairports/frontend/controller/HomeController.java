package de.othr.eerben.erbenairports.frontend.controller;

import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.exceptions.UIErrorMessage;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Optional;

@Controller
@Scope("singleton")
public class HomeController {

    @Autowired
    private UserServiceIF userService;

    @RequestMapping("/")
    public String start(){
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET) // /login
    public String login(Model model, @RequestParam("error") Optional<Boolean> error) {
        System.out.println(error);
        if(error.isPresent() && error.get()){
            model.addAttribute("error",true);
        }else{
            model.addAttribute("error",false);
        }
        model.addAttribute("user", new User());
        return "authentication/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST) // th:action="@{/login}"
    public String doLogin() {
        return "index";
    }

    @RequestMapping("/errors")
    public String errorOccured(){return "error";}

    @RequestMapping(value = "/register", method = RequestMethod.GET) // /login
    public String register(Model model) {
        model.addAttribute("user", new User());

        return "authentication/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST) // /login
    public String doRegister( @Valid @ModelAttribute("user") User user,
                              BindingResult result,
                              @ModelAttribute("passwordRepeat") String confirmationPasswort,
                              @ModelAttribute("username") String username,
                              HttpServletRequest servlet,
                              Model model) throws ApplicationException {
        try {
            System.out.println("POST /register");
            System.out.println(user);
            user.setID(username);
            if (!confirmationPasswort.equals(user.getPassword())) {
                result.addError(new ObjectError("globalError", "The two given passwords are not the same!"));
            }
            if (result.hasErrors()) {
                return "authentication/register";
            }
            // save User
            user.setAccountType(AccountType.CUSTOMER);
            userService.registerUser(user);

            //Auto-Login nach Registrierung
            servlet.login(user.getUsername(), confirmationPasswort);

            return "redirect:/";
        }catch (ApplicationException exception) {
            model.addAttribute("uiErrorMessage", exception);
            return "authentication/register";
        }catch (ServletException e) {
            model.addAttribute("uiErrorMessage", new UIErrorMessage("Fehler bei der Anmeldung des neuen Benutzers.", e.getMessage()));
            return "redirect:/authentication/login";
        }

    }
}
