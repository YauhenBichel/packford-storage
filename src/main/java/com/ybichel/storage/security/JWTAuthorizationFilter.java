package com.ybichel.storage.security;

import com.ybichel.storage.security.model.JwtToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.ybichel.storage.security.Constants.AUTHORIZATION_HEADER_KEY;
import static com.ybichel.storage.security.Constants.AUTHORIZATION_HEADER_PREFIX_VALUE;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtTokenUtil jwtTokenUtil;

    public JWTAuthorizationFilter(AuthenticationManager authManager,
                                  JwtTokenUtil jwtTokenUtil) {
        super(authManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(AUTHORIZATION_HEADER_KEY);

        if (header == null || !header.startsWith(AUTHORIZATION_HEADER_PREFIX_VALUE)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER_KEY)
                .replace(AUTHORIZATION_HEADER_PREFIX_VALUE, "");

        JwtToken jwtToken = jwtTokenUtil.getJwtToken(token);

        if (Objects.nonNull(jwtToken)) {
            return new UsernamePasswordAuthenticationToken(
                    jwtToken,
                    null,
                    jwtToken.getAuthorities());
        }

        return null;
    }
}
