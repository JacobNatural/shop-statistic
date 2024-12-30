package com.app.security.service.impl;

import com.app.config.AppConfig;
import com.app.persistence.repository.UserRepository;
import com.app.security.dto.RefreshTokenDto;
import com.app.security.dto.TokensDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

import static com.app.data.UserData.USER_ENTITY1;
import static com.app.data.UserData.USER_ENTITY_ACCESS_TOKEN_1;

@SpringBootTest(classes = {TokenServiceImpl.class, AppConfig.class})
public class TokenServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private SecretKey secretKey;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private Authentication authentication;

    @Autowired
    private TokenServiceImpl tokenService;


    @Test
    @DisplayName("When trying to generate a token and the user is not found, throw an IllegalArgumentException.")
    public void test1() {

        Mockito.when(authentication.getName())
                .thenReturn("A");

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                        tokenService.generateToken(authentication))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authentication failed");
    }

    @Test
    @DisplayName("When attempting to generate a token and return a TokensDto with the access and refresh tokens..")
    public void test2() {

        var inOrder = Mockito.inOrder(userRepository, authentication);

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(USER_ENTITY1));

        Mockito.when(authentication.getName()).thenReturn("A");

        TokensDto tokensDto = tokenService.generateToken(authentication);

        Assertions.assertThat(tokensDto.accessToken()).isNotEmpty();
        Assertions.assertThat(tokensDto.refreshToken()).isNotEmpty();

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(ArgumentMatchers.anyString());
        Mockito.verify(authentication, Mockito.times(1)).getName();
    }

    @Test
    @DisplayName("When parsing the access token and the header is incorrect, throw an IllegalArgumentException.")
    public void test3() {
        Assertions.assertThatThrownBy(() -> tokenService.parseAccessToken("token"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authorization header is incorrect");
    }

    @Test
    @DisplayName("When parsing the access token and the token has expired, throw an ExpiredJwtException.")
    public void test4() {
        var token = Jwts
                .builder()
                .subject(1L + "")
                .issuedAt(new Date())
                .expiration(new Date())
                .signWith(secretKey)
                .compact();

        Assertions.assertThatThrownBy(() -> tokenService.parseAccessToken("Bearer " + token))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("When parsing the access token and the token is invalid, throw an IllegalArgumentException.")
    public void test5() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        var currentTime = new Date();
        var token = Jwts
                .builder()
                .subject(1L + "")
                .issuedAt(new Date())
                .expiration(new Date(currentTime.getTime() + 300000))
                .signWith(secretKey)
                .compact();

        Assertions.assertThatThrownBy(() ->
                        tokenService.parseAccessToken("Bearer " + token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authorization failed");
    }

    @Test
    @DisplayName("When parsing the access token, return a UsernamePasswordAuthenticationToken.")
    public void test6() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(USER_ENTITY_ACCESS_TOKEN_1));

        var currentTime = new Date();
        var token = Jwts
                .builder()
                .subject(1L + "")
                .issuedAt(new Date())
                .expiration(new Date(currentTime.getTime() + 300000))
                .signWith(secretKey)
                .compact();

        Assertions.assertThat(tokenService.parseAccessToken("Bearer " + token).getName())
                .isEqualTo("AB");
    }

    @Test
    @DisplayName("When generating a refresh token and the RefreshTokenDto is null, throw an IllegalArgumentException.")
    public void test7() {

        Assertions.assertThatThrownBy( () ->
                tokenService.refreshToken(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Refresh token cannot be null");
    }

    @Test
    @DisplayName("When generating a refresh token and the refresh token is expired, throw an ExpiredJwtException.")
    public void test8() {

        var currentTime = new Date();
        var refreshToken = Jwts
                .builder()
                .subject(1+ "")
                .expiration(new Date(currentTime.getTime()))
                .issuedAt(new Date())
                .claim("access_token_expiration_time_ms_property", currentTime.getTime())
                .signWith(secretKey)
                .compact();

        var refreshTokenDto = new RefreshTokenDto(refreshToken);

        Assertions.assertThatThrownBy( () ->
                        tokenService.refreshToken(refreshTokenDto))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    @DisplayName("When generating a refresh token and the access token is expired, throw an ExpiredJwtException.")
    public void test9() {

        var currentTime = new Date();
        var refreshToken = Jwts
                .builder()
                .subject(1+ "")
                .expiration(new Date(currentTime.getTime() + 30000000))
                .issuedAt(new Date())
                .claim("access_token_expiration_time_ms_property", currentTime.getTime())
                .signWith(secretKey)
                .compact();

        var refreshTokenDto = new RefreshTokenDto(refreshToken);

        Assertions.assertThatThrownBy( () ->
                        tokenService.refreshToken(refreshTokenDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot create a new access token - old access token is expired");
    }

    @Test
    @DisplayName("When generating a refresh token and return a RefreshTokenDto.")
    public void test10() {

        var currentTime = new Date();
        var refreshToken = Jwts
                .builder()
                .subject(1 + "")
                .expiration(new Date(currentTime.getTime() + 30000000))
                .issuedAt(new Date())
                .claim("access_token_expiration_time_ms_property", currentTime.getTime() + 30000)
                .signWith(secretKey)
                .compact();

        var refreshTokenDto = new RefreshTokenDto(refreshToken);

        var tokensDto = tokenService.refreshToken(refreshTokenDto);
        Assertions.assertThat(tokensDto.accessToken()).isNotEmpty();
        Assertions.assertThat(tokensDto.refreshToken()).isNotEmpty();
    }
}
