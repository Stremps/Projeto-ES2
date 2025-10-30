package com.example.apiparticipantes.service;

import com.example.apiparticipantes.model.TipoLogradouro;
import com.example.apiparticipantes.repository.TipoLogradouroRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LogradouroExtractorService {

    private final TipoLogradouroRepository tipoLogradouroRepository;

    // Lista de tipos conhecidos (pode ser expandida)
    private static final List<String> TIPOS_CONHECIDOS = Arrays.asList(
            "Rua", "Avenida", "Praça", "Travessa", "Alameda",
            "Estrada", "Largo", "Rodovia", "Viela", "Parque", "Jardim", "Quadra", "Praia"
            // Adicione abreviações se necessário: "Av.", "Tv.", etc.
    );

    public LogradouroExtractorService(TipoLogradouroRepository tipoLogradouroRepository) {
        this.tipoLogradouroRepository = tipoLogradouroRepository;
    }

    public ExtractedLogradouro extract(String fullLogradouro) {
        if (fullLogradouro == null || fullLogradouro.trim().isEmpty()) {
            return new ExtractedLogradouro(findOrCreateTipoLogradouro("Desconhecido"), "");
        }

        String nomeTipoEncontrado = "Desconhecido"; // Padrão
        String nomeLogradouroExtraido = fullLogradouro.trim();

        // Tenta encontrar um tipo conhecido no início da string
        for (String tipo : TIPOS_CONHECIDOS) {
            // Verifica se começa com o tipo + espaço (case-insensitive)
            if (nomeLogradouroExtraido.toLowerCase().startsWith(tipo.toLowerCase() + " ")) {
                nomeTipoEncontrado = tipo; // Encontrou o tipo
                // Remove o tipo e o espaço do início para obter o nome
                nomeLogradouroExtraido = nomeLogradouroExtraido.substring(tipo.length() + 1).trim();
                break; // Para após encontrar o primeiro tipo
            }
        }

        // Busca ou cria o TipoLogradouro no banco de dados
        TipoLogradouro tipoLogradouro = findOrCreateTipoLogradouro(nomeTipoEncontrado);

        return new ExtractedLogradouro(tipoLogradouro, nomeLogradouroExtraido);
    }

    /**
     * Busca um TipoLogradouro pelo nome ou cria um novo se não existir.
     */
    private TipoLogradouro findOrCreateTipoLogradouro(String nomeTipo) {
        // Normaliza o nome (ex: primeira letra maiúscula)
        String nomeNormalizado = nomeTipo.substring(0, 1).toUpperCase() + nomeTipo.substring(1).toLowerCase();

        return tipoLogradouroRepository.findByNomeTipoLogradouro(nomeNormalizado)
                .orElseGet(() -> {
                    TipoLogradouro novo = new TipoLogradouro();
                    novo.setIdTipoLogradouro(UUID.randomUUID().toString());
                    novo.setNomeTipoLogradouro(nomeNormalizado);
                    return tipoLogradouroRepository.save(novo);
                });
    }

    /**
     * Classe auxiliar interna para retornar os dois valores extraídos.
     */
    public static class ExtractedLogradouro {
        private final TipoLogradouro tipoLogradouro;
        private final String nomeLogradouro;

        public ExtractedLogradouro(TipoLogradouro tipoLogradouro, String nomeLogradouro) {
            this.tipoLogradouro = tipoLogradouro;
            this.nomeLogradouro = nomeLogradouro;
        }

        public TipoLogradouro getTipoLogradouro() { return tipoLogradouro; }
        public String getNomeLogradouro() { return nomeLogradouro; }
    }
}