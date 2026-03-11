package med.voll.web_application.infra.exception.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OncePerRequestFilter filterCustomPassword) throws Exception {
        return http
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/css/**", "/js/**", "/assets/**",
                            "/", "/index", "/home").permitAll();
                    req.requestMatchers("/pacientes/**").hasRole("ATENDENTE");
                    req.requestMatchers(HttpMethod.GET, "/medicos").hasAnyRole("ATENDENTE", "PACIENTE");
                    req.requestMatchers("/medicos/**").hasRole("ATENDENTE");
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(filterCustomPassword, UsernamePasswordAuthenticationFilter.class)
                .formLogin((form) ->
                        form
                                .loginPage("/login")
                                .defaultSuccessUrl("/").permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .addLogoutHandler(new SecurityContextLogoutHandler())
                        .permitAll())
                .rememberMe(rm -> rm.key("remember-me"))
                .csrf(crsfCustomizer -> Customizer.withDefaults())
        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
