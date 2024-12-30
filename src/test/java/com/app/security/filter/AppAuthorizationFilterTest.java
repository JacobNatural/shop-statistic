package com.app.security.filter;

import com.app.security.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AppAuthorizationFilterTest {
    @Mock
    private TokenService tokenService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private AuthenticationManager authenticationManager;

//    @Mock
//    private Authentication authentication;

    @InjectMocks
    private AppAuthorizationFilter appAuthorizationFilter;

    @Test
    @DisplayName("When the header is not existing")
    @SneakyThrows
    public void test1(){

        Mockito.when(request.getHeader("Authorization"))
                        .thenReturn(null);

        appAuthorizationFilter.doFilterInternal(request, response, filterChain);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Mockito.verify(tokenService, Mockito.never()).parseAccessToken(ArgumentMatchers.anyString());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
        assert securityContext.getAuthentication() == null;
    }

    @Test
    @DisplayName("When the header is not existing")
    @SneakyThrows
    public void test2(){

        var token = "Bearer token";
        var userPassword = new UsernamePasswordAuthenticationToken(
                "user", "password", Collections.emptyList());
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn(token);

        Mockito.when(tokenService.parseAccessToken(token))
                        .thenReturn(userPassword);

        appAuthorizationFilter.doFilterInternal(request, response, filterChain);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Mockito.verify(tokenService, Mockito.times(1))
                .parseAccessToken(ArgumentMatchers.anyString());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);

        Assertions.assertTrue( securityContext.getAuthentication().isAuthenticated());
    }
}
