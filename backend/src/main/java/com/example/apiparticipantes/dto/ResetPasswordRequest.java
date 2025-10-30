package com.example.apiparticipantes.dto;

public class ResetPasswordRequest {
    private String code; // Certifique-se que o NOME DO CAMPO é 'code'
    private String newPassword;

    // --- Certifique-se que o NOME DO MÉTODO GETTER é 'getCode' ---
    public String getCode() {
        return code;
    }
    // --- FIM DA VERIFICAÇÃO ---

    public void setCode(String code) {
        this.code = code;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}