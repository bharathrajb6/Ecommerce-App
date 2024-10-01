package com.example.user_service.config;

import com.example.user_service.Filter.JwtAuthenticationFilter;
import com.example.user_service.Service.Impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFiler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(
                        req -> req.requestMatchers("login/**", "/register/**", "/refreshToken/**").permitAll().
                                requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN").
                                requestMatchers("/api/v1/user/**").hasAuthority("USER").anyRequest().authenticated()).
                userDetailsService(userService).
                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                addFilterBefore(jwtAuthenticationFiler, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
