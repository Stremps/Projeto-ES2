package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.AprovacaoRequest;
import com.example.apiparticipantes.dto.EventoInscricaoRequest;
import com.example.apiparticipantes.dto.VinculoEventoResponseDto;
import com.example.apiparticipantes.model.*;
import com.example.apiparticipantes.repository.*;
import com.example.apiparticipantes.service.EmailService; // 1. IMPORTAR O SERVIÇO
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
    private final EmailService emailService; // 2. DECLARAR O SERVIÇO

    // 3. INJETAR O SERVIÇO NO CONSTRUTOR
    public VinculoEventoController(VinculoEventoRepository vinculoEventoRepository,
                                   ParticipanteRepository participanteRepository,
                                   EventoRepository eventoRepository,
                                   TipoParticipacaoRepository tipoParticipacaoRepository,
                                   EmailService emailService) { // Adicionar aqui
        this.vinculoEventoRepository = vinculoEventoRepository;
        this.participanteRepository = participanteRepository;
        this.eventoRepository = eventoRepository;
        this.tipoParticipacaoRepository = tipoParticipacaoRepository;
        this.emailService = emailService; // Atribuir aqui
    }

    /**
     * Endpoint para um participante solicitar um vínculo a um evento.
     */
    @PostMapping("/{eventoId}/inscrever-se")
    public ResponseEntity<String> inscreverEmEvento(@PathVariable Long eventoId, @RequestBody EventoInscricaoRequest request) {
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
        novoVinculo.setDataFim(evento.getDataFim());

        String mensagemResposta;
        if (tipoParticipacao.isRequerAprovacao()) {
            novoVinculo.setStatus(StatusInscricao.PENDENTE);
            mensagemResposta = "Solicitação para " + tipoParticipacao.getNome() + " registrada. Aguardando aprovação do administrador.";
        } else {
            novoVinculo.setStatus(StatusInscricao.CONFIRMADA);
            mensagemResposta = "Inscrição como " + tipoParticipacao.getNome() + " realizada com sucesso!";
        }

        vinculoEventoRepository.save(novoVinculo);

        // --- 4. CHAMADA PARA O E-MAIL (REQ 2) ---
        // Se o status for PENDENTE, envia o e-mail de aviso
        if (novoVinculo.getStatus() == StatusInscricao.PENDENTE) {
            emailService.sendVinculoPendenteEmail(
                    participante.getEmailParticipante(),
                    participante.getNomeParticipante(),
                    evento.getNome(),
                    tipoParticipacao.getNome()
            );
        }
        // (Você pode adicionar um 'else' aqui para enviar e-mail de confirmação imediata se quiser)
        // --- FIM DA CHAMADA ---

        return ResponseEntity.status(HttpStatus.CREATED).body(mensagemResposta);
    }

    // ... (Método listarVinculos() permanece o mesmo) ...
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<VinculoEventoResponseDto> listarVinculos() {
        return vinculoEventoRepository.findAll().stream()
                .map(VinculoEventoResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para o ADMIN listar apenas vínculos PENDENTES.
     */
    @GetMapping("/admin/pendentes")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<VinculoEventoResponseDto> listarPendentes() {
        return vinculoEventoRepository.findByStatus(StatusInscricao.PENDENTE).stream()
                .map(VinculoEventoResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para o ADMIN aprovar ou rejeitar um vínculo pendente.
     */
    @PostMapping("/admin/gerenciar/{idVinculo}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> gerenciarVinculo(@PathVariable Long idVinculo, @RequestBody AprovacaoRequest request) {

        VinculoEvento vinculo = vinculoEventoRepository.findById(idVinculo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vínculo não encontrado."));

        if (vinculo.getStatus() != StatusInscricao.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este vínculo não está pendente.");
        }

        if (request.isAprovar()) {
            vinculo.setStatus(StatusInscricao.CONFIRMADA);
            vinculoEventoRepository.save(vinculo);

            // --- 5. CHAMADA PARA O E-MAIL DE APROVAÇÃO (REQ 2) ---
            emailService.sendVinculoStatusUpdateEmail(
                    vinculo.getParticipante().getEmailParticipante(),
                    vinculo.getParticipante().getNomeParticipante(),
                    vinculo.getEvento().getNome(),
                    vinculo.getTipoParticipacao().getNome(),
                    true // Aprovado
            );
            // --- FIM DA CHAMADA ---

            return ResponseEntity.ok("Vínculo APROVADO com sucesso. E-mail enviado ao participante.");

        } else {
            vinculo.setStatus(StatusInscricao.REJEITADA);
            vinculoEventoRepository.save(vinculo);

            // --- 6. CHAMADA PARA O E-MAIL DE REJEIÇÃO (REQ 2) ---
            emailService.sendVinculoStatusUpdateEmail(
                    vinculo.getParticipante().getEmailParticipante(),
                    vinculo.getParticipante().getNomeParticipante(),
                    vinculo.getEvento().getNome(),
                    vinculo.getTipoParticipacao().getNome(),
                    false // Rejeitado
            );
            // --- FIM DA CHAMADA ---

            return ResponseEntity.ok("Vínculo REJEITADO. E-mail enviado ao participante.");
        }
    }
}