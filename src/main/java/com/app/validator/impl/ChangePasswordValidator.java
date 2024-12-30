package com.app.validator.impl;

import com.app.controller.dto.user.ChangePasswordDto;
import com.app.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link Validator} interface for validating the {@link ChangePasswordDto}.
 * This class is responsible for checking the validity of a password change request, including the current password,
 * new password, and password confirmation fields.
 * It ensures that the current password is provided, the new password follows the necessary criteria,
 * and the new password and its confirmation match.
 */
@Component
@RequiredArgsConstructor
public class ChangePasswordValidator implements Validator<ChangePasswordDto> {

    /**
     * A validator for checking the validity of the new password itself.
     */
    private final Validator<String> passwordValidator;

    /**
     * Validates the provided {@link ChangePasswordDto}. This method checks if:
     * <ul>
     *     <li>The current password is provided.</li>
     *     <li>The new password confirmation is provided.</li>
     *     <li>The new password meets the validation criteria.</li>
     *     <li>The new password and the confirmation match.</li>
     * </ul>
     * If any of these checks fail, an appropriate error message is returned.
     *
     * @param changePasswordDto the data transfer object containing the current password, new password, and password confirmation
     * @return an empty string if the validation passes; an error message if validation fails
     */
    @Override
    public String validate(ChangePasswordDto changePasswordDto) {

        // Check if the current password is provided
        if (Validator.stringNullCheck(changePasswordDto.currentPassword())) {
            return "Current password required";
        }

        // Check if the new password confirmation is provided
        if (Validator.stringNullCheck(changePasswordDto.newPasswordConfirmation())) {
            return "New password confirmation required";
        }

        // Validate the new password using the passwordValidator
        var passwordValidate = passwordValidator.validate(changePasswordDto.newPassword());
        if (!passwordValidate.isEmpty()) {
            return passwordValidate;
        }

        // Check if the new password and confirmation match
        if (!changePasswordDto.newPassword().equals(changePasswordDto.newPasswordConfirmation())) {
            return "Passwords confirmation do not match";
        }

        return "";
    }
}
