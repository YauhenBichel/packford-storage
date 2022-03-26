package com.ybichel.storage.authorization.controller;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.authorization.mapper.AuthenticatedAccountMapper;
import com.ybichel.storage.authorization.mapper.AuthMapper;
import com.ybichel.storage.authorization.model.AuthenticatedAccount;
import com.ybichel.storage.authorization.service.IEmailAuthService;
import com.ybichel.storage.authorization.vo.ConfirmRegistrationResponseVO;
import com.ybichel.storage.authorization.vo.EmailVerificationStatusResponseVO;
import com.ybichel.storage.authorization.vo.LoginRequestVO;
import com.ybichel.storage.authorization.vo.LoginResponseVO;
import com.ybichel.storage.authorization.vo.RegistrationRequestVO;
import com.ybichel.storage.authorization.vo.RegistrationResponseVO;
import com.ybichel.storage.authorization.vo.EmailResetPasswordRequestVO;
import com.ybichel.storage.authorization.vo.EmailResetPasswordResponseVO;
import com.ybichel.storage.authorization.vo.ResetPasswordRequestVO;
import com.ybichel.storage.authorization.vo.ResetPasswordResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping({"/auth"})
public class EmailAuthController {

    private final IEmailAuthService emailAuthService;
    private final AuthMapper authMapper;
    private final AuthenticatedAccountMapper authenticatedAccountMapper;

    public EmailAuthController(IEmailAuthService emailAuthService,
                               AuthMapper authMapper,
                               AuthenticatedAccountMapper authenticatedAccountMapper) {
        this.emailAuthService = emailAuthService;
        this.authMapper = authMapper;
        this.authenticatedAccountMapper = authenticatedAccountMapper;
    }

    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public ResponseEntity<RegistrationResponseVO> register(@Valid @RequestBody RegistrationRequestVO request) {
        final UUID accountId = UUID.randomUUID();
        final Account dbAccount = emailAuthService.register(accountId, request);

        RegistrationResponseVO responseVO = authMapper.toRegistrationResponseVO(dbAccount);

        return new ResponseEntity<>(responseVO, HttpStatus.CREATED);
    }

    /**
     * Sends email message with a link to confirm a user email address
     * @param id
     * @return
     */
    @GetMapping("/verify-email/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity verifyEmail(@PathVariable("id") final UUID id) {
        final Optional<Account> optDbAccount = emailAuthService.verifyEmail(id);

        if (optDbAccount.isEmpty()) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(null, HttpStatus.OK);
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<LoginResponseVO> login(@Valid @RequestBody LoginRequestVO loginRequestVO) {

        final AuthenticatedAccount authenticatedAccount = emailAuthService.login(loginRequestVO);

        LoginResponseVO responseVO = authenticatedAccountMapper.toLoginResponseVO(authenticatedAccount);
        return new ResponseEntity<>(responseVO, HttpStatus.OK);
    }

    @GetMapping("/registration-confirm")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ConfirmRegistrationResponseVO> confirmRegistration(@Valid @RequestParam("token") String token) {
        ConfirmRegistrationResponseVO responseVO = emailAuthService.confirmRegistration(token);
        return new ResponseEntity<>(responseVO, HttpStatus.OK);
    }

    @GetMapping("/verification-status/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<EmailVerificationStatusResponseVO> getVerificationStatus(@PathVariable("id") final UUID id) {
        AuthenticatedAccount authenticatedAccount = emailAuthService.getEmailVerificationStatus(id);

        EmailVerificationStatusResponseVO responseVO = new EmailVerificationStatusResponseVO();
        responseVO.setAccountId(id);
        responseVO.setVerificated(authenticatedAccount.getVerificated());
        responseVO.setJwtToken(authenticatedAccount.getJwtToken());

        return new ResponseEntity<>(responseVO, HttpStatus.OK);
    }

    @PostMapping("/reset-password-email")
    @CrossOrigin(origins = "*")
    public ResponseEntity<EmailResetPasswordResponseVO> resetPasswordEmail(@Valid @RequestBody EmailResetPasswordRequestVO requestVO) {
        emailAuthService.sendEmailNotificationToResetPassword(requestVO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ResetPasswordResponseVO> resetPassword(@Valid @RequestBody ResetPasswordRequestVO requestVO) {
        final EmailAccount updatedEmailAccount = emailAuthService.resetPassword(requestVO);

        ResetPasswordResponseVO responseVO = new ResetPasswordResponseVO();
        responseVO.setId(updatedEmailAccount.getAccount().getId());
        responseVO.setActive(updatedEmailAccount.getAccount().getActive());
        responseVO.setEmail(updatedEmailAccount.getEmail());
        responseVO.setVerificated(updatedEmailAccount.getVerificated());

        return new ResponseEntity<>(responseVO, HttpStatus.OK);
    }
}
