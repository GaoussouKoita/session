package ml.pic.tech.security.session.security.config;

import ml.pic.tech.security.session.security.entity.Utilisateur;
import ml.pic.tech.security.session.security.service.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;

@EnableWebSecurity
public class SecurityConfig {

    private final AccountService service;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(AccountService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        return http.formLogin().loginPage("/login").permitAll()
                .and().authorizeRequests().antMatchers("/utilisateur/**" )
                    .hasAuthority("ADMINISTRATEUR")
                .and().authorizeRequests().antMatchers("/password")
                    .hasAnyAuthority("ADMINISTRATEUR", "UTILISATEUR")
                .and().authorizeRequests().antMatchers("/login", "/css/**", "/web**/**")
                    .permitAll()
                .and().authorizeRequests().anyRequest().authenticated()
                .and().build();

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Utilisateur utilisateur = service.findByUsername(username);
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                utilisateur.getRoles().forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
                });
                return new User(utilisateur.getEmail(), utilisateur.getPassword(), authorities);
            }
        });

        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }


}
