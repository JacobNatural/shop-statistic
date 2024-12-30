package com.app.security.filter;

import com.app.security.dto.AuthenticationDto;
import com.app.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Custom filter to handle user authentication during login.
 * <p>
 * This filter processes the login request, extracts user credentials (username and password),
 * authenticates the user using the provided {@link AuthenticationManager}, and generates
 * authentication tokens upon successful login. It also sets the generated tokens in cookies
 * and returns them in the response body.
 * </p>
 */
public class AppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs a new {@link AppAuthenticationFilter} with the specified {@link TokenService}
     * and {@link AuthenticationManager}.
     *
     * @param tokenService          the service responsible for generating tokens
     * @param authenticationManager the authentication manager used to authenticate the user
     */
    public AppAuthenticationFilter(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Attempts to authenticate the user based on the provided credentials.
     * <p>
     * The method reads the {@link AuthenticationDto} from the request body, which contains
     * the username and password. It then creates a {@link UsernamePasswordAuthenticationToken}
     * and delegates the authentication to the {@link AuthenticationManager}.
     * </p>
     *
     * @param request  the HTTP request containing the user's credentials
     * @param response the HTTP response
     * @return the authenticated {@link Authentication} object
     * @throws AuthenticationException if authentication fails
     */
    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        var jsonBody = new ObjectMapper()
                .readValue(request.getInputStream(), AuthenticationDto.class);

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                jsonBody.username(),
                jsonBody.password(),
                Collections.emptyList()
        ));
    }

    /**
     * Handles successful authentication by generating and setting authentication tokens.
     * <p>
     * Upon successful authentication, this method generates the access and refresh tokens
     * using the {@link TokenService}. The tokens are then added as HTTP-only cookies and
     * returned in the response body in JSON format.
     * </p>
     *
     * @param request    the HTTP request
     * @param response   the HTTP response
     * @param chain      the filter chain
     * @param authResult the authentication result
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        var tokens = tokenService.generateToken(authResult);

        var accesTokenCookie = new Cookie("AccessToken", tokens.accessToken());
        accesTokenCookie.setHttpOnly(true);
        accesTokenCookie.setMaxAge(86400); // 1 day
        var refreshTokenCookie = new Cookie("RefreshToken", tokens.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(86400); // 1 day

        response.addCookie(accesTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
