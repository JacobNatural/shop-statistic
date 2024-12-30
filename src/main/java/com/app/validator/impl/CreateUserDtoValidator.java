package com.app.validator.impl;

import com.app.controller.dto.user.CreateUserDto;
import com.app.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link Validator} interface for validating {@link CreateUserDto} objects.
 * This class ensures that the user creation data follows the required validation rules such as
 * username, name, surname, email format, and password complexity.
 * It checks for null values, minimum lengths, allowed characters using regular expressions,
 * and matching of password and password confirmation fields.
 */
@Component
@RequiredArgsConstructor
public class CreateUserDtoValidator implements Validator<CreateUserDto> {

    /**
     * Regular expression for validating the username.
     */
    @Value("${validate.user.username.regex}")
    private String usernameRegex;

    /**
     * Minimum length required for the username.
     */
    @Value("${validate.user.username.min.length}")
    private int usernameMinLength;

    /**
     * Regular expression for validating the user's name.
     */
    @Value("${validate.user.name.regex}")
    private String nameRegex;

    /**
     * Minimum length required for the user's name.
     */
    @Value("${validate.user.name.min.length}")
    private int nameMinLength;

    /**
     * Regular expression for validating the user's surname.
     */
    @Value("${validate.user.surname.regex}")
    private String surnameRegex;

    /**
     * Minimum length required for the user's surname.
     */
    @Value("${validate.user.surname.min.length}")
    private int surnameMinLength;

    /**
     * Regular expression for validating the email format.
     */
    @Value("${validate.user.email.regex}")
    private String emailRegex;

    /**
     * Validator for the password field to ensure it meets password strength criteria.
     */
    private final Validator<String> passwordValidator;

    /**
     * Validates the provided {@link CreateUserDto} object by ensuring all fields adhere to the specified validation rules.
     * The validation checks for the following:
     * <ul>
     *     <li>The username is not null, has the minimum length, and contains only allowed characters.</li>
     *     <li>The name is not null, has the minimum length, and contains only allowed characters.</li>
     *     <li>The surname is not null, has the minimum length, and contains only allowed characters.</li>
     *     <li>The password and password confirmation match.</li>
     *     <li>The email is in a valid format.</li>
     * </ul>
     * If any validation fails, an appropriate error message is returned.
     *
     * @param createUserDto the {@link CreateUserDto} object to be validated
     * @return a validation error message if any field is invalid; otherwise, an empty string
     * @throws IllegalArgumentException if the password and password confirmation do not match
     */
    @Override
    public String validate(CreateUserDto createUserDto) {

        // Validate the password using the provided password validator
        var passwordValidate = passwordValidator.validate(createUserDto.password());
        if (!passwordValidate.isEmpty()) {
            return passwordValidate;
        }

        // Validate the username
        if (Validator.stringNullCheck(createUserDto.username())) {
            return "Username cannot be null";
        }

        if (Validator.stringMinLength(createUserDto.username(), usernameMinLength)) {
            return "Username must be at least " + usernameMinLength + " characters";
        }

        if (!Validator.stringRegexCheck(createUserDto.username(), usernameRegex)) {
            return "Username contains illegal characters";
        }

        // Validate the name
        if (Validator.stringNullCheck(createUserDto.name())) {
            return "Name cannot be null";
        }

        if (Validator.stringMinLength(createUserDto.name(), nameMinLength)) {
            return "Name must be at least " + nameMinLength + " characters";
        }

        if (!Validator.stringRegexCheck(createUserDto.name(), nameRegex)) {
            return "Name contains illegal characters";
        }

        // Validate the surname
        if (Validator.stringNullCheck(createUserDto.surname())) {
            return "Surname cannot be null";
        }

        if (Validator.stringMinLength(createUserDto.surname(), surnameMinLength)) {
            return "Surname must be at least " + surnameMinLength + " characters";
        }

        if (!Validator.stringRegexCheck(createUserDto.surname(), surnameRegex)) {
            return "Surname contains illegal characters";
        }

        // Check if password and password confirmation match
        if (!createUserDto.password().equals(createUserDto.passwordConfirmation())) {
            throw new IllegalArgumentException("Passwords and password confirmation do not match");
        }

        // Validate the email address
        if (!Validator.stringRegexCheck(createUserDto.email(), emailRegex)) {
            return "Email address is invalid";
        }

        return "";
    }
}
