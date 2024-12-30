package com.app.config;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

/**
 * Configuration class for application-level beans.
 * Provides beans for password encoding and JWT secret key generation.
 */
@Configuration
public class AppConfig {

    /**
     * Creates a {@link PasswordEncoder} bean.
     * <p>
     * This method provides a password encoder that supports multiple encoding algorithms.
     * The encoder will automatically delegate to the appropriate encoding algorithm based on the prefix of the stored password.
     * </p>
     *
     * @return a delegating {@link PasswordEncoder}.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Creates a {@link SecretKey} bean for signing JSON Web Tokens (JWTs).
     * <p>
     * The key is generated using the HS512 signing algorithm provided by the {@link io.jsonwebtoken.Jwts} library.
     * This key is used to ensure the integrity and authenticity of JWTs.
     * </p>
     *
     * @return a {@link SecretKey} for JWT signing.
     */
    @Bean
    SecretKey secretKey() {
        return Jwts.SIG.HS512.key().build();
    }
}
