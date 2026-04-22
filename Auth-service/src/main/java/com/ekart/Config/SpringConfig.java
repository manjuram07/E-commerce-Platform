package com.ekart.Config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

public class SpringConfig {

    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                                        .requestMatchers("/Ekart/payment-api/**").denyAll()
                                                        .requestMatchers("/Ekart/customercart-api/**").denyAll()
                                                        .requestMatchers("/Ekart/product-api/**").denyAll()
                                                        .anyRequest().authenticated()
                                          )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .logout(LogoutConfigurer::permitAll);



        return httpSecurity.build();
    }
}
