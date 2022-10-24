package ml.pic.tech.security.session.security.controller;

import ml.pic.tech.security.session.security.entity.ChangePassword;
import ml.pic.tech.security.session.security.entity.Utilisateur;
import ml.pic.tech.security.session.security.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UtilisateurController {

    private final AccountService service;

    public UtilisateurController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/utilisateur/add")
    public String addForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        model.addAttribute("roles", service.roleList());
        model.addAttribute("currentU", service.currentUtilisateur());

        return "utilisateur/ajout";
    }

    @PostMapping("/utilisateur/add")
    public String add(@ModelAttribute("utilisateur") @Valid Utilisateur utilisateur, Errors errors, Model model) {
        model.addAttribute("currentU", service.currentUtilisateur());

        if (!utilisateur.getConfirmation().equals(utilisateur.getPassword())) {
            errors.rejectValue("password", "", "Le password et la confirmation sont different");
            errors.rejectValue("confirmation", "", "Le password et la confirmation sont different");
            model.addAttribute("roles", service.roleList());
            return "utilisateur/ajout";
        }
        if(errors.hasErrors()){
            model.addAttribute("roles", service.roleList());
            return "utilisateur/ajout";
        }
        Utilisateur u = service.addUtilisateur(utilisateur);
        model.addAttribute("utilisateur", service.findById(u.getId()));
        return "utilisateur/details";
    }

    @GetMapping("/utilisateur/details")
    public String info(@RequestParam Long id, Model model){
        model.addAttribute("currentU", service.currentUtilisateur());
        model.addAttribute("utilisateur", service.findById(id));
        return "utilisateur/details";
    }

    @GetMapping("/utilisateur/update")
    public String update(@RequestParam Long id, Model model) {
        model.addAttribute("currentU", service.currentUtilisateur());
        model.addAttribute("utilisateur", service.findById(id));
        return "utilisateur/ajout";
    }

    @GetMapping("/utilisateur/delete")
    public String delete(@RequestParam Long id) {
        service.deleteById(id);
        return "redirect:/";
    }

    @GetMapping({"", "/"})
    public String liste(Model model) {
        model.addAttribute("currentU", service.currentUtilisateur());
        model.addAttribute("utilisateurs", service.utilisateurList());
        return "utilisateur/liste";
    }

    @GetMapping("/utilisateur/password")
    public String changePasswordForm(Model model) {
        model.addAttribute("changePassword", new ChangePassword());
        model.addAttribute("currentU", service.currentUtilisateur());

        return "utilisateur/password";
    }

    @PostMapping("/utilisateur/password")
    public String PasswordForm(@ModelAttribute("changePassword") @Valid ChangePassword changePassword,
                               Errors errors, Model model) {
        Utilisateur utilisateur = service.currentUtilisateur();
        String email = utilisateur.getEmail();

        String oldPassword = changePassword.getOldPassword();
        String newPassword = changePassword.getNewPassword();
        String confirmation = changePassword.getConfirmation();

        model.addAttribute("currentU", service.currentUtilisateur());

        if (service.passwordEncodeVerifie(oldPassword, utilisateur.getPassword())) {
            System.err.println("Ancien mot de passe " + oldPassword);
            System.err.println("Ancien mot de passe U " + utilisateur.getPassword());

            if (newPassword.equals(confirmation)) {
                service.updatePassword(email, newPassword);
            } else {
                errors.rejectValue("newPassword", "", "Le password et la confirmation sont different");

            }

        } else {
            errors.rejectValue("oldPassword", "", "L'ancien password est incorrect");

        }
        if (errors.hasErrors()) {
            return "utilisateur/password";
        } else {

            return "redirect:";
        }
    }

}
