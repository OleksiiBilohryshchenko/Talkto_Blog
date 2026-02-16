package com.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",
                "/register",
                "/login",
                "/css/**",
                "/password/**",
                "/error/**"
            ).permitAll()
            .requestMatchers("/admin/**").denyAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/posts", true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        )
        .exceptionHandling(ex -> ex
            .accessDeniedHandler((request, response, accessDeniedException) ->
                response.sendError(403)
            )
        )
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        );

    return http.build();
  }
}