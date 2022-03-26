package com.ybichel.storage.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.authorization.entity.EmailAccount;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            EmailAccount creds = new ObjectMapper()
                    .readValue(req.getInputStream(), EmailAccount.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth) {
        String token = JWT.create()
                .withSubject(((EmailAccount) auth.getPrincipal()).getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtTokenUtil.JWT_TOKEN_VALIDITY))
                .sign(Algorithm.HMAC512(jwtTokenUtil.getSecret().getBytes()));
        res.addHeader(Constants.AUTHORIZATION_HEADER_KEY, Constants.AUTHORIZATION_HEADER_PREFIX_VALUE + token);
    }
}
