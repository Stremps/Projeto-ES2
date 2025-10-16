package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.EventoInscricaoRequest;
import com.example.apiparticipantes.dto.VinculoEventoResponseDto;
import com.example.apiparticipantes.model.*;
import com.example.apiparticipantes.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vinculos-evento")
@CrossOrigin(origins = "*")
public class VinculoEventoController {

    private final VinculoEventoRepository vinculoEventoRepository;
    private final ParticipanteRepository participanteRepository;
    private final EventoRepository eventoRepository;
    private final TipoParticipacaoRepository tipoParticipacaoRepository;


    public VinculoEventoController(VinculoEventoRepository vinculoEventoRepository, ParticipanteRepository participanteRepository, EventoRepository eventoRepository, TipoParticipacaoRepository tipoParticipacaoRepository) {
        this.vinculoEventoRepository = vinculoEventoRepository;
        this.participanteRepository = participanteRepository;
        this.eventoRepository = eventoRepository;
        this.tipoParticipacaoRepository = tipoParticipacaoRepository;
    }

    @PostMapping("/{eventoId}/inscrever-se")
    public ResponseEntity<VinculoEventoResponseDto> inscreverEmEvento(@PathVariable Long eventoId, @RequestBody EventoInscricaoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Participante participante = participanteRepository.findByEmailParticipante(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não encontrado."));

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));

        TipoParticipacao tipoParticipacao = tipoParticipacaoRepository.findById(request.getTipoParticipacaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de participação não encontrado."));

        if (vinculoEventoRepository.existsByParticipanteAndEvento(participante, evento)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já está inscrito neste evento.");
        }

        VinculoEvento novoVinculo = new VinculoEvento();
        novoVinculo.setParticipante(participante);
        novoVinculo.setEvento(evento);
        novoVinculo.setTipoParticipacao(tipoParticipacao);
        novoVinculo.setDataInicio(evento.getDataInicio());

        VinculoEvento vinculoSalvo = vinculoEventoRepository.save(novoVinculo);

        // Converte a entidade salva para o DTO de resposta
        VinculoEventoResponseDto respostaDto = new VinculoEventoResponseDto(vinculoSalvo);

        // Retorna o DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(respostaDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<VinculoEventoResponseDto> listarVinculos() {
        return vinculoEventoRepository.findAll().stream()
                .map(VinculoEventoResponseDto::new)
                .collect(Collectors.toList());
    }
}