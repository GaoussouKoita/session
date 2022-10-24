package ml.pic.tech.security.session.security.config;

import ml.pic.tech.security.session.security.entity.Role;
import ml.pic.tech.security.session.security.entity.Utilisateur;
import ml.pic.tech.security.session.security.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
public class Utilis {

   @Bean
    CommandLineRunner commandLineRunner(AccountService service) {
        return args -> {
            Role role1 = new Role(1L, "ADMINISTRATEUR");
            Role role2 = new Role(2L, "UTILISATEUR");
            Collection<Role> roles = new ArrayList<>();
            roles.add(role1);
            roles.add(role2);

            roles.forEach(role -> service.addRole(role));
            service.addUtilisateur(new Utilisateur(1L, "Gaoussou", "KOITA", 76684788L,
                    "Bamako-Mali", "admin@g", "1234","1234", roles));
        };
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
