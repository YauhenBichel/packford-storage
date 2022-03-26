package com.ybichel.storage.mail;

import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.authorization.service.IResetPasswordTokenService;
import com.ybichel.storage.authorization.service.IVerificationTokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MailService implements IMailService {

    private static final Logger logger = LogManager.getLogger(MailService.class);

    private final MailClient mailClient;
    private final IVerificationTokenService verificationTokenService;
    private final IResetPasswordTokenService resetPasswordTokenService;

    public MailService(MailClient mailClient,
                       IVerificationTokenService tokenService,
                       IResetPasswordTokenService resetPasswordTokenService) {
        this.mailClient = mailClient;
        this.verificationTokenService = tokenService;
        this.resetPasswordTokenService = resetPasswordTokenService;
    }

    public void confirmRegistration(EmailAccount emailAccount) {

        logger.info("confirmRegistration email = {}", emailAccount.getEmail());

        final String appUrl = "localhost";

        UUID id = UUID.randomUUID();
        String token = UUID.randomUUID().toString();
        verificationTokenService.createVerificationToken(id, token, emailAccount);

        final String recipientAddress = emailAccount.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = appUrl + "/email-confirmation?token=" + token;
        final String greetingMessage = "Please confirm your email address.";
        final String linkLabel = "Confirm Email";

        final String greeting = Constants.getGreetingTemplate(/*account.getFirstName()*/"test", greetingMessage);
        final String button = Constants.getButtonLink(confirmationUrl, linkLabel);
        final String message = generateEmailBody(Constants.IMAGE_ROW_TEMPLATE, greeting, button, recipientAddress);

        mailClient.prepareAndSend(recipientAddress, subject, message);
    }

    public void resetPassword(EmailAccount emailAccount) {

        logger.info("resetPassword email = {}", emailAccount.getEmail());

        UUID id = UUID.randomUUID();
        String token = UUID.randomUUID().toString();
        resetPasswordTokenService.createVerificationToken(id, token, emailAccount);

        String recipientAddress = emailAccount.getEmail();
        String subject = "Reset Password";
        String greetingMessage = "Please reset your password";

        final String greeting = Constants.getGreetingTemplate(/*account.getFirstName()*/"test", greetingMessage);
        final String button = Constants.getButtonLink("localhost/reset-password?token=" + token, "Reset Password");
        final String message = generateEmailBody(Constants.IMAGE_ROW_TEMPLATE, greeting, button, recipientAddress);

        mailClient.prepareAndSend(recipientAddress, subject, message);
    }

    private String generateEmailBody(String logoRow, String greeting, String button, String userEmail) {
        return "<html>" +
                    "<body>" +
                        "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">" +
                            logoRow +
                            greeting +
                            button +
                        "</table>" +
                        "<br/>" +
                        "<hr>" +
                        Constants.EMAIL_SIGNATURE +
                    "</body>" +
                    Constants.getEmailFooter(userEmail) +
                "</html>";
    }
}
