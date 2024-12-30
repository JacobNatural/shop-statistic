package com.app.controller;

import com.app.controller.dto.ResponseDto;
import com.app.controller.dto.user.*;
import com.app.security.dto.RefreshTokenDto;
import com.app.security.dto.TokensDto;
import com.app.security.service.TokenService;
import com.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Controller for handling user-related operations such as registration, activation,
 * login, password reset, email change, role change, and token management.
 * <p>
 * This class provides endpoints for managing users and authentication tasks like
 * creating, activating, and refreshing users, as well as managing passwords and roles.
 * </p>
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    /**
     * Creates a new user account.
     * <p>
     * This endpoint allows the creation of a new user by accepting the user's information.
     * </p>
     *
     * @param user the details of the user to be created.
     * @return a {@link ResponseDto} containing the ID of the created user.
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            description = "Access for all users"
    )
    public ResponseDto<Long> createUser(@RequestBody CreateUserDto user) {
        return new ResponseDto<>(userService.createUser(user));
    }

    /**
     * Activates a user account using a token.
     * <p>
     * This endpoint activates a user by providing the activation token.
     * </p>
     *
     * @param userTokenActivationDto the activation token details for the user.
     * @return a {@link ResponseDto} containing the ID of the activated user.
     */
    @PostMapping("/login/activate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access for all users"
    )
    public ResponseDto<Long> activateUser(@RequestBody UserTokenActivationDto userTokenActivationDto) {
        return new ResponseDto<>(userService.activateUser(userTokenActivationDto));
    }

    /**
     * Refreshes the activation token for the user via email.
     * <p>
     * This endpoint sends a new activation token to the user's email for account activation.
     * </p>
     *
     * @param userTokenActivationDto the email details for refreshing the token.
     * @return a {@link ResponseDto} containing the ID of the user whose token is refreshed.
     */
    @PostMapping("/login/mail/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access for all users"
    )
    public ResponseDto<Long> refreshActivate(@RequestBody UserEmailRefreshToken userTokenActivationDto) {
        return new ResponseDto<>(userService.refreshToken(userTokenActivationDto));
    }

    /**
     * Refreshes the user's access token.
     * <p>
     * This endpoint refreshes the user's session by accepting the refresh token and issuing
     * a new pair of access and refresh tokens.
     * </p>
     *
     * @param refreshTokenDto the details of the refresh token.
     * @return a {@link ResponseDto} containing the new {@link TokensDto} (access and refresh tokens).
     */
    @PostMapping("/login/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access for all users"
    )
    public ResponseDto<TokensDto> refreshUser(@RequestBody RefreshTokenDto refreshTokenDto) {
        return new ResponseDto<>(tokenService.refreshToken(refreshTokenDto));
    }

    /**
     * Initiates the password reset process by sending a reset link to the provided email.
     * <p>
     * This endpoint allows the user to request a password reset by providing their email.
     * </p>
     *
     * @param emailDto      the email address of the user who lost their password.
     * @param bindingResult holds validation errors, if any.
     * @return a {@link ResponseDto} containing the ID of the user requesting password reset.
     * @throws IllegalArgumentException if validation errors are found.
     */
    @PostMapping("/login/lost")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access for all users"
    )
    public ResponseDto<Long> lostPassword(@Valid @RequestBody EmailDto emailDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining("\n")));
        }
        return new ResponseDto<>(userService.lostPassword(emailDto));
    }

    /**
     * Allows the user to set a new password.
     * <p>
     * This endpoint allows the user to reset their password using the new password provided.
     * </p>
     *
     * @param newPasswordDto the new password details.
     * @return a {@link ResponseDto} containing the ID of the user whose password has been changed.
     */
    @PatchMapping("/login/password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access for all users"
    )
    public ResponseDto<Long> newPassword(@RequestBody NewPasswordDto newPasswordDto) {
        return new ResponseDto<>(userService.newPassword(newPasswordDto));
    }

    /**
     * Changes the user's email address.
     * <p>
     * This endpoint allows the user to change their email address after providing the new
     * email and a valid authorization token.
     * </p>
     *
     * @param emailDto the new email details.
     * @param token    the authorization token for the user.
     * @return a {@link ResponseDto} containing the ID of the user whose email was changed.
     */
    @PatchMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<Long> changeEmail(@RequestBody EmailDto emailDto,
                                         @RequestHeader(name = "Authorization") String token) {
        return new ResponseDto<>(userService.changeEmail(emailDto, token));
    }

    /**
     * Changes the user's password.
     * <p>
     * This endpoint allows the user to change their password after providing the current and
     * new password along with a valid authorization token.
     * </p>
     *
     * @param changePasswordDto the details of the current and new password.
     * @param token             the authorization token for the user.
     * @return a {@link ResponseDto} containing the ID of the user whose password was changed.
     */
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            description = "Access only for ADMIN, LEADER and WORKER using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseDto<Long> changePassword(
            @RequestBody ChangePasswordDto changePasswordDto,
            @RequestHeader(name = "Authorization") String token) {
        return new ResponseDto<>(userService.changePassword(changePasswordDto, token));
    }

    /**
     * Changes the user's role.
     * <p>
     * This endpoint allows an administrator to change the role of a user.
     * </p>
     *
     * @param changeRoleDto the details of the role change request.
     * @return a {@link ResponseDto} containing the ID of the user whose role was changed.
     */
    @PatchMapping("/role")
    @Operation(
            description = "Access only for ADMIN using JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Long> changeRole(
            @RequestBody ChangeRoleDto changeRoleDto) {
        return new ResponseDto<>(userService.changeRole(changeRoleDto));
    }
}
