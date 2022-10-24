package ml.pic.tech.security.session.security.controller;

import ml.pic.tech.security.session.security.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UtilisController {


    private final AccountService service;

    public UtilisController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String login() {
        return "utilisateur/login";
    }



}
