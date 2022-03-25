package com.ybichel.storage.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.security.model.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil implements Serializable {
    public final long JWT_TOKEN_VALIDITY = 900_000; // 15 mins

    @Value("${jwt.secret}")
    private String secret;

    public JwtToken getJwtToken(String token) {

        Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);

        final String email = claimsJws.getBody().get(Constants.JWT_CLAIMS_KEY_EMAIL).toString();
        final String accountId = claimsJws.getBody().get(Constants.JWT_CLAIMS_KEY_ACCOUNT_ID).toString();
        final String serializedRoles = claimsJws.getBody().get(Constants.JWT_CLAIMS_KEY_ROLE).toString();

        JwtToken jwtToken = new JwtToken();
        jwtToken.setAccountId(UUID.fromString(accountId));
        jwtToken.setEmail(email);

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> roles = null;
        try {
            roles = objectMapper.readValue(serializedRoles, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        jwtToken.setAuthorities(authorities);

        return jwtToken;
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Account account) {

        List<String> roles = account.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        String serializedRoles = null;
        try {
            serializedRoles = objectMapper.writeValueAsString(roles);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Map<String, Object> claims = new HashMap<>();

        String email;
        if(account.getEmailAccount() != null) {
            email = account.getEmailAccount().getEmail();
        } else {
            email = account.getEmail();
        }

        claims.put(Constants.JWT_CLAIMS_KEY_ACCOUNT_ID, account.getId());
        claims.put(Constants.JWT_CLAIMS_KEY_EMAIL, email);
        claims.put(Constants.JWT_CLAIMS_KEY_ROLE, serializedRoles);

        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("alg", Constants.JWT_HEADER_ALG_VALUE)
                .setHeaderParam("typ", Constants.JWT_HEADER_TYP_VALUE)
                .setSubject(Constants.JWT_STORAGE_SUBJECT)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .setId(account.getId().toString())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, Account dbAccount) {
        //final Account account = getSubjectFromToken(token);
        return false; //(account.equals(dbAccount) && !isTokenExpired(token));
    }

    public String getSecret() {
        return this.secret;
    }
}
