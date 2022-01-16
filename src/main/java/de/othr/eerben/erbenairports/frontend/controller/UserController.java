package de.othr.eerben.erbenairports.frontend.controller;

import de.othr.eerben.erbenairports.backend.data.entities.User;
import de.othr.eerben.erbenairports.backend.exceptions.AirportException;
import de.othr.eerben.erbenairports.backend.services.UserServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String showEmployeeUserDetails(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "profile";
    }

}
