package com.example.apiparticipantes.service;

import com.example.apiparticipantes.model.Participante;
import com.example.apiparticipantes.repository.ParticipanteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("participanteSecurityService") // Nome do Bean para usar em @PreAuthorize
public class ParticipanteSecurityService {

    private final ParticipanteRepository participanteRepository;

    public ParticipanteSecurityService(ParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }

    /**
     * Verifica se o utilizador autenticado é o mesmo que o participante com o ID fornecido.
     * @param participanteId O ID (UUID como String) do participante a verificar.
     * @param authentication O objeto de autenticação do utilizador logado.
     * @return true se for o mesmo utilizador, false caso contrário.
     */
    public boolean isSelf(String participanteId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String emailAutenticado = authentication.getName();
        Participante participante = participanteRepository.findById(participanteId).orElse(null);

        return participante != null && participante.getEmailParticipante().equals(emailAutenticado);
    }
}