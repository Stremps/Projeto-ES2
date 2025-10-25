package com.example.apiparticipantes.config;

import com.example.apiparticipantes.security.JwtTokenProvider;
import com.example.apiparticipantes.security.JwtAuthenticationFilter;
import com.example.apiparticipantes.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Importar HttpMethod
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Importar EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy; // Importar SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // Importar CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource; // Importar CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Importar UrlBasedCorsConfigurationSource

import java.util.Arrays; // Importar Arrays
import java.util.List; // Importar List

@Configuration
@EnableWebSecurity // Adicionar esta anotação pode ajudar
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
                // HABILITA A CONFIGURAÇÃO CORS PRIMEIRO
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permite requisições OPTIONS ANTES de qualquer outra regra
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Rotas Públicas
                        .requestMatchers("/api/auth/**").permitAll()
                        // Rotas Autenticadas (exemplos mantidos)
                        .requestMatchers(HttpMethod.POST, "/api/vinculos-evento/*/inscrever-se").authenticated()
                        .requestMatchers("/api/inscricoes-palestra/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tipos-participacao").authenticated() // GET para listar tipos pode ser autenticado ou publico, depende da regra
                        .requestMatchers(HttpMethod.GET, "/api/palestras/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/eventos").authenticated() // Permitir GET de eventos para usuários logados
                        .requestMatchers(HttpMethod.GET, "/api/eventos/**").authenticated() // Permitir GET de evento específico para usuários logados

                        // Rotas de Admin (manter PUT, POST, DELETE etc. apenas para ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/eventos").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/eventos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/eventos/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/palestras").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/palestras/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/palestras/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/tipos-participacao/**").hasAuthority("ADMIN") // POST, PUT, DELETE de tipos
                        .requestMatchers(HttpMethod.GET, "/api/vinculos-evento").hasAuthority("ADMIN") // Listar todos os vinculos

                        // Qualquer outra rota exige autenticação
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}