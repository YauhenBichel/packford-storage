package com.ybichel.storage.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class MailClient {

    private static final Logger logger = LogManager.getLogger(MailClient.class);

    private final JavaMailSender mailSender;
    private final SimpleMailMessage template;

    @Autowired
    public MailClient(JavaMailSender mailSender, SimpleMailMessage template) {
        this.mailSender = mailSender;
        this.template = template;
    }

    public void prepareAndSend(String to, String subject, String message) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("info.storage@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            String text = String.format(template.getText(), message);
            messageHelper.setText(text, true);
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
