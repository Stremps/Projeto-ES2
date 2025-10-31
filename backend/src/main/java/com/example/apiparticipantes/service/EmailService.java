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

    /**
     * NOVO MÉTODO (REQ 2): E-mail enviado quando um palestrante solicita o vínculo.
     * Informa que a solicitação está pendente.
     */
    public void sendVinculoPendenteEmail(String to, String nomeParticipante, String nomeEvento, String tipoParticipacao) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Sua solicitação de vínculo foi recebida - Infinity Events");
            message.setText("Olá, " + nomeParticipante + "!\n\n" +
                    "Recebemos sua solicitação para o vínculo de '" + tipoParticipacao +
                    "' no evento: '" + nomeEvento + "'.\n\n" +
                    "Sua solicitação está agora PENDENTE de aprovação pelo administrador.\n" +
                    "Você será notificado por e-mail assim que houver uma atualização.\n\n" +
                    "Obrigado,\nEquipe Infinity Events");

            mailSender.send(message);
            logger.info("E-mail de 'vínculo pendente' enviado com sucesso para {}", to);
        } catch (Exception e) {
            // A falha no envio do e-mail não deve interromper o fluxo principal
            logger.error("Erro ao enviar e-mail de 'vínculo pendente' para {}: {}", to, e.getMessage());
        }
    }

    /**
     * NOVO MÉTODO (REQ 2): E-mail enviado quando o Admin aprova ou rejeita.
     * Informa o status final da solicitação.
     */
    public void sendVinculoStatusUpdateEmail(String to, String nomeParticipante, String nomeEvento, String tipoParticipacao, boolean isAprovado) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);

            String statusStr = isAprovado ? "APROVADA" : "REJEITADA";
            String subject = "Atualização da sua solicitação de vínculo: " + statusStr + " - Infinity Events";
            String body;

            if (isAprovado) {
                body = "Olá, " + nomeParticipante + "!\n\n" +
                        "Boas notícias! Sua solicitação para o vínculo de '" + tipoParticipacao +
                        "' no evento: '" + nomeEvento + "' foi APROVADA.\n\n" +
                        "Você já pode acessar suas funções de " + tipoParticipacao + " no evento.\n\n" +
                        "Obrigado,\nEquipe Infinity Events";
            } else {
                body = "Olá, " + nomeParticipante + "!\n\n" +
                        "Sua solicitação para o vínculo de '" + tipoParticipacao +
                        "' no evento: '" + nomeEvento + "' foi REJEITADA pelo administrador.\n\n" +
                        "Para mais detalhes, entre em contato com a organização do evento.\n\n" +
                        "Obrigado,\nEquipe Infinity Events";
            }

            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            logger.info("E-mail de atualização de status ({}) enviado com sucesso para {}", statusStr, to);
        } catch (Exception e) {
            logger.error("Erro ao enviar e-mail de atualização de status para {}: {}", to, e.getMessage());
        }
    }
}