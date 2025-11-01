package com.example.apiparticipantes.dto;

public class AprovacaoRequest {

    // true = aprovar
    // false = rejeitar
    private boolean aprovar;

    // Getter e Setter
    public boolean isAprovar() {
        return aprovar;
    }

    public void setAprovar(boolean aprovar) {
        this.aprovar = aprovar;
    }
}