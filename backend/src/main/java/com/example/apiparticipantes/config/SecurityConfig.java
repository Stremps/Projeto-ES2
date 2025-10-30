package com.example.apiparticipantes.config;

import com.example.apiparticipantes.security.JwtTokenProvider;
import com.example.apiparticipantes.security.JwtAuthenticationFilter;
import com.example.apiparticipantes.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // <-- ADICIONADO
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;

import com.example.apiparticipantes.service.TokenBlacklistService;

// Imports de CORS adicionados do arquivo funcional
import org.springframework.web.cors.CorsConfiguration; // <-- ADICIONADO
import org.springframework.web.cors.CorsConfigurationSource; // <-- ADICIONADO
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // <-- ADICIONADO
import java.util.Arrays; // <-- ADICIONADO

@Configuration
@EnableWebSecurity // <-- ADICIONADO (Importante para habilitar a segurança web)
@EnableMethodSecurity // <-- MANTIDO (Bom para @PreAuthorize)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtTokenProvider tokenProvider,
                          TokenBlacklistService tokenBlacklistService) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.tokenBlacklistService = tokenBlacklistService; // <-- Lógica nova mantida
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        // Usa a nova implementação com blacklist
        return new JwtAuthenticationFilter(tokenProvider, userDetailsService, tokenBlacklistService);
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

    // <-- BEAN DE CORS ADICIONADO DO ARQUIVO FUNCIONAL -->
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite as origens do frontend (desenvolvimento e produção)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8080"));
        // Permite os métodos HTTP mais comuns
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // Permite cabeçalhos comuns, incluindo Authorization para o JWT
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a configuração a todas as rotas da API
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // HABILITA A CONFIGURAÇÃO CORS (como no arquivo funcional)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- ADICIONADO
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permite requisições OPTIONS (essencial para CORS pre-flight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // <-- ADICIONADO

                        // 1. Rotas Públicas (mantidas do 'novo')
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated() // Lógica nova mantida
                        .requestMatchers(HttpMethod.GET, "/api/address/lookup/**").permitAll() // Lógica nova mantida

                        // 2. Rotas Específicas para Usuários Autenticados (mantidas do 'novo')
                        .requestMatchers(HttpMethod.GET,"/api/eventos").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/vinculos-evento/*/inscrever-se").authenticated()
                        .requestMatchers("/api/inscricoes-palestra/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tipos-participacao").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/palestras/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/participantes/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/participantes/me").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/participantes/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/participantes/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/participantes/*").authenticated()

                        // 3. Rotas Gerais para ADMIN (mantidas do 'novo')
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