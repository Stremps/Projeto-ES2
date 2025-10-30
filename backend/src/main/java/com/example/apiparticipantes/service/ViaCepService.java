package com.example.apiparticipantes.service;

import com.example.apiparticipantes.dto.ViaCepResponseDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ViaCepService {

    private final RestTemplate restTemplate;
    private final String viaCepUrl = "https://viacep.com.br/ws/";

    public ViaCepService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Busca um endereço na API ViaCEP pelo CEP fornecido.
     * @param cep O CEP a ser consultado (apenas números).
     * @return Um Optional contendo o ViaCepResponseDto se encontrado e válido,
     * ou Optional.empty() se o CEP não for encontrado ou inválido.
     */
    public Optional<ViaCepResponseDto> getAddressByCep(String cep) {
        // Remove caracteres não numéricos do CEP
        String cleanedCep = cep.replaceAll("[^0-9]", "");

        if (cleanedCep.length() != 8) {
            // CEP inválido, retorna vazio ou lança exceção, dependendo da sua preferência
            // Lançar exceção pode ser melhor para informar o erro ao frontend
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de CEP inválido.");
            // return Optional.empty();
        }

        String url = viaCepUrl + cleanedCep + "/json/";

        try {
            ViaCepResponseDto response = restTemplate.getForObject(url, ViaCepResponseDto.class);

            // Verifica se o ViaCEP retornou um erro (CEP não existente)
            if (response != null && response.isErro()) {
                return Optional.empty(); // CEP não encontrado
            }

            return Optional.ofNullable(response);

        } catch (HttpClientErrorException e) {
            // Trata erros HTTP, como 400 Bad Request se o formato estiver errado
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de CEP inválido (ViaCEP).");
            }
            // Logar outros erros HTTP
            // logger.error("Erro ao chamar ViaCEP: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao consultar serviço de CEP.");
            // return Optional.empty();
        } catch (Exception e) {
            // Logar outros erros inesperados
            // logger.error("Erro inesperado ao chamar ViaCEP: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao consultar serviço de CEP.");
            // return Optional.empty();
        }
    }
}