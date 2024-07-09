package com.example.devicemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection for simplicity
                .authorizeHttpRequests(authorize -> {
                    // Permit all requests to the Swagger UI, API docs, and H2 console
                    authorize.requestMatchers(antMatcher("/swagger-ui/**"),
                                    antMatcher("/swagger-ui.html"),
                                    antMatcher("/v3/**"),
                                    antMatcher("/h2-console/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.GET, "/devices")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.GET, "/devices/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.PUT, "/devices/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.POST, "/devices/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.PATCH, "/devices/**")).permitAll()
                            .requestMatchers(antMatcher(HttpMethod.DELETE, "/devices/**")).permitAll()
                            .anyRequest().authenticated(); // Require authentication for any other requests
                })
                .headers(h -> h.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // Allow H2 console to be embedded in a frame
                .httpBasic(Customizer.withDefaults()) // Use basic HTTP authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Use stateless session management

        return httpSecurity.build();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(12);
//    }
}