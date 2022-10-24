package ml.pic.tech.security.session.security.service;

import ml.pic.tech.security.session.security.entity.Role;
import ml.pic.tech.security.session.security.entity.Utilisateur;
import ml.pic.tech.security.session.security.repository.RoleRepository;
import ml.pic.tech.security.session.security.repository.UtilisateurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountService {


    private final UtilisateurRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public AccountService(UtilisateurRepository repository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Utilisateur addUtilisateur(Utilisateur utilisateur) {

        if (utilisateur.getConfirmation().equals(utilisateur.getPassword())) {
            utilisateur.setPassword(passwordEncoder
                    .encode(utilisateur.getPassword()));
            return repository.save(utilisateur);
        } else {
            try {
                throw new Exception("Le mot de passe est different de la confirmation");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Role addRole(Role role) {
        return roleRepository.save(role);
    }


    public Utilisateur findByUsername(String email) {
        return repository.findByEmail(email);
    }

    public List<Utilisateur> utilisateurList() {
        return repository.findAll();
    }


    public Page<Utilisateur> utilisateurPage(int page) {
        return repository.findAll(PageRequest.of(page, 5, Sort.by("prenom").and(Sort.by("nom"))));
    }

    public List<Role> roleList() {
        return roleRepository.findAll(Sort.by("roleName"));
    }

    public Utilisateur findById(Long id) {
        return repository.findById(id).get();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Utilisateur currentUtilisateur() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Utilisateur utilisateur = repository.findByEmail(email);
        return utilisateur;
    }

    public void updatePassword(String email, String passwordNew) {
        repository.updatePasswordByEmail(email, passwordEncoder.encode(passwordNew));
    }

    public boolean passwordEncodeVerifie(String password, String encodedPassword) {

        return passwordEncoder.matches(password, encodedPassword);
    }
}
