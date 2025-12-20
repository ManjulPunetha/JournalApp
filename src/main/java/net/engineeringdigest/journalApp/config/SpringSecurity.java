package net.engineeringdigest.journalApp.config;

import net.engineeringdigest.journalApp.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity
{
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers(new AntPathRequestMatcher("/journal/**"),
                        new AntPathRequestMatcher("/users/", "PUT"),
                        new AntPathRequestMatcher("/users/", "POST"))
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .build();
    }

    /**
     * Configures the authentication provider.
     * <p>
     * This method sets up a {@link DaoAuthenticationProvider}, which retrieves user details
     * via {@link CustomUserDetailsService#loadUserByUsername(String)} and validates passwords
     * using the {@link #passwordEncoder()} defined in this class.
     * </p>
     *
     * @return the configured authentication provider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * Configures the PasswordEncoder bean for hashing and verifying passwords.
     * <p>
     * This bean is injected into the {@link DaoAuthenticationProvider} to verify that the
     * raw password provided during login matches the hashed password stored in the database.
     * </p>
     *
     * @return a new instance of {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
