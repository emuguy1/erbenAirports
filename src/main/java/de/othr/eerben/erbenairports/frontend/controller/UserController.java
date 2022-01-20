package de.othr.eerben.erbenairports.frontend.controller;

import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Scope("singleton")
public class UserController {

    @Autowired
    UserServiceIF userServiceIF;

    @RequestMapping(value = "/user/{id}/details", method = RequestMethod.GET)
    public String showEmployeeUserDetails(Model model, @PathVariable("id")String username) {
        try{
            model.addAttribute("user", userServiceIF.getUserByUsername(username));
        }
        //TODO: Error reformati
        catch(Exception e){
            model.addAttribute("UIerror", new AirportException(e.getMessage()));
            return "index";
        }
        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showUserDetails(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "profile";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET) // /login
    public String registerCustomer(Model model) {
        model.addAttribute("user", new User());
        return "authentication/register";
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET) // /login
    public String getCustomers(Model model) {
        model.addAttribute("customers", userServiceIF.getAllCustomers());
        return "employee/customerTable";
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
            userServiceIF.registerUser(user);

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
            userServiceIF.registerUser(user);

            return "redirect:/user/{"+user.getUsername()+"}/details";
        }catch (AirportException exception) {
            model.addAttribute("error", exception);
            return "employee/registerEmployee";
        }

    }

}
