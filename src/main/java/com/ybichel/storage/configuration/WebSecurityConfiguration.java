package com.ybichel.storage.configuration;

import com.ybichel.storage.common.exception.GlobalFilterExceptionHandler;
import com.ybichel.storage.security.JWTAuthorizationFilter;
import com.ybichel.storage.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.ybichel.storage.security.Constants.LOGIN_URL;
import static com.ybichel.storage.security.Constants.LOGIN_URL_STARS;
import static com.ybichel.storage.security.Constants.REGISTER_URL;
import static com.ybichel.storage.security.Constants.REGISTER_URL_STARS;
import static com.ybichel.storage.security.Constants.REGISTRATION_CONFIRM_URL;
import static com.ybichel.storage.security.Constants.REGISTRATION_CONFIRM_URL_STARS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private GlobalFilterExceptionHandler filterExceptionHandler;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/**").fullyAuthenticated()
                .antMatchers(REGISTER_URL, REGISTER_URL_STARS,
                        LOGIN_URL, LOGIN_URL_STARS,
                        REGISTRATION_CONFIRM_URL, REGISTRATION_CONFIRM_URL_STARS).permitAll()
                .and().exceptionHandling()
                //.and().oauth2Login()
                .and()
                .addFilterBefore(filterExceptionHandler, JWTAuthorizationFilter.class)
                //.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtTokenUtil))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtTokenUtil))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    //@Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration()
                .applyPermitDefaultValues()
        );
        return source;
    }
}
