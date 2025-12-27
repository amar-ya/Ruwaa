package org.example.ruwaa.Config;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Config.JWT.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@org.springframework.context.annotation.Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class Configuration
{

    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/signup/customer",
                                "/api/v1/auth/signup/expert",
                                "/api/v1/auth/login",
                                "/api/v1/payment/thanks").permitAll()

                        .requestMatchers("/api/v1/review/finished",
                                "/api/v1/review//unfinished").hasAuthority("EXPERT")

                        .requestMatchers("/api/v1/expert//most-active/category/{category}",
                                "/api/v1/post/create",
                                "/api/v1/review/request-review/{postId}/{expertId}").hasAuthority("CUSTOMER")

                        .requestMatchers("/api/v1/auth/me").hasAnyAuthority("CUSTOMER", "EXPERT")

                        .requestMatchers("/api/v1/category/create",
                                "/api/v1/category/get",
                                "/api/v1/category/get/{name}",
                                "/api/v1/category/update/{id}",
                                "/api/v1/category/delete/{id}").hasAuthority("ADMIN")

                        .anyRequest().authenticated())

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
