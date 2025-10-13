package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.RegisterRequest;
import com.example.apiparticipantes.dto.JwtResponse;
import com.example.apiparticipantes.dto.LoginRequest;
import com.example.apiparticipantes.model.*;
import com.example.apiparticipantes.repository.*;
import com.example.apiparticipantes.security.JwtTokenProvider;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final ParticipanteRepository participanteRepository;
    private final EnderecoRepository enderecoRepository;
    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;
    private final UnidadeFederacaoRepository unidadeFederacaoRepository;
    private final LogradouroRepository logradouroRepository;
    private final TipoLogradouroRepository tipoLogradouroRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthController(ParticipanteRepository participanteRepository,
                          EnderecoRepository enderecoRepository,
                          BairroRepository bairroRepository,
                          CidadeRepository cidadeRepository,
                          UnidadeFederacaoRepository unidadeFederacaoRepository,
                          LogradouroRepository logradouroRepository,
                          TipoLogradouroRepository tipoLogradouroRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider) {
        this.participanteRepository = participanteRepository;
        this.enderecoRepository = enderecoRepository;
        this.bairroRepository = bairroRepository;
        this.cidadeRepository = cidadeRepository;
        this.unidadeFederacaoRepository = unidadeFederacaoRepository;
        this.logradouroRepository = logradouroRepository;
        this.tipoLogradouroRepository = tipoLogradouroRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (participanteRepository.existsByEmailParticipante(request.getEmailParticipante())) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        // ====== UF ======
        UnidadeFederacao uf = unidadeFederacaoRepository.findById(request.getSiglaUf())
                .orElseGet(() -> {
                    UnidadeFederacao nova = new UnidadeFederacao();
                    nova.setSiglaUf(request.getSiglaUf());
                    nova.setNomeUf(request.getSiglaUf()); // ou use request.getNomeUf() se tiver
                    return unidadeFederacaoRepository.save(nova);
                });

        // ====== Cidade ======
        Cidade cidade = cidadeRepository.findByNomeCidade(request.getNomeCidade())
                .orElseGet(() -> {
                    Cidade nova = new Cidade();
                    nova.setIdCidade(UUID.randomUUID().toString());
                    nova.setNomeCidade(request.getNomeCidade());
                    nova.setUnidadeFederacao(uf);
                    return cidadeRepository.save(nova);
                });

        // ====== Bairro ======
        Bairro bairro = bairroRepository.findByNomeBairro(request.getNomeBairro())
                .orElseGet(() -> {
                    Bairro novo = new Bairro();
                    novo.setIdBairro(UUID.randomUUID().toString());
                    novo.setNomeBairro(request.getNomeBairro());
                    return bairroRepository.save(novo);
                });

        // ====== Tipo Logradouro ======
        TipoLogradouro tipo = tipoLogradouroRepository.findByNomeTipoLogradouro(request.getNomeTipoLogradouro())
                .orElseGet(() -> {
                    TipoLogradouro novo = new TipoLogradouro();
                    novo.setIdTipoLogradouro(UUID.randomUUID().toString());
                    novo.setNomeTipoLogradouro(request.getNomeTipoLogradouro());
                    return tipoLogradouroRepository.save(novo);
                });

        // ====== Logradouro ======
        Logradouro logradouro = logradouroRepository.findByNomeLogradouro(request.getNomeLogradouro())
                .orElseGet(() -> {
                    Logradouro novo = new Logradouro();
                    novo.setIdLogradouro(UUID.randomUUID().toString());
                    novo.setNomeLogradouro(request.getNomeLogradouro());
                    novo.setTipoLogradouro(tipo);
                    return logradouroRepository.save(novo);
                });

        // ====== Endereço ======
        Endereco endereco = new Endereco();
        endereco.setCep(request.getCep());
        endereco.setComplemento(request.getComplemento());
        endereco.setNumero(request.getNumero());
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setLogradouro(logradouro);
        enderecoRepository.save(endereco);

        // ====== Participante ======
        Participante p = new Participante();
        p.setIdParticipante(UUID.randomUUID().toString());
        p.setNomeParticipante(request.getNomeParticipante());
        p.setEmailParticipante(request.getEmailParticipante());
        p.setTelefoneParticipante(request.getTelefoneParticipante());
        p.setSenhaParticipante(passwordEncoder.encode(request.getSenhaParticipante()));
        p.setCargo(request.getCargo());
        p.setEndereco(endereco);

        participanteRepository.save(p);

        return ResponseEntity.ok("Registrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha());
        var auth = authenticationManager.authenticate(authToken);
        String token = tokenProvider.generateToken(request.getEmail());
        return ResponseEntity.ok(new JwtResponse(token, request.getEmail()));
    }
}
