package com.app.service.impl;

import com.app.controller.dto.user.*;
import com.app.exception.InvalidCredentialsException;
import com.app.exception.ResourceAlreadyExistException;
import com.app.exception.UserAlreadyActivatedException;
import com.app.persistence.entity.BaseEntity;
import com.app.persistence.repository.UserRepository;
import com.app.persistence.repository.VerificationTokenRepository;
import com.app.security.service.TokenService;
import com.app.service.UserService;
import com.app.validator.Validator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link UserService} interface. This class handles the user management functionality, including user creation,
 * activation, password change, role change, email change, and token management.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final Validator<CreateUserDto> createUserValidator;
    private final Validator<ChangePasswordDto> changePasswordDtoValidator;
    private final Validator<NewPasswordDto> newPasswordDtoValidator;
    private final Validator<EmailDto> emailDtoValidator;
    private final CreateAdminUserDto createAdminUserDto;
    private final TokenService tokenService;

    /**
     * Initializes the service by checking if the default admin user already exists.
     * If it does not exist, the default admin user is created.
     */
    @PostConstruct
    public void init() {
        if (userRepository.findByUsername(createAdminUserDto.username()).isPresent()) {
            throw new ResourceAlreadyExistException("Username already exists");
        }

        userRepository.save(createAdminUserDto.toUserEntity()
                .withPassword(passwordEncoder.encode(createAdminUserDto.password())));
    }

    /**
     * Creates a new user.
     *
     * @param createUserDto the DTO containing the user creation data
     * @return the ID of the created user
     * @throws ResourceAlreadyExistException if the username or email already exists
     */
    @Override
    public Long createUser(CreateUserDto createUserDto) {
        Validator.validate(createUserDto, createUserValidator);

        if (userRepository.findByUsername(createUserDto.username()).isPresent()) {
            throw new ResourceAlreadyExistException("Username already exists");
        }

        if (userRepository.findByEmail(createUserDto.email()).isPresent()) {
            throw new ResourceAlreadyExistException("Email already exists");
        }

        var id = userRepository
                .save(createUserDto.toCreateUserEntity()
                        .withPassword(passwordEncoder.encode(createUserDto.password())))
                .getId();

        eventPublisher.publishEvent(new UserActivationDto(id));

        return id;
    }

    /**
     * Activates a user based on the provided token.
     *
     * @param userTokenActivationDto the DTO containing the token for user activation
     * @return the ID of the activated user
     * @throws EntityNotFoundException if the token is not found or invalid
     */
    @Override
    public Long activateUser(UserTokenActivationDto userTokenActivationDto) {
        var token = verificationTokenRepository
                .findByToken(userTokenActivationDto.token())
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));

        var userEntity = token.validate();

        verificationTokenRepository.delete(token);
        userEntity.ifPresent(entity -> userRepository.save(entity.toActivateUserEntity()));

        return userEntity.map(BaseEntity::getId).orElse(null);
    }

    /**
     * Refreshes the activation token for a user based on their email.
     *
     * @param userEmailRefreshToken the DTO containing the user's email
     * @return the ID of the user whose token is being refreshed
     * @throws EntityNotFoundException       if the user is not found
     * @throws UserAlreadyActivatedException if the user is already activated
     * @throws ResourceAlreadyExistException if a token already exists
     */
    @Override
    public Long refreshToken(UserEmailRefreshToken userEmailRefreshToken) {
        var userEntity = userRepository.findByEmail(userEmailRefreshToken.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userEntity.isActivate()) {
            throw new UserAlreadyActivatedException("User already activated");
        }

        verificationTokenRepository
                .findByUserId(userEntity.getId()).ifPresent(token -> {
                    if (token.validate().isPresent()) {
                        throw new ResourceAlreadyExistException("Token already exists");
                    } else {
                        verificationTokenRepository.delete(token);
                    }
                });

        eventPublisher.publishEvent(new UserActivationDto(userEntity.getId()));
        return userEntity.getId();
    }

    /**
     * Changes the user's password.
     *
     * @param changePasswordDto the DTO containing the new password data
     * @param token             the token used to identify the user
     * @return the ID of the user whose password was changed
     * @throws InvalidCredentialsException if the current password does not match
     * @throws IllegalArgumentException    if the token is null
     * @throws EntityNotFoundException     if the user is not found
     */
    @Override
    public Long changePassword(ChangePasswordDto changePasswordDto, String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }

        Validator.validate(changePasswordDto, changePasswordDtoValidator);

        var id = tokenService.id(token.replace("Bearer ", ""));

        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordDto.currentPassword(), userEntity.getPassword())) {
            throw new InvalidCredentialsException("Passwords do not match");
        }

        return userRepository
                .save(userEntity.withPassword(passwordEncoder.encode(changePasswordDto.newPassword())))
                .getId();
    }

    /**
     * Changes the role of an existing user.
     *
     * @param changeRoleDto the DTO containing the user ID and the new role
     * @return the ID of the user whose role was changed
     * @throws EntityNotFoundException if the user is not found
     */
    @Override
    public Long changeRole(ChangeRoleDto changeRoleDto) {
        var userEntity = userRepository.findById(changeRoleDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userRepository.save(userEntity.toNewRoleEntity(changeRoleDto.newRole()))
                .getId();
    }

    /**
     * Handles the case where a user has lost their password and requests a password reset.
     *
     * @param emailDto the DTO containing the user's email
     * @return the ID of the user who requested a password reset
     * @throws EntityNotFoundException if the email is not found
     */
    @Override
    public Long lostPassword(EmailDto emailDto) {
        var userEntity = userRepository.findByEmail(emailDto.email())
                .orElseThrow(() -> new EntityNotFoundException("Email not found"));

        eventPublisher.publishEvent(new UserActivationDto(userEntity.getId()));

        return userEntity.getId();
    }

    /**
     * Sets a new password for a user based on the provided token.
     *
     * @param newPasswordDto the DTO containing the new password and token
     * @return the ID of the user whose password was set
     * @throws EntityNotFoundException if the token is not found or invalid
     */
    @Override
    public Long newPassword(NewPasswordDto newPasswordDto) {
        Validator.validate(newPasswordDto, newPasswordDtoValidator);

        var token = verificationTokenRepository.findByToken(newPasswordDto.token())
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));

        var userEntity = token.validate();

        verificationTokenRepository.delete(token);

        userEntity.ifPresent(ue -> userRepository
                .save(ue.withPassword(passwordEncoder.encode(newPasswordDto.newPassword()))));

        return userEntity.map(BaseEntity::getId).orElse(null);
    }

    /**
     * Changes the email address of a user.
     *
     * @param emailDto the DTO containing the new email address
     * @param token    the token used to identify the user
     * @return the ID of the user whose email was changed
     * @throws IllegalArgumentException if the token is null
     * @throws EntityNotFoundException  if the user is not found
     */
    @Override
    public Long changeEmail(EmailDto emailDto, String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token cannot be null");
        }

        Validator.validate(emailDto, emailDtoValidator);

        var id = tokenService.id(token.replace("Bearer ", ""));

        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userRepository.save(userEntity.toNewEmailEntity(emailDto.email())).getId();
    }

}
