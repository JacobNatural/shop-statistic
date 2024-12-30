package com.app.security.config;

import com.app.security.dto.AuthenticationErrorDto;
import com.app.security.filter.AppAuthenticationFilter;
import com.app.security.filter.AppAuthorizationFilter;
import com.app.security.service.TokenService;
import com.app.security.service.impl.AppUserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration for the web application.
 * <p>
 * This class configures the web security settings for the application. It includes CORS settings, authentication and authorization filters,
 * exception handling, and session management. The class ensures that the application is stateless, uses JWT for authentication,
 * and provides different access levels based on user roles.
 * </p>
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class AppWebSecurityConfig {

    private final AppUserDetailsServiceImpl appUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    /**
     * Configures the HTTP security settings for the web application.
     * <p>
     * This method sets up the authentication manager, CORS configuration, CSRF settings, exception handling,
     * session management, and HTTP request authorization rules. It also adds custom authentication and authorization filters
     * to handle token-based authentication and authorization.
     * </p>
     *
     * @param http the {@link HttpSecurity} object used to configure the HTTP security
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if there is an error during the configuration
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        var authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(appUserDetailsService)
                .passwordEncoder(passwordEncoder);

        var authenticationManager = authenticationManagerBuilder.build();

        http
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint());
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
                })
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
                                .permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/users/login/password")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/login/**", "/login")
                                .permitAll()
                                .requestMatchers("/orders/**", "/shop/**", "/users/password").hasAnyRole("WORKER", "LEADER", "ADMIN")
                                .requestMatchers("/clients/**", "/products/**").hasAnyRole("LEADER", "ADMIN")
                                .requestMatchers("/**").hasAnyRole("ADMIN")
                                .anyRequest()
                                .authenticated())
                .addFilter(new AppAuthenticationFilter(tokenService, authenticationManager))
                .addFilter(new AppAuthorizationFilter(authenticationManager, tokenService))
                .authenticationManager(authenticationManager);

        return http.build();
    }

    /**
     * Configures the authentication entry point for the application.
     * <p>
     * This method handles unauthorized access by returning a custom error response with a 403 status code.
     * </p>
     *
     * @return the configured {@link AuthenticationEntryPoint}
     */
    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            var ex = new AuthenticationErrorDto(authException.getMessage());
            response.setStatus(403);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(ex));
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    /**
     * Configures the access denied handler for the application.
     * <p>
     * This method handles access denied scenarios by returning a custom error response with a 403 status code.
     * </p>
     *
     * @return the configured {@link AccessDeniedHandler}
     */
    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            var ex = new AuthenticationErrorDto(accessDeniedException.getMessage());
            response.setStatus(403);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(new ObjectMapper().writeValueAsString(ex));
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    /**
     * Configures the CORS settings for the application.
     * <p>
     * This method allows cross-origin requests from the specified origins, enables credentials, and defines
     * the allowed headers and HTTP methods for the application.
     * </p>
     *
     * @return the configured {@link CorsConfigurationSource}
     */
    private CorsConfigurationSource corsConfigurationSource() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.CACHE_CONTROL,
                "X-Requested-With"
        ));
        corsConfiguration.setAllowedMethods(
                List.of(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name())
        );

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
