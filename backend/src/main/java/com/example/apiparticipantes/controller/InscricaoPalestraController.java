package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.InscricaoPalestraResponseDto;
import com.example.apiparticipantes.dto.InscricaoRequest;
import com.example.apiparticipantes.model.InscricaoPalestra;
import com.example.apiparticipantes.model.Palestra;
import com.example.apiparticipantes.model.Participante;
import com.example.apiparticipantes.repository.InscricaoPalestraRepository;
import com.example.apiparticipantes.repository.PalestraRepository;
import com.example.apiparticipantes.repository.ParticipanteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inscricoes-palestra")
@CrossOrigin(origins = "*")
public class InscricaoPalestraController {

    private final InscricaoPalestraRepository inscricaoRepository;
    private final ParticipanteRepository participanteRepository;
    private final PalestraRepository palestraRepository;

    public InscricaoPalestraController(InscricaoPalestraRepository inscricaoRepository,
                                       ParticipanteRepository participanteRepository,
                                       PalestraRepository palestraRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.participanteRepository = participanteRepository;
        this.palestraRepository = palestraRepository;
    }

    @GetMapping
    public List<InscricaoPalestraResponseDto> listarInscricoes() {
        return inscricaoRepository.findAll().stream()
                .map(InscricaoPalestraResponseDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<InscricaoPalestraResponseDto> inscreverEmPalestra(@RequestBody InscricaoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Participante participante = participanteRepository.findByEmailParticipante(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não encontrado."));

        Palestra palestra = palestraRepository.findById(request.getPalestraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestra não encontrada."));

        if (palestra.getPalestrante().getIdParticipante().equals(participante.getIdParticipante())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você não pode se inscrever na sua própria palestra.");
        }

        if (inscricaoRepository.existsByParticipanteAndPalestra(participante, palestra)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já está inscrito nesta palestra.");
        }

        long numeroDeInscritos = inscricaoRepository.countByPalestra(palestra);
        if (numeroDeInscritos >= palestra.getNumeroVagas()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não há mais vagas para esta palestra.");
        }

        InscricaoPalestra novaInscricao = new InscricaoPalestra();
        novaInscricao.setParticipante(participante);
        novaInscricao.setPalestra(palestra);
        novaInscricao.setData(LocalDate.now());

        InscricaoPalestra inscricaoSalva = inscricaoRepository.save(novaInscricao);
        InscricaoPalestraResponseDto respostaDto = new InscricaoPalestraResponseDto(inscricaoSalva);
        return ResponseEntity.status(HttpStatus.CREATED).body(respostaDto);
    }
}