package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.EventoRequest;
import com.example.apiparticipantes.dto.EventoResponseDto;
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
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "*")
public class EventoController {

    private final EventoRepository eventoRepository;
    private final EnderecoRepository enderecoRepository;
    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;
    private final UnidadeFederacaoRepository unidadeFederacaoRepository;
    private final LogradouroRepository logradouroRepository;
    private final TipoLogradouroRepository tipoLogradouroRepository;
    private final ParticipanteRepository participanteRepository;

    public EventoController(EventoRepository eventoRepository, EnderecoRepository enderecoRepository, BairroRepository bairroRepository, CidadeRepository cidadeRepository, UnidadeFederacaoRepository unidadeFederacaoRepository, LogradouroRepository logradouroRepository, TipoLogradouroRepository tipoLogradouroRepository, ParticipanteRepository participanteRepository) {
        this.eventoRepository = eventoRepository;
        this.enderecoRepository = enderecoRepository;
        this.bairroRepository = bairroRepository;
        this.cidadeRepository = cidadeRepository;
        this.unidadeFederacaoRepository = unidadeFederacaoRepository;
        this.logradouroRepository = logradouroRepository;
        this.tipoLogradouroRepository = tipoLogradouroRepository;
        this.participanteRepository = participanteRepository;
    }


    @GetMapping
    public List<EventoResponseDto> listarEventos() {
        return eventoRepository.findAll().stream()
                .map(EventoResponseDto::new)
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventoResponseDto> criarEvento(@RequestBody EventoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminEmail = authentication.getName();
        Participante admin = participanteRepository.findByEmailParticipante(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin não encontrado."));

        UnidadeFederacao uf = unidadeFederacaoRepository.findById(request.getSiglaUf()).orElseGet(() -> unidadeFederacaoRepository.save(new UnidadeFederacao(request.getSiglaUf(), request.getSiglaUf())));
        Cidade cidade = cidadeRepository.findByNomeCidade(request.getNomeCidade()).orElseGet(() -> cidadeRepository.save(new Cidade(UUID.randomUUID().toString(), request.getNomeCidade(), uf)));
        Bairro bairro = bairroRepository.findByNomeBairro(request.getNomeBairro()).orElseGet(() -> bairroRepository.save(new Bairro(UUID.randomUUID().toString(), request.getNomeBairro())));
        TipoLogradouro tipoLogradouro = tipoLogradouroRepository.findByNomeTipoLogradouro(request.getNomeTipoLogradouro()).orElseGet(() -> tipoLogradouroRepository.save(new TipoLogradouro(UUID.randomUUID().toString(), request.getNomeTipoLogradouro())));
        Logradouro logradouro = logradouroRepository.findByNomeLogradouro(request.getNomeLogradouro()).orElseGet(() -> logradouroRepository.save(new Logradouro(UUID.randomUUID().toString(), request.getNomeLogradouro(), tipoLogradouro)));

        Endereco endereco = new Endereco();
        endereco.setCep(request.getCep());
        endereco.setComplemento(request.getComplemento());
        endereco.setNumero(request.getNumero());
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setLogradouro(logradouro);
        Endereco enderecoSalvo = enderecoRepository.save(endereco);

        Evento novoEvento = new Evento();
        novoEvento.setNome(request.getNome());
        novoEvento.setDataInicio(request.getDataInicio());
        novoEvento.setDataFim(request.getDataFim());
        novoEvento.setHoraInicio(request.getHoraInicio());
        novoEvento.setHoraFim(request.getHoraFim());
        novoEvento.setDescricao(request.getDescricao());
        novoEvento.setEndereco(enderecoSalvo);
        novoEvento.setCriador(admin);

        Evento eventoSalvo = eventoRepository.save(novoEvento);

        // Converte a entidade salva para o DTO de resposta
        EventoResponseDto respostaDto = new EventoResponseDto(eventoSalvo);

        // Retorna o DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(respostaDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventoResponseDto> editarEvento(@PathVariable Long id, @RequestBody EventoRequest request) {
        // 1. Busca o evento existente no banco de dados
        Evento eventoExistente = eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));

        // 2. Atualiza os dados do evento com as informações da requisição
        eventoExistente.setNome(request.getNome());
        eventoExistente.setDataInicio(request.getDataInicio());
        eventoExistente.setDataFim(request.getDataFim());
        eventoExistente.setHoraInicio(request.getHoraInicio());
        eventoExistente.setHoraFim(request.getHoraFim());
        eventoExistente.setDescricao(request.getDescricao());

        // A lógica de endereço pode ser mais complexa (atualizar ou criar novo).
        // Por simplicidade, vamos apenas atualizar os campos do endereço existente.
        Endereco endereco = eventoExistente.getEndereco();
        endereco.setCep(request.getCep());
        // ... atualize os outros campos do endereço se necessário

        // 3. Salva o evento atualizado
        Evento eventoSalvo = eventoRepository.save(eventoExistente);

        // 4. Retorna a resposta com o DTO
        return ResponseEntity.ok(new EventoResponseDto(eventoSalvo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> excluirEvento(@PathVariable Long id) {
        // 1. Verifica se o evento existe antes de tentar deletar
        if (!eventoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado.");
        }

        // 2. Deleta o evento (e as palestras/vínculos associados, graças ao 'cascade')
        eventoRepository.deleteById(id);

        // 3. Retorna uma resposta 204 No Content, indicando sucesso
        return ResponseEntity.noContent().build();
    }
}