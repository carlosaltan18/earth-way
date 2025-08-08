package com.uvg.earth.way.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.uvg.earth.way.configuration.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthFilter jwtAuthFilter;
    private AuthenticationProvider authenticationProvider;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final String ADMIN = "ROLE_ADMIN";
    private static final String USER = "ROLE_USER";
    private static final String ORGANIZATION = "ROLE_ORGANIZATION";

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/swagger-ui.html").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/v3/**").permitAll()
                                .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/api/v1/post/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers("/api/v1/post/listPost").permitAll()
                                .requestMatchers("/api/v1/user/**").hasAnyAuthority(USER, ADMIN, ORGANIZATION)
                                .requestMatchers("/api/v1/organization/**").hasAnyAuthority(ADMIN, ORGANIZATION)
                                .requestMatchers("/api/v1/report/**").hasAnyAuthority(USER, ADMIN, ORGANIZATION)
                                .requestMatchers("/api/v1/role/**").hasAnyAuthority(ADMIN)
                                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
