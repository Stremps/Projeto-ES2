package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.PalestraRequest;
import com.example.apiparticipantes.model.*;
import com.example.apiparticipantes.repository.*;
import org.springframework.http.HttpStatus;
import com.example.apiparticipantes.dto.VagasDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.apiparticipantes.dto.PalestraResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/palestras")
@CrossOrigin(origins = "*")
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
        // 1. Buscar o participante pelo E-MAIL que o ADMIN forneceu
        Participante palestrante = participanteRepository.findByEmailParticipante(palestraRequest.getPalestranteEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestrante com o e-mail fornecido não encontrado."));

        // ... (o resto da lógica permanece exatamente o mesmo)
        Evento evento = eventoRepository.findById(palestraRequest.getEventoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));

        TipoParticipacao tipoPalestrante = tipoParticipacaoRepository.findByNome("PALESTRANTE")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Tipo 'PALESTRANTE' não configurado."));

        if (vinculoEventoRepository.findByParticipanteAndEventoAndTipoParticipacao(palestrante, evento, tipoPalestrante).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O participante selecionado não está inscrito como palestrante neste evento.");
        }

        // ... (criação e salvamento da palestra)
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
        // 1. Busca a lista de entidades do banco
        List<Palestra> palestras = palestraRepository.findAll();

        // 2. Converte cada entidade 'Palestra' para o seu 'PalestraResponseDto'
        return palestras.stream()
                .map(PalestraResponseDto::new) // Para cada palestra, cria um DTO
                .collect(Collectors.toList()); // Coleta tudo em uma nova lista de DTOs
    }

    @GetMapping("/{id}/vagas")
    public ResponseEntity<VagasDto> getVagasInfo(@PathVariable Long id) {
        // 1. Busca a palestra pelo ID ou retorna erro 404 se não encontrar
        Palestra palestra = palestraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestra não encontrada."));

        // 2. Conta o número de inscritos usando o repositório
        long numeroDeInscritos = inscricaoPalestraRepository.countByPalestra(palestra);

        // 3. Pega o número total de vagas da entidade Palestra
        int totalVagas = palestra.getNumeroVagas();

        // 4. Calcula as vagas restantes
        long vagasRestantes = totalVagas - numeroDeInscritos;

        // Garante que o número não seja negativo
        if (vagasRestantes < 0) {
            vagasRestantes = 0;
        }

        // 5. Cria o objeto de resposta (DTO) e o retorna com status 200 OK
        VagasDto vagasDto = new VagasDto(totalVagas, numeroDeInscritos, vagasRestantes);
        return ResponseEntity.ok(vagasDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PalestraResponseDto> editarPalestra(@PathVariable Long id, @RequestBody PalestraRequest request) {
        // 1. Busca a palestra existente
        Palestra palestraExistente = palestraRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestra não encontrada."));

        // 2. Busca o novo palestrante e evento, se eles foram alterados
        Participante novoPalestrante = participanteRepository.findByEmailParticipante(request.getPalestranteEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestrante com o e-mail fornecido não encontrado."));

        Evento novoEvento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));

        // ... (você pode adicionar a mesma validação de vínculo do método criarPalestra aqui)

        // 3. Atualiza os dados da palestra
        palestraExistente.setTitulo(request.getTitulo());
        palestraExistente.setData(request.getData());
        palestraExistente.setHoraInicio(request.getHoraInicio());
        palestraExistente.setHoraFim(request.getHoraFim());
        palestraExistente.setDescricao(request.getDescricao());
        palestraExistente.setLocalInterno(request.getLocalInterno());
        palestraExistente.setNumeroVagas(request.getNumeroVagas());
        palestraExistente.setPalestrante(novoPalestrante);
        palestraExistente.setEvento(novoEvento);

        // 4. Salva a palestra atualizada
        Palestra palestraSalva = palestraRepository.save(palestraExistente);

        // 5. Retorna a resposta com o DTO
        return ResponseEntity.ok(new PalestraResponseDto(palestraSalva));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> excluirPalestra(@PathVariable Long id) {
        // 1. Verifica se a palestra existe
        if (!palestraRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Palestra não encontrada.");
        }

        // 2. Deleta a palestra (e as inscrições associadas)
        palestraRepository.deleteById(id);

        // 3. Retorna 204 No Content
        return ResponseEntity.noContent().build();
    }
}