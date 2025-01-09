package com.vincent.gallerybackend.security;

import com.vincent.gallerybackend.config.CookieUtils;
import com.vincent.gallerybackend.user.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String ROLE_ARTIST = "ARTIST";
    private static final String ROLE_USER = "USER";

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        //TODO: Check if all endpoints are up to date and still needed

                        // Role ARTIST
                        .requestMatchers(HttpMethod.POST, "/artwork").hasAuthority(ROLE_ARTIST)
                        .requestMatchers(HttpMethod.PUT, "/artwork").hasAuthority(ROLE_ARTIST)
                        .requestMatchers(HttpMethod.DELETE, "/artwork/**").hasAuthority(ROLE_ARTIST)

                        .requestMatchers(HttpMethod.GET, "/artist").hasAuthority(ROLE_ARTIST)

                        .requestMatchers(HttpMethod.POST, "/event").hasAuthority(ROLE_ARTIST)
                        .requestMatchers(HttpMethod.PUT, "/event").hasAuthority(ROLE_ARTIST)
                        .requestMatchers(HttpMethod.DELETE, "/event/**").hasAuthority(ROLE_ARTIST)

                        // Role ARTIST/USER
                        .requestMatchers(HttpMethod.PUT, "/user").hasAnyAuthority(ROLE_ARTIST, ROLE_USER)
                        .requestMatchers(HttpMethod.PUT, "/user/updateLanguage").hasAnyAuthority(ROLE_ARTIST, ROLE_USER)
                        .requestMatchers(HttpMethod.POST, "/user/updateFavoriteArtworks").hasAnyAuthority(ROLE_ARTIST, ROLE_USER)
                        .requestMatchers(HttpMethod.POST, "/user/updateAcceptedEvents").hasAnyAuthority(ROLE_ARTIST, ROLE_USER)
                        .requestMatchers(HttpMethod.GET, "/user/favoriteArtworkIds/**").hasAnyAuthority(ROLE_ARTIST, ROLE_USER)
                        .requestMatchers(HttpMethod.GET, "/user/acceptedEventIds/**").hasAnyAuthority(ROLE_ARTIST, ROLE_USER)
                        .requestMatchers(HttpMethod.GET, "/user/favoriteArtworks/**").hasAnyAuthority(ROLE_ARTIST, ROLE_USER)
                        .requestMatchers(HttpMethod.GET, "/user/requestedArtworks/**").hasAnyAuthority(ROLE_ARTIST, ROLE_USER)

                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        // Filter every request
                        new JwtAuthenticationFilter(customUserDetailsService, jwtUtils, cookieUtils),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
