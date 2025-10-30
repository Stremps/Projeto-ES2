package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.ParticipanteResponseDto;
import com.example.apiparticipantes.dto.ParticipanteUpdateRequestDto;
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
@RequestMapping("/api/participantes")

public class ParticipanteController {

    private final ParticipanteRepository participanteRepository;
    private final EnderecoRepository enderecoRepository;
    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;
    private final UnidadeFederacaoRepository unidadeFederacaoRepository;
    private final LogradouroRepository logradouroRepository;
    private final TipoLogradouroRepository tipoLogradouroRepository;


    public ParticipanteController(ParticipanteRepository participanteRepository, EnderecoRepository enderecoRepository, BairroRepository bairroRepository, CidadeRepository cidadeRepository, UnidadeFederacaoRepository unidadeFederacaoRepository, LogradouroRepository logradouroRepository, TipoLogradouroRepository tipoLogradouroRepository) {
        this.participanteRepository = participanteRepository;
        this.enderecoRepository = enderecoRepository;
        this.bairroRepository = bairroRepository;
        this.cidadeRepository = cidadeRepository;
        this.unidadeFederacaoRepository = unidadeFederacaoRepository;
        this.logradouroRepository = logradouroRepository;
        this.tipoLogradouroRepository = tipoLogradouroRepository;
    }

    /**
     * Rota para ADMIN listar APENAS os participantes ATIVOS.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ParticipanteResponseDto> listarTodosAtivos() { // Nome do método ligeiramente alterado para clareza
        return participanteRepository.findAll().stream()
                .filter(Participante::isAtivo) // <-- Filtra apenas os ativos
                .map(ParticipanteResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Rota para visualizar um participante específico (ADMIN ou o próprio participante).
     * Retorna 404 se o participante estiver inativo e o requisitante não for ADMIN.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ParticipanteResponseDto> visualizarParticipante(@PathVariable String id) {
        Participante participante = participanteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não encontrado."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        // Se não for admin E o participante estiver inativo, retorna não encontrado
        if (!isAdmin && !participante.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não encontrado.");
        }

        // Se for admin OU for o próprio utilizador (e participante está ativo ou inativo - admin vê tudo)
        if (!isAdmin && !participante.getEmailParticipante().equals(emailAutenticado)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado.");
        }

        return ResponseEntity.ok(new ParticipanteResponseDto(participante));
    }

    @GetMapping("/me")
    public ResponseEntity<ParticipanteResponseDto> getMeuPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = authentication.getName();

        Participante participante = participanteRepository.findByEmailParticipante(emailAutenticado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante autenticado não encontrado."));

        // --- VERIFICAÇÃO DE SEGURANÇA ---
        // Se o utilizador estiver inativo, negamos o acesso
        if (!participante.isAtivo()) {
            // Usar FORBIDDEN é semanticamente mais correto aqui, pois o utilizador existe mas não tem permissão
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Utilizador inativo.");
        }
        // --- FIM DA VERIFICAÇÃO ---

        ParticipanteResponseDto respostaDto = new ParticipanteResponseDto(participante);
        return ResponseEntity.ok(respostaDto);
    }

    /**
     * Rota para editar informações de um participante (ADMIN ou o próprio participante).
     * Não permite editar participantes inativos (exceto ADMIN para reativar, se implementado).
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @participanteSecurityService.isSelf(#id, authentication)")
    public ResponseEntity<ParticipanteResponseDto> editarParticipante(@PathVariable String id, @RequestBody ParticipanteUpdateRequestDto request) {
        Participante participante = participanteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não encontrado."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        // Impede a edição de participantes inativos por não-admins
        if (!isAdmin && !participante.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Não é possível editar um participante inativo.");
        }

        // Atualiza os dados pessoais
        participante.setNomeParticipante(request.getNomeParticipante());
        participante.setTelefoneParticipante(request.getTelefoneParticipante());

        // --- Lógica para atualizar/criar o endereço ---
        if (request.getCep() != null && !request.getCep().isEmpty()) {
            UnidadeFederacao uf = unidadeFederacaoRepository.findById(request.getSiglaUf())
                    .orElseGet(() -> unidadeFederacaoRepository.save(new UnidadeFederacao(request.getSiglaUf(), request.getSiglaUf())));
            Cidade cidade = cidadeRepository.findByNomeCidade(request.getNomeCidade())
                    .orElseGet(() -> cidadeRepository.save(new Cidade(UUID.randomUUID().toString(), request.getNomeCidade(), uf)));
            Bairro bairro = bairroRepository.findByNomeBairro(request.getNomeBairro())
                    .orElseGet(() -> bairroRepository.save(new Bairro(UUID.randomUUID().toString(), request.getNomeBairro())));
            TipoLogradouro tipoLogradouro = tipoLogradouroRepository.findByNomeTipoLogradouro(request.getNomeTipoLogradouro())
                    .orElseGet(() -> tipoLogradouroRepository.save(new TipoLogradouro(UUID.randomUUID().toString(), request.getNomeTipoLogradouro())));
            Logradouro logradouro = logradouroRepository.findByNomeLogradouro(request.getNomeLogradouro())
                    .orElseGet(() -> logradouroRepository.save(new Logradouro(UUID.randomUUID().toString(), request.getNomeLogradouro(), tipoLogradouro)));

            Endereco endereco = participante.getEndereco();
            if (endereco == null) {
                endereco = new Endereco();
            }
            endereco.setCep(request.getCep());
            endereco.setComplemento(request.getComplemento());
            endereco.setNumero(request.getNumero());
            endereco.setBairro(bairro);
            endereco.setCidade(cidade);
            endereco.setLogradouro(logradouro);
            Endereco enderecoSalvo = enderecoRepository.save(endereco);
            participante.setEndereco(enderecoSalvo);
        }

        Participante participanteSalvo = participanteRepository.save(participante);
        return ResponseEntity.ok(new ParticipanteResponseDto(participanteSalvo));
    }

    /**
     * Rota para o participante autenticado editar o SEU PRÓPRIO perfil.
     */
    @PutMapping("/me")
    public ResponseEntity<ParticipanteResponseDto> editarMeuPerfil(@RequestBody ParticipanteUpdateRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = authentication.getName();

        Participante participante = participanteRepository.findByEmailParticipante(emailAutenticado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante autenticado não encontrado."));

        // Não permitir edição se estiver inativo
        if (!participante.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Não é possível editar um participante inativo.");
        }

        // --- Chama um método auxiliar para a lógica de atualização ---
        Participante participanteAtualizado = atualizarDadosParticipante(participante, request);

        return ResponseEntity.ok(new ParticipanteResponseDto(participanteAtualizado));
    }

    /**
     * Rota para o participante autenticado desativar a SUA PRÓPRIA conta.
     * Utiliza exclusão lógica.
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> excluirMeuPerfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailAutenticado = authentication.getName();

        Participante participante = participanteRepository.findByEmailParticipante(emailAutenticado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante autenticado não encontrado."));

        // Segurança extra: Impedir que o Admin se auto-desative por esta rota
        if (participante.getCargo() == com.example.apiparticipantes.model.Cargo.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O administrador não pode ser desativado por esta rota.");
        }

        if (!participante.isAtivo()) {
            // Se já estiver inativo, apenas retorna sucesso (ação idempotente)
            return ResponseEntity.noContent().build();
        }

        participante.setAtivo(false);
        participanteRepository.save(participante);

        return ResponseEntity.noContent().build();
    }

    /**
     * Rota para ADMIN "excluir" (desativar) um participante.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> desativarParticipante(@PathVariable String id) { // Nome do método alterado
        Participante participante = participanteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participante não encontrado."));

        if (participante.getCargo() == com.example.apiparticipantes.model.Cargo.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível desativar o administrador principal.");
        }

        // --- LÓGICA DE EXCLUSÃO ALTERADA ---
        participante.setAtivo(false); // Apenas marca como inativo
        participanteRepository.save(participante); // Salva a alteração
        // --- FIM DA ALTERAÇÃO ---

        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    // --- Método Auxiliar para Atualização ---
    /**
     * Método privado para encapsular a lógica de atualização de dados do participante
     * (dados pessoais e endereço), chamado tanto por /me quanto por /{id}.
     */
    private Participante atualizarDadosParticipante(Participante participante, ParticipanteUpdateRequestDto request) {
        // Atualiza os dados pessoais
        participante.setNomeParticipante(request.getNomeParticipante());
        participante.setTelefoneParticipante(request.getTelefoneParticipante());

        // --- Lógica para atualizar/criar o endereço ---
        if (request.getCep() != null && !request.getCep().isEmpty()) {
            UnidadeFederacao uf = unidadeFederacaoRepository.findById(request.getSiglaUf())
                    .orElseGet(() -> unidadeFederacaoRepository.save(new UnidadeFederacao(request.getSiglaUf(), request.getSiglaUf())));
            Cidade cidade = cidadeRepository.findByNomeCidade(request.getNomeCidade())
                    .orElseGet(() -> cidadeRepository.save(new Cidade(UUID.randomUUID().toString(), request.getNomeCidade(), uf)));
            Bairro bairro = bairroRepository.findByNomeBairro(request.getNomeBairro())
                    .orElseGet(() -> bairroRepository.save(new Bairro(UUID.randomUUID().toString(), request.getNomeBairro())));
            TipoLogradouro tipoLogradouro = tipoLogradouroRepository.findByNomeTipoLogradouro(request.getNomeTipoLogradouro())
                    .orElseGet(() -> tipoLogradouroRepository.save(new TipoLogradouro(UUID.randomUUID().toString(), request.getNomeTipoLogradouro())));
            Logradouro logradouro = logradouroRepository.findByNomeLogradouro(request.getNomeLogradouro())
                    .orElseGet(() -> logradouroRepository.save(new Logradouro(UUID.randomUUID().toString(), request.getNomeLogradouro(), tipoLogradouro)));

            Endereco endereco = participante.getEndereco();
            if (endereco == null) {
                endereco = new Endereco();
            }
            endereco.setCep(request.getCep());
            endereco.setComplemento(request.getComplemento());
            endereco.setNumero(request.getNumero());
            endereco.setBairro(bairro);
            endereco.setCidade(cidade);
            endereco.setLogradouro(logradouro);
            Endereco enderecoSalvo = enderecoRepository.save(endereco);
            participante.setEndereco(enderecoSalvo);
        }

        // Salva e retorna o participante atualizado
        return participanteRepository.save(participante);
    }
}