package com.egercibrahim.creditModule.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").hasRole("ADMIN")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/credit/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/credit/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/credit/customer").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/credit/customer").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/credit/loan").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/credit/loan").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/credit/installment").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/credit/installment").permitAll()
                        .anyRequest().authenticated())
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
