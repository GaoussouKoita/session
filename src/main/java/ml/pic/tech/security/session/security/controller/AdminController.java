package ml.pic.tech.security.session.security.controller;

import ml.pic.tech.security.session.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class AdminController {
    @Autowired
    private AccountService service;

    @GetMapping
    public String dash(Model model) {
        model.addAttribute("currentU", service.currentUtilisateur());
        return "admin/dashboard";
    }

    @GetMapping("/liste")
    public String dashList(Model model) {
        model.addAttribute("currentU", service.currentUtilisateur());
        return "admin/liste";
    }

    @GetMapping("/login")
    public String login() {
                return "admin/login";
    }



}
