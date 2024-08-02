package carreiras.com.github.java_spring_boot_library.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import carreiras.com.github.java_spring_boot_library.services.EmailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${application.email.lateloans.remetent}")
    private String remetent;

    @Override
    public void sendEmails(String message, List<String> emailsList) {
        String[] emails = emailsList.toArray(new String[emailsList.size()]);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(remetent);
        mailMessage.setSubject("Livro com empréstimo atrasado");
        mailMessage.setText(message);
        mailMessage.setTo(emails);

        javaMailSender.send(mailMessage);
    }

}
