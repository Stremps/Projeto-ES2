package com.example.apiparticipantes.controller;

import com.example.apiparticipantes.dto.*;
import com.example.apiparticipantes.dto.RegisterRequest;
import com.example.apiparticipantes.dto.JwtResponse;
import com.example.apiparticipantes.dto.LoginRequest;
import com.example.apiparticipantes.model.*;
import com.example.apiparticipantes.repository.*;
import com.example.apiparticipantes.security.JwtTokenProvider;
import com.example.apiparticipantes.service.EmailService;
import com.example.apiparticipantes.service.LogradouroExtractorService; // Importar
import com.example.apiparticipantes.service.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus; // Importar HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional; // Importar Transactional
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Importar ResponseStatusException

import java.time.LocalDateTime; // Importar LocalDateTime
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final ParticipanteRepository participanteRepository;
    private final EnderecoRepository enderecoRepository;
    private final BairroRepository bairroRepository;
    private final CidadeRepository cidadeRepository;
    private final UnidadeFederacaoRepository unidadeFederacaoRepository;
    private final LogradouroRepository logradouroRepository;
    // TipoLogradouroRepository ainda é necessário pelo LogradouroExtractorService
    private final TipoLogradouroRepository tipoLogradouroRepository;
    private final LogradouroExtractorService logradouroExtractorService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    private static final List<String> ALLOWED_EMAIL_DOMAINS = Arrays.asList(
            "gmail.com", "hotmail.com", "outlook.com", "yahoo.com",
            "live.com", "icloud.com", "unioeste.br"
    );

    // Construtor corrigido e completo
    public AuthController(ParticipanteRepository participanteRepository,
                          EnderecoRepository enderecoRepository,
                          BairroRepository bairroRepository,
                          CidadeRepository cidadeRepository,
                          UnidadeFederacaoRepository unidadeFederacaoRepository,
                          LogradouroRepository logradouroRepository,
                          TipoLogradouroRepository tipoLogradouroRepository, // Incluir
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          TokenBlacklistService tokenBlacklistService,
                          PasswordResetTokenRepository tokenRepository,
                          LogradouroExtractorService logradouroExtractorService, // Incluir
                          EmailService emailService) {
        this.participanteRepository = participanteRepository;
        this.enderecoRepository = enderecoRepository;
        this.bairroRepository = bairroRepository;
        this.cidadeRepository = cidadeRepository;
        this.unidadeFederacaoRepository = unidadeFederacaoRepository;
        this.logradouroRepository = logradouroRepository;
        this.tipoLogradouroRepository = tipoLogradouroRepository; // Atribuir
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.tokenBlacklistService = tokenBlacklistService;
        this.tokenRepository = tokenRepository;
        this.logradouroExtractorService = logradouroExtractorService; // Atribuir
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // 1. Validações Iniciais
        if (participanteRepository.existsByEmailParticipante(request.getEmailParticipante())) {
            return ResponseEntity.badRequest().body("E-mail já registado");
        }
        if (request.getCargo() == Cargo.ADMIN) {
            return ResponseEntity.badRequest().body("Não é possível registar-se como ADMIN.");
        }
        String email = request.getEmailParticipante();
        if (email == null || !email.contains("@")) {
            return ResponseEntity.badRequest().body("Formato de e-mail inválido.");
        }
        String domain = email.substring(email.lastIndexOf("@") + 1).toLowerCase();
        if (!ALLOWED_EMAIL_DOMAINS.contains(domain)) {
            return ResponseEntity.badRequest().body("Domínio de e-mail não permitido.");
        }

        // 2. Processamento do Endereço
        UnidadeFederacao uf = unidadeFederacaoRepository.findById(request.getSiglaUf())
                .orElseGet(() -> unidadeFederacaoRepository.save(new UnidadeFederacao(request.getSiglaUf(), request.getSiglaUf())));

        Cidade cidade = cidadeRepository.findByNomeCidade(request.getNomeCidade())
                .orElseGet(() -> cidadeRepository.save(new Cidade(UUID.randomUUID().toString(), request.getNomeCidade(), uf)));

        Bairro bairro = bairroRepository.findByNomeBairro(request.getNomeBairro())
                .orElseGet(() -> bairroRepository.save(new Bairro(UUID.randomUUID().toString(), request.getNomeBairro())));

        // Extrai tipo e nome do logradouro a partir da string enviada
        LogradouroExtractorService.ExtractedLogradouro extracted =
                logradouroExtractorService.extract(request.getNomeLogradouro());
        TipoLogradouro tipoLogradouro = extracted.getTipoLogradouro(); // Tipo encontrado ou criado pelo serviço
        String nomeLogradouroExtraido = extracted.getNomeLogradouro(); // Nome sem o tipo

        // Busca ou cria o Logradouro usando o nome extraído e o tipo encontrado/criado
        Logradouro logradouro = logradouroRepository.findByNomeLogradouroAndTipoLogradouro(nomeLogradouroExtraido, tipoLogradouro)
                .orElseGet(() -> logradouroRepository.save(
                        new Logradouro(UUID.randomUUID().toString(), nomeLogradouroExtraido, tipoLogradouro))
                );

        // Cria e salva o Endereço
        Endereco endereco = new Endereco();
        endereco.setCep(request.getCep());
        endereco.setComplemento(request.getComplemento());
        endereco.setNumero(request.getNumero());
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setLogradouro(logradouro);
        Endereco enderecoSalvo = enderecoRepository.save(endereco); // Salva apenas uma vez

        // 3. Criação do Participante
        Participante p = new Participante(); // O ID é gerado no construtor
        p.setNomeParticipante(request.getNomeParticipante());
        p.setEmailParticipante(request.getEmailParticipante());
        p.setTelefoneParticipante(request.getTelefoneParticipante());
        p.setSenhaParticipante(passwordEncoder.encode(request.getSenhaParticipante()));
        p.setCargo(request.getCargo());
        p.setEndereco(enderecoSalvo); // Associa o endereço JÁ salvo

        participanteRepository.save(p);

        return ResponseEntity.ok("Registado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha());
        // A autenticação verifica se o user está ativo (CustomUserDetailsService)
        var auth = authenticationManager.authenticate(authToken);
        String token = tokenProvider.generateToken(request.getEmail());
        return ResponseEntity.ok(new JwtResponse(token, request.getEmail()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        if (token != null) {
            tokenBlacklistService.adicionarTokenNaBlacklist(token);
        }
        return ResponseEntity.ok("Logout realizado com sucesso.");
    }

    @PostMapping("/forgot-password")
    @Transactional
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Participante participante = participanteRepository.findByEmailParticipante(request.getEmail())
                .orElse(null);

        boolean emailSent = false;

        if (participante != null && participante.isAtivo()) {
            tokenRepository.deleteByParticipanteIdParticipante(participante.getIdParticipante());

            Random rnd = new Random();
            int number = rnd.nextInt(900000) + 100000;
            String codeValue = String.valueOf(number);

            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

            PasswordResetToken resetCode = new PasswordResetToken(codeValue, participante, expiryDate);
            tokenRepository.save(resetCode);

            emailSent = emailService.sendPasswordResetEmail(participante.getEmailParticipante(), codeValue);

            if (!emailSent) {
                // logger.error("Falha ao enviar e-mail de redefinição para {}", participante.getEmailParticipante());
            }
        }

        return ResponseEntity.ok("Se o e-mail estiver registrado e ativo, receberá instruções para redefinir a senha via E-mail.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        PasswordResetToken resetCode = tokenRepository.findByCode(request.getCode())
                .orElse(null);

        if (resetCode == null || resetCode.isExpired()) {
            return ResponseEntity.badRequest().body("Código inválido ou expirado.");
        }

        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            return ResponseEntity.badRequest().body("A nova senha deve ter pelo menos 6 caracteres.");
        }

        Participante participante = resetCode.getParticipante();

        if (!participante.isAtivo()) {
            tokenRepository.delete(resetCode);
            return ResponseEntity.badRequest().body("Utilizador inativo.");
        }

        participante.setSenhaParticipante(passwordEncoder.encode(request.getNewPassword()));
        participanteRepository.save(participante);

        tokenRepository.delete(resetCode);

        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
}