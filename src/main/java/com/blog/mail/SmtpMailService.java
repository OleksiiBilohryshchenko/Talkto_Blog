package com.blog.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SmtpMailService implements MailService {

    private final JavaMailSender mailSender;

    public SmtpMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Password Reset Request");
            helper.setText(buildEmailBody(resetLink), true);

            mailSender.send(message);

        } catch (MessagingException ex) {
            throw new IllegalStateException("Failed to send email", ex);
        }
    }

    private String buildEmailBody(String resetLink) {
        return """
                <p>You requested a password reset.</p>
                <p>Click the link below to reset your password:</p>
                <a href="%s">%s</a>
                """.formatted(resetLink, resetLink);
    }
}