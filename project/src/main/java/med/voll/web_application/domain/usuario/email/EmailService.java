package med.voll.web_application.domain.usuario.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import med.voll.web_application.domain.RegraDeNegocioException;
import med.voll.web_application.domain.usuario.Usuario;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender sender;

    private static final String EMAIL_ORIGEM = "vollmed@email.com";
    private static final String NAME_SENDER = "Clínica Voll.Med";

    public static final String URL ="http://localhost:8080";

    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    @Async
    private void sendEmail(String email, String subject, String content) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(EMAIL_ORIGEM, NAME_SENDER);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
        } catch(MessagingException | UnsupportedEncodingException e){
            throw new RegraDeNegocioException("Erro ao enviar email");
        }

        sender.send(message);
    }

    public void sendEmailChangePassword(Usuario usuario) {
        String subject = "Aqui está seu link para alterar a senha";
        String template = templateContentEmail("Olá [[name]],<br>"
                + "Por favor clique no link abaixo para alterar a senha:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">ALTERAR</a></h3>"
                + "Obrigado,<br>"
                + "Clínica Voll Med.", usuario.getNome(), URL + "/recuperar-conta?codigo=" + usuario.getToken());

        sendEmail(usuario.getUsername(), subject, template);
    }

    private String templateContentEmail(String template, String nome, String url) {
        return template.replace("[[name]]", nome).replace("[[URL]]", url);
    }

    public void sendEmailRandomPassword(Usuario usuario, String randomPassword) {
        String subject = "Bem-vindo à Clínica Voll Med | Dados de Acesso ";
        String template = templateEmailRandomPassowordTemplate("Olá [[name]],<br>"
                + "Aqui estão suas informações de login <br>"
                + "<strong>Email:</strong> [[email]] <br>"
                + "<strong>Senha:</strong> [[senha]] <br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">ACESSAR SUA CONTA</a></h3>"
                + "Conte com nossa equipe para o que precisar!<br>"
                + "Obrigado,<br>"
                + "Clínica Voll Med.", usuario.getNome(), usuario.getUsername(), randomPassword);

        sendEmail(usuario.getUsername(), subject, template);
    }

    private String templateEmailRandomPassowordTemplate(String template, String nome, String email, String randomPassword) {
        return template.replace("[[name]]", nome)
                .replace("[[email]]", email)
                .replace("[[senha]]", randomPassword)
                .replace("[[URL]]", URL );
    }

    public void sendEmailActiveAccount(Usuario usuario) {
        String subject = "Bem-vindo à Clínica Voll Med | Ativação de Conta";
            String template = templateContentEmail("Olá [[name]],<br>"
                    + "Por favor clique no link abaixo para Ativar Sua Conta<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">ATIVAR CONTA</a></h3>"
                    + "Conte com nossa equipe para o que precisar!<br>"
                    + "Obrigado,<br>"
                    + "Clínica Voll Med.", usuario.getNome(), URL + "/ativar-conta?token=" + usuario.getToken());

            sendEmail(usuario.getUsername(), subject, template);
    }
}
