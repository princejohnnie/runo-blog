package dev.levelupschool.backend.mail;

import dev.levelupschool.backend.model.User;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendMail(String to, String subject, String body) throws MessagingException;
}
