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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;

import com.example.apiparticipantes.service.TokenBlacklistService;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Essencial para @PreAuthorize funcionar!
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtTokenProvider tokenProvider,
                          TokenBlacklistService tokenBlacklistService) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite as origens do frontend
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:8080"));
        // Permite os métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // Permite cabeçalhos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // 1. ROTAS PÚBLICAS
                        // Permite requisições OPTIONS (para CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Permite todas as rotas de autenticação (login, register, forgot-password, etc.)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Permite a busca de endereços (se for público, como busca por CEP)
                        .requestMatchers(HttpMethod.GET, "/api/address/lookup/**").permitAll()


                        // 2. ROTAS DE "ADMIN"
                        // As rotas de ADMIN (POST, PUT, DELETE em palestras, eventos, etc.)
                        // já estão protegidas pelo @PreAuthorize("hasAuthority('ADMIN')")
                        // nos seus controllers, graças ao @EnableMethodSecurity.

                        // CORREÇÃO DA FALHA DE SEGURANÇA:
                        // Apenas ADMIN pode ver/editar/deletar OUTROS participantes
                        .requestMatchers(HttpMethod.GET, "/api/participantes").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/participantes/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/participantes/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/participantes/*").hasAuthority("ADMIN")

                        // A rota GET /api/vinculos-evento (listar todos) também deve ser admin
                        // (Ela já está coberta pelo @PreAuthorize no seu controller, mas é bom ser explícito)
                        .requestMatchers(HttpMethod.GET, "/api/vinculos-evento").hasAuthority("ADMIN")


                        // 3. ROTAS DE USUÁRIO (AUTENTICADO)
                        // Usuário faz logout
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                        // Usuário gerencia o PRÓPRIO perfil
                        .requestMatchers("/api/participantes/me").authenticated() // Cobre GET, PUT, DELETE do /me

                        // Usuário pode LER (GET) dados do evento
                        .requestMatchers(HttpMethod.GET,"/api/eventos").authenticated()
                        .requestMatchers(HttpMethod.GET,"/api/eventos/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/palestras").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/palestras/**").authenticated() // Inclui /vagas
                        .requestMatchers(HttpMethod.GET, "/api/tipos-participacao").authenticated()

                        // Usuário pode se INSCREVER
                        //.requestMatchers(HttpMethod.POST, "/api/vinculos-evento/*/inscrever-se").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/vinculos-evento/*/inscrever-se").authenticated()
                        .requestMatchers("/api/inscricoes-palestra/**").authenticated() // POST para se inscrever, GET para ver as suas

                        // 4. QUALQUER OUTRA ROTA
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}