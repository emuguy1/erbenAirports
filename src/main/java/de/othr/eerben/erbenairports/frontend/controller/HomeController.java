package de.othr.eerben.erbenairports.frontend.controller;

import de.othr.eerben.erbenairports.backend.data.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String start(){
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET) // /login
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "authentication/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST) // th:action="@{/login}"
    public String doLogin() {
        return "index";
    }
}
