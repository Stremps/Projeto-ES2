package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.PalestraRequest;
import com.example.apiparticipantes.model.*;
import com.example.apiparticipantes.repository.*;
import org.springframework.http.HttpStatus;
import com.example.apiparticipantes.dto.VagasDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importe este
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.apiparticipantes.dto.PalestraResponseDto;

import java.util.List;
// import java.util.Optional; // Não é mais necessário com o stream
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/palestras")
public class PalestraController {

    // Injeção de todas as dependências necessárias
    private final PalestraRepository palestraRepository;
    private final ParticipanteRepository participanteRepository;
    private final EventoRepository eventoRepository;
    private final VinculoEventoRepository vinculoEventoRepository;
    private final TipoParticipacaoRepository tipoParticipacaoRepository;
    private final InscricaoPalestraRepository inscricaoPalestraRepository;

    public PalestraController(PalestraRepository palestraRepository,
                              ParticipanteRepository participanteRepository,
                              EventoRepository eventoRepository,
                              VinculoEventoRepository vinculoEventoRepository,
                              TipoParticipacaoRepository tipoParticipacaoRepository,
                              InscricaoPalestraRepository inscricaoPalestraRepository) {
        this.palestraRepository = palestraRepository;
        this.participanteRepository = participanteRepository;
        this.eventoRepository = eventoRepository;
        this.vinculoEventoRepository = vinculoEventoRepository;
        this.tipoParticipacaoRepository = tipoParticipacaoRepository;
        this.inscricaoPalestraRepository = inscricaoPalestraRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PalestraResponseDto> criarPalestra(@RequestBody PalestraRequest palestraRequest) {

        // 1. Buscar o participante pelo E-MAIL
        Participante palestrante = participanteRepository.findByEmailParticipante(palestraRequest.getPalestranteEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestrante com o e-mail fornecido não encontrado."));

        // 2. Buscar o Evento
        Evento evento = eventoRepository.findById(palestraRequest.getEventoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));

        // 3. Buscar o Tipo de Participação "PALESTRANTE"
        TipoParticipacao tipoPalestrante = tipoParticipacaoRepository.findByNome("PALESTRANTE")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Tipo 'PALESTRANTE' não configurado."));

        // --- VALIDAÇÃO (A CORREÇÃO ESTÁ AQUI) ---
        // 4. Verificar se o palestrante tem um VÍNCULO APROVADO para este evento
        vinculoEventoRepository.findByParticipanteAndEventoAndTipoParticipacaoAndStatus(
                palestrante,
                evento,
                tipoPalestrante,
                StatusInscricao.CONFIRMADA // <-- Verificamos o status!
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "O participante selecionado não possui um vínculo APROVADO como palestrante para este evento."));
        // --- FIM DA VALIDAÇÃO ---

        // 5. Criar e salvar a palestra
        Palestra novaPalestra = new Palestra();
        novaPalestra.setTitulo(palestraRequest.getTitulo());
        novaPalestra.setData(palestraRequest.getData());
        novaPalestra.setHoraInicio(palestraRequest.getHoraInicio());
        novaPalestra.setHoraFim(palestraRequest.getHoraFim());
        novaPalestra.setDescricao(palestraRequest.getDescricao());
        novaPalestra.setLocalInterno(palestraRequest.getLocalInterno());
        novaPalestra.setNumeroVagas(palestraRequest.getNumeroVagas());
        novaPalestra.setEvento(evento);
        novaPalestra.setPalestrante(palestrante);

        Palestra palestraSalva = palestraRepository.save(novaPalestra);
        PalestraResponseDto respostaDto = new PalestraResponseDto(palestraSalva);
        return ResponseEntity.status(HttpStatus.CREATED).body(respostaDto);
    }

    @GetMapping
    public List<PalestraResponseDto> listarPalestras() {
        List<Palestra> palestras = palestraRepository.findAll();
        return palestras.stream()
                .map(PalestraResponseDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/vagas")
    public ResponseEntity<VagasDto> getVagasInfo(@PathVariable Long id) {
        Palestra palestra = palestraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestra não encontrada."));

        long numeroDeInscritos = inscricaoPalestraRepository.countByPalestra(palestra);
        int totalVagas = palestra.getNumeroVagas();
        long vagasRestantes = Math.max(0, totalVagas - numeroDeInscritos); // Garante que não seja negativo

        VagasDto vagasDto = new VagasDto(totalVagas, numeroDeInscritos, vagasRestantes);
        return ResponseEntity.ok(vagasDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PalestraResponseDto> editarPalestra(@PathVariable Long id, @RequestBody PalestraRequest request) {

        Palestra palestraExistente = palestraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestra não encontrada."));

        Participante novoPalestrante = participanteRepository.findByEmailParticipante(request.getPalestranteEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestrante com o e-mail fornecido não encontrado."));

        Evento novoEvento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));

        // --- VALIDAÇÃO (CORREÇÃO ADICIONADA AQUI) ---
        // 1. Buscar o Tipo de Participação "PALESTRANTE"
        TipoParticipacao tipoPalestrante = tipoParticipacaoRepository.findByNome("PALESTRANTE")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Tipo 'PALESTRANTE' não configurado."));

        // 2. Verificar se o palestrante (novo ou o mesmo) tem o vínculo APROVADO
        vinculoEventoRepository.findByParticipanteAndEventoAndTipoParticipacaoAndStatus(
                novoPalestrante,
                novoEvento,
                tipoPalestrante,
                StatusInscricao.CONFIRMADA
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "O participante selecionado não possui um vínculo APROVADO como palestrante para este evento."));
        // --- FIM DA VALIDAÇÃO ---


        palestraExistente.setTitulo(request.getTitulo());
        palestraExistente.setData(request.getData());
        palestraExistente.setHoraInicio(request.getHoraInicio());
        palestraExistente.setHoraFim(request.getHoraFim());
        palestraExistente.setDescricao(request.getDescricao());
        palestraExistente.setLocalInterno(request.getLocalInterno());
        palestraExistente.setNumeroVagas(request.getNumeroVagas());
        palestraExistente.setPalestrante(novoPalestrante);
        palestraExistente.setEvento(novoEvento);

        Palestra palestraSalva = palestraRepository.save(palestraExistente);
        return ResponseEntity.ok(new PalestraResponseDto(palestraSalva));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> excluirPalestra(@PathVariable Long id) {
        if (!palestraRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestra não encontrada.");
        }

        // A configuração `cascade = CascadeType.ALL` em Palestra.java
        // deve garantir que as InscricaoPalestra sejam removidas juntas.
        palestraRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}