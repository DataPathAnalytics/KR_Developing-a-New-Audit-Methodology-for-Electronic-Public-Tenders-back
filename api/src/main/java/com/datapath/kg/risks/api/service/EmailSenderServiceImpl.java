package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.exception.ResetPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final String TOOL_NAME = "KG Audit Tool";
    private static final String RECOVERY_SUBJECT = "Reset password";

    @Value("${password.reset.redirectUrl}")
    private String recoveryRedirectUrl;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String FROM;

    @Override
    public void send(String email, String token) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.displayName());
        try {
            message.setSubject(RECOVERY_SUBJECT);
            message.setTo(email);
            message.setFrom(FROM, TOOL_NAME);
            String redirectUrl = String.format(recoveryRedirectUrl, token);
            message.setText(redirectUrl, false);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new ResetPasswordException("Email for confirm user not send.", "RP1");
        }
    }
}



