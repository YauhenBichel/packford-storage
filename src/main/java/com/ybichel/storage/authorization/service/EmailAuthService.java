package com.ybichel.storage.authorization.service;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.account.service.IAccountService;
import com.ybichel.storage.authorization.entity.ResetPasswordToken;
import com.ybichel.storage.authorization.entity.VerificationToken;
import com.ybichel.storage.authorization.mapper.AuthenticatedAccountMapper;
import com.ybichel.storage.authorization.mapper.AuthMapper;
import com.ybichel.storage.authorization.model.AuthenticatedAccount;
import com.ybichel.storage.authorization.model.VerificationTokenStatus;
import com.ybichel.storage.authorization.vo.ConfirmRegistrationResponseVO;
import com.ybichel.storage.authorization.vo.LoginRequestVO;
import com.ybichel.storage.authorization.vo.RegistrationRequestVO;
import com.ybichel.storage.authorization.vo.EmailResetPasswordRequestVO;
import com.ybichel.storage.authorization.vo.ResetPasswordRequestVO;
import com.ybichel.storage.common.Constants;
import com.ybichel.storage.common.exception.AccountDuplicateException;
import com.ybichel.storage.common.exception.AccountNotActiveException;
import com.ybichel.storage.common.exception.AccountNotFoundException;
import com.ybichel.storage.common.exception.AccountNotVerificatedException;
import com.ybichel.storage.common.exception.ResetPasswordTokenExpiredException;
import com.ybichel.storage.common.exception.ResetPasswordTokenNotFoundException;
import com.ybichel.storage.mail.MailService;
import com.ybichel.storage.security.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailAuthService implements IEmailAuthService {

    private static final Logger logger = LogManager.getLogger(EmailAuthService.class);

    private final IAccountService accountService;
    private final IVerificationTokenService verificationTokenService;
    private final IResetPasswordTokenService resetPasswordTokenService;
    private final AuthenticatedAccountMapper authenticatedAccountMapper;
    private final MailService mailService;
    private final JwtTokenUtil jwtTokenUtil;

    public EmailAuthService(IAccountService accountService,
                            IVerificationTokenService verificationTokenService,
                            IResetPasswordTokenService resetPasswordTokenService,
                            AuthenticatedAccountMapper authenticatedAccountMapper,
                            AuthMapper authMapper,
                            MailService mailService,
                            JwtTokenUtil jwtTokenUtil) {
        this.accountService = accountService;
        this.verificationTokenService = verificationTokenService;
        this.resetPasswordTokenService = resetPasswordTokenService;
        this.authenticatedAccountMapper = authenticatedAccountMapper;
        this.mailService = mailService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = MailException.class)
    public Account register(UUID userId, RegistrationRequestVO registrationRequestVO) {
        final String hashedPassWithSalt = accountService.generateHash(registrationRequestVO.getPassword());

        Optional<Account> optDbAccount = accountService.findActiveAccount(registrationRequestVO.getEmail());

        if (optDbAccount.isPresent()) {
            Account dbAccount = optDbAccount.get();

            if (Boolean.TRUE.equals(dbAccount.getVerificated())) {
                logger.info("The same verificated account id = {} exists.", dbAccount.getId());
                throw new AccountDuplicateException("The same verificated account exists.");
            }

            throw new AccountNotVerificatedException(Constants.ERROR_MESSAGE_ACCOUNT_IS_NOT_VERIFICATED, dbAccount.getId());
        }

        final Account dbAccount = accountService.createAccount(userId, hashedPassWithSalt, registrationRequestVO);
        mailService.confirmRegistration(dbAccount);

        return dbAccount;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<Account> verifyEmail(UUID accountId) {
        Optional<Account> optDbAccount = accountService.findActiveAccount(accountId);
        optDbAccount.ifPresent(mailService::confirmRegistration);

        return optDbAccount;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AuthenticatedAccount login(LoginRequestVO loginRequestVO) {
        Optional<Account> optDbAccount = accountService.findActiveAccountByEmailAndPassword
                (loginRequestVO.getEmail(), loginRequestVO.getPassword());

        if (optDbAccount.isEmpty()) {
            throw new AccountNotFoundException(Constants.ERROR_MESSAGE_ACCOUNT_IS_NOT_FOUND);

        }

        Account dbAccount = optDbAccount.get();

        if (Boolean.FALSE.equals(dbAccount.getVerificated())) {
            throw new AccountNotVerificatedException(Constants.ERROR_MESSAGE_ACCOUNT_IS_NOT_VERIFICATED, dbAccount.getId());
        }

        final String jwtToken = jwtTokenUtil.generateToken(dbAccount);

        if (Boolean.FALSE.equals(dbAccount.getActive())) {
            logger.warn("The account id = {} is not active.", dbAccount.getId());
            throw new AccountNotActiveException(Constants.ERROR_MESSAGE_ACCOUNT_IS_NOT_ACTIVE);
        }

        return authenticatedAccountMapper.toLoginModel(optDbAccount, jwtToken);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ConfirmRegistrationResponseVO confirmRegistration(String token) {
        ConfirmRegistrationResponseVO responseVO = new ConfirmRegistrationResponseVO();

        Optional<VerificationToken> optDbToken = verificationTokenService.findByToken(token);

        if (optDbToken.isEmpty()) {
            logger.info("Bad token. Please register a new account.");
            responseVO.setMessage("Bad token. Please register a new account.");
            responseVO.setTokenStatus(VerificationTokenStatus.BAD_TOKEN);

            return responseVO;
        }

        Account account = optDbToken.get().getAccount();
        Calendar cal = Calendar.getInstance();
        if ((optDbToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            logger.info("Token is expired. Please register a new account. Account id = {}", account.getId());

            responseVO.setMessage("Token is expired. Please register a new account.");
            responseVO.setTokenStatus(VerificationTokenStatus.EXPIRED_TOKEN);

            return responseVO;
        }

        account.setVerificated(true);
        account.getEmailAccount().setVerificated(true);
        accountService.saveAccount(account);

        responseVO.setMessage("Success. Email is confirmed.");
        responseVO.setTokenStatus(VerificationTokenStatus.VALID_TOKEN);

        return responseVO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AuthenticatedAccount getEmailVerificationStatus(UUID accountId) {
        Optional<Account> optDbAccount = accountService.findActiveAccount(accountId);

        if (optDbAccount.isEmpty()) {
            throw new AccountNotFoundException(Constants.ERROR_MESSAGE_ACCOUNT_IS_NOT_FOUND);
        }

        Account dbAccount = optDbAccount.get();
        if (Boolean.FALSE.equals(dbAccount.getActive())) {
            logger.warn("The account id = {} is not active", dbAccount.getId());

            throw new AccountNotActiveException(Constants.ERROR_MESSAGE_ACCOUNT_IS_NOT_ACTIVE);
        }

        final String jwtToken = jwtTokenUtil.generateToken(dbAccount);
        return authenticatedAccountMapper.toLoginModel(optDbAccount, jwtToken);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void sendEmailNotificationToResetPassword(EmailResetPasswordRequestVO requestVO) {
        Optional<Account> optDbAccount = accountService.findActiveAccount(requestVO.getEmail());

        if (optDbAccount.isEmpty()) {
            throw new AccountNotFoundException(Constants.ERROR_MESSAGE_ACCOUNT_IS_NOT_FOUND);
        }

        Account dbAccount = optDbAccount.get();
        if (Boolean.FALSE.equals(dbAccount.getActive())) {
            logger.warn("The account id = {} is not active", dbAccount.getId());
            throw new AccountNotActiveException(Constants.ERROR_MESSAGE_ACCOUNT_IS_NOT_ACTIVE);
        }

        mailService.resetPassword(dbAccount);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Account resetPassword(ResetPasswordRequestVO requestVO) {
        Optional<ResetPasswordToken> optDbToken = resetPasswordTokenService.findByToken(requestVO.getToken().toString());

        if (optDbToken.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException(Constants.ERROR_MESSAGE_BAD_TOKEN);
        }

        Account dbAccount = optDbToken.get().getAccount();

        Calendar cal = Calendar.getInstance();
        if ((optDbToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new ResetPasswordTokenExpiredException(Constants.ERROR_MESSAGE_TOKEN_IS_EXPIRED);
        }

        final String hashedPassWithSalt = accountService.generateHash(requestVO.getPassword());
        dbAccount.setPassword(hashedPassWithSalt);
        accountService.saveAccount(dbAccount);

        return dbAccount;
    }
}
