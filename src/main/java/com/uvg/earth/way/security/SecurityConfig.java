package com.uvg.earth.way.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthFilter jwtAuthFilter;
    private AuthenticationProvider authenticationProvider;
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String ORGANIZATION = "ORGANIZATION";


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/v3/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/api/v1/user/**").hasAnyRole(USER, ADMIN, ORGANIZATION)
                                .requestMatchers("/api/v1/organization/**").hasAnyRole(ADMIN, ORGANIZATION)
                                .requestMatchers("/api/v1/post/**").permitAll()
                                .requestMatchers("/api/v1/report/**").hasAnyRole(USER, ADMIN, ORGANIZATION)
                                .requestMatchers("/api/v1/role/**").hasAnyRole(ADMIN)
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
}