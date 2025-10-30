package com.example.apiparticipantes.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {

    // Usando um Set concorrente para segurança em ambientes multi-thread,
    // mas ainda assim volátil (perdido ao reiniciar).
    private final Set<String> blacklist = Collections.synchronizedSet(new HashSet<>());

    /**
     * Adiciona um token JWT à blacklist.
     * @param token O token a ser invalidado.
     */
    public void adicionarTokenNaBlacklist(String token) {
        blacklist.add(token);
        // Em produção, aqui você adicionaria o token a um Redis ou DB
        // com um tempo de expiração igual ao tempo restante do token.
    }

    /**
     * Verifica se um token está na blacklist.
     * @param token O token a ser verificado.
     * @return true se o token estiver na blacklist, false caso contrário.
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
        // Em produção, verificaria no Redis ou DB.
    }

    // Seria útil ter um mecanismo para limpar tokens expirados da blacklist,
    // especialmente se usar DB ou memória sem expiração automática.
}