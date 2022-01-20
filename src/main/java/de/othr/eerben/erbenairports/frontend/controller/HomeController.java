package de.othr.eerben.erbenairports.frontend.controller;

import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
        if(error.isPresent() && error.get()){
            //TODO: Logger mit Error
            model.addAttribute("UIerror", new AirportException("Password or Username were wrong!"));
        }
        model.addAttribute("user", new User());
        return "authentication/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST) // th:action="@{/login}"
    public String doLogin() {
        return "index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET) // /login
    public String registerCustomer(Model model,HttpServletRequest servlet) {
        model.addAttribute("user", new User());

        return "authentication/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST) // /login
    public String doRegisterCustomer( @Valid @ModelAttribute("user") User user,
                              BindingResult result,
                              @ModelAttribute("passwordRepeat") String confirmationPasswort,
                              @ModelAttribute("username") String username,
                              HttpServletRequest servlet,
                              Model model) {
        try {
            System.out.println("POST /register");
            user.setID(username);
            if (!confirmationPasswort.equals(user.getPassword())) {
                result.addError(new ObjectError("globalError", "The two given passwords are not the same!"));
            }
            if (user.getIban()==null || user.getIban().isBlank()) {
                result.rejectValue("iban", "error.user", "IBAN field can´t be empty");
            }
            if (user.getCompany_name()==null || user.getCompany_name().isBlank()) {
                result.rejectValue("company_name", "error.user", "Company Name can´t be empty");
            }
            if (result.hasErrors()) {
                return "authentication/register";
            }
            // save User
            user.setAccountType(AccountType.CUSTOMER);
            userService.registerUser(user);

            //Auto-Login after Registration
            servlet.login(user.getUsername(), confirmationPasswort);

            return "redirect:/";
        }catch (AirportException exception) {
            model.addAttribute("error", exception);
            return "authentication/register";
        }catch (ServletException e) {
            model.addAttribute("error", new AirportException("Fehler bei der Anmeldung des neuen Benutzers.", e.getMessage()));
            return "redirect:/authentication/login";
        }

    }

    @RequestMapping(value = "/employee/register", method = RequestMethod.GET) // /login
    public String registerEmployee(Model model) {
        model.addAttribute("user", new User());

        return "employee/registerEmployee";
    }

    @RequestMapping(value = "/employee/register", method = RequestMethod.POST) // /login
    public String doRegisterEmployee( @Valid @ModelAttribute("user") User user,
                                      BindingResult result,
                                      @ModelAttribute("passwordRepeat") String confirmationPasswort,
                                      @ModelAttribute("username") String username,
                                      HttpServletRequest servlet,
                                      Model model) {
        try {
            System.out.println("POST /register");
            user.setID(username);
            if (!confirmationPasswort.equals(user.getPassword())) {
                result.addError(new ObjectError("globalError", "The two given passwords are not the same!"));
            }
            if (result.hasErrors()) {
                return "employee/registerEmployee";
            }
            // save User
            user.setAccountType(AccountType.EMPLOYEE);
            userService.registerUser(user);

            return "redirect:/user/{"+user.getUsername()+"}/details";
        }catch (AirportException exception) {
            model.addAttribute("error", exception);
            return "employee/registerEmployee";
        }

    }
}
