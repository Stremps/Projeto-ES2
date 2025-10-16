package com.example.apiparticipantes.config;

import com.example.apiparticipantes.security.JwtTokenProvider;
import com.example.apiparticipantes.security.JwtAuthenticationFilter;
import com.example.apiparticipantes.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Rotas Públicas (acessíveis sem token)
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. Rotas Específicas para Usuários Autenticados (qualquer cargo)
                        .requestMatchers(HttpMethod.POST, "/api/vinculos-evento/*/inscrever-se").authenticated()
                        .requestMatchers("/api/inscricoes-palestra/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tipos-participacao").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/palestras/**").authenticated() // Cobre /api/palestras e /api/palestras/{id}/vagas

                        // 3. Rotas Gerais para ADMIN (qualquer método: GET, POST, PUT, DELETE)
                        .requestMatchers("/api/eventos/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/palestras/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/tipos-participacao/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/vinculos-evento/**").hasAuthority("ADMIN")

                        // 4. Qualquer outra rota não especificada acima exige autenticação
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
