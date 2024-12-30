package com.app.validator.impl;

import com.app.controller.dto.user.NewPasswordDto;
import com.app.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link Validator} interface for validating {@link NewPasswordDto} objects.
 * This class ensures that the new password and its confirmation are valid, and that the token provided is not null.
 */
@Component
@RequiredArgsConstructor
public class NewPasswordValidator implements Validator<NewPasswordDto> {

    /**
     * Validator used to validate the new password format.
     */
    private final Validator<String> passwordValidator;

    /**
     * Validates the provided {@link NewPasswordDto} object by ensuring that the token is not null,
     * the new password meets the required format, and the new password matches its confirmation.
     *
     * @param newPasswordDto the {@link NewPasswordDto} object to be validated
     * @return a validation error message if any validation fails; otherwise, an empty string
     */
    @Override
    public String validate(NewPasswordDto newPasswordDto) {

        // Check if the token is null or empty
        if (Validator.stringNullCheck(newPasswordDto.token())) {
            return "Token cannot be null";
        }

        // Check if the new password confirmation is provided
        if (Validator.stringNullCheck(newPasswordDto.confirmPassword())) {
            return "New password confirmation required";
        }

        // Validate the new password using the passwordValidator
        var passwordValidate = passwordValidator.validate(newPasswordDto.newPassword());

        // If password validation fails, return the validation message
        if (!passwordValidate.isEmpty()) {
            return passwordValidate;
        }

        // Ensure that the new password matches the confirmation
        if (!newPasswordDto.newPassword().equals(newPasswordDto.confirmPassword())) {
            return "Passwords confirmation do not match";
        }

        // If all validations pass, return an empty string
        return "";
    }
}
