package com.example.apiparticipantes.service;

import com.example.apiparticipantes.model.Participante;
import com.example.apiparticipantes.repository.ParticipanteRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ParticipanteRepository participanteRepository;

    public CustomUserDetailsService(ParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Participante p = participanteRepository.findByEmailParticipante(username)
                .orElseThrow(() -> new UsernameNotFoundException("Participante não encontrado com email: " + username));

        return User.withUsername(p.getEmailParticipante())
                .password(p.getSenhaParticipante())
                .authorities(p.getCargo().name())
                .build();
    }
}
