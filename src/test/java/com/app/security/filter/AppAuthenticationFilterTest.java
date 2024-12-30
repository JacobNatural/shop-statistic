package com.app.security.filter;

import com.app.security.config.AppWebSecurityConfig;
import com.app.security.dto.AuthenticationDto;
import com.app.security.dto.TokensDto;
import com.app.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class    AppAuthenticationFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AppAuthenticationFilter appAuthenticationFilter;

    @Test
    @SneakyThrows
    public void test1() {
        var request = new MockHttpServletRequest();
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var authenticationDto = new AuthenticationDto("username", "password");
        request.setContent(new ObjectMapper().writeValueAsBytes(authenticationDto));

        var response = new MockHttpServletResponse();
        var mockAuth = new UsernamePasswordAuthenticationToken("username", "password",
                Collections.emptyList());

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(mockAuth);

        var result = appAuthenticationFilter.attemptAuthentication(request, response);


        Assertions.assertNotNull(result);
        Assertions.assertEquals("username", result.getPrincipal());
        Assertions.assertEquals("password", result.getCredentials());
        Assertions.assertTrue(result.isAuthenticated());

    }

    @Test
    @SneakyThrows
    public void test2() {

        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        var mockAuth = new UsernamePasswordAuthenticationToken("username", "password",
                Collections.emptyList());

        Mockito.when(tokenService.generateToken(ArgumentMatchers.any()))
                .thenReturn(new TokensDto("access_token", "refresh_token"));

        appAuthenticationFilter.successfulAuthentication(request, response, null, mockAuth);

        Assertions.assertEquals(response.getCookie("AccessToken").getValue(), "access_token");
        Assertions.assertEquals(response.getCookie("AccessToken").getMaxAge(), 86400);
        Assertions.assertTrue(response.getCookie("AccessToken").isHttpOnly());
        Assertions.assertEquals(response.getCookie("RefreshToken").getValue(), "refresh_token");
        Assertions.assertEquals(response.getCookie("RefreshToken").getMaxAge(), 86400);
        Assertions.assertTrue(response.getCookie("RefreshToken").isHttpOnly());
    }


}
