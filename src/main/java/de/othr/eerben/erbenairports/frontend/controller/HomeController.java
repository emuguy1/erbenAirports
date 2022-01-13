package de.othr.eerben.erbenairports.frontend.controller;

import de.othr.eerben.erbenairports.backend.data.entities.AccountType;
import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.ApplicationException;
import de.othr.eerben.erbenairports.backend.services.AirportServiceIF;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Optional;

@Controller
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
    public String doRegister(Model model, @ModelAttribute("user") User user) throws ApplicationException {
        user.setAccountType(AccountType.CUSTOMER);
        //TODO error handling, input validation
        userService.registerUser(user);

        return login(model, Optional.of(false));
    }
}
