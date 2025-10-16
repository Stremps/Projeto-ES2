package com.example.apiparticipantes.config;

import com.example.apiparticipantes.model.Cargo;
import com.example.apiparticipantes.model.Participante;
import com.example.apiparticipantes.repository.ParticipanteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner initAdminUser(ParticipanteRepository participanteRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verifica se o admin já existe
            if (!participanteRepository.existsByEmailParticipante("admin@sistema.com")) {
                Participante admin = new Participante();
                admin.setNomeParticipante("Administrador");
                admin.setEmailParticipante("admin@sistema.com");
                // IMPORTANTE: Use uma senha forte e guarde-a em um local seguro (ex: variáveis de ambiente)
                admin.setSenhaParticipante(passwordEncoder.encode("admin123"));
                admin.setCargo(Cargo.ADMIN);
                admin.setTelefoneParticipante("000000000");

                participanteRepository.save(admin);
                System.out.println("Usuário ADMIN criado com sucesso!");
            }
        };
    }
}