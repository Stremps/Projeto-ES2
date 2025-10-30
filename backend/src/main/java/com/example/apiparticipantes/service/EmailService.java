package com.example.apiparticipantes.service;

import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // Adicionar Logger para registar erros ou sucessos
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendPasswordResetEmail(String to, String code) { // Parâmetro renomeado para 'code'
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Redefinição de Senha - Evento App");

            // Mensagem atualizada para usar "código" em vez de "token"
            message.setText("Recebemos uma solicitação para redefinir a sua senha.\n\n" +
                    "Use o seguinte código de verificação: " + code + "\n\n" + // Usar a variável 'code'
                    "Este código expirará em 15 minutos.\n\n" +
                    "Se não solicitou esta alteração, pode ignorar este e-mail.");

            mailSender.send(message);
            logger.info("E-mail de redefinição enviado com sucesso para {}", to);
            return true; // Indica sucesso
        } catch (Exception e) {
            // Regista o erro se o envio falhar
            logger.error("Erro ao enviar e-mail de redefinição para {}: {}", to, e.getMessage());
            return false; // Indica falha
        }
    }
}