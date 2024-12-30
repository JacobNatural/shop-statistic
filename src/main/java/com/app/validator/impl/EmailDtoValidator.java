package com.app.validator.impl;

import com.app.controller.dto.user.EmailDto;
import com.app.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link Validator} interface for validating {@link EmailDto} objects.
 * This class ensures that the email provided in the {@link EmailDto} is not null and follows a valid format
 * using a regular expression for email validation.
 */
@Component
public class EmailDtoValidator implements Validator<EmailDto> {

    /**
     * Regular expression for validating the email format.
     */
    @Value("${validate.user.email.regex}")
    private String emailRegex;

    /**
     * Validates the provided {@link EmailDto} object by ensuring the email is not null and matches the
     * required regular expression pattern.
     *
     * @param emailDto the {@link EmailDto} object to be validated
     * @return a validation error message if the email is invalid; otherwise, an empty string
     */
    @Override
    public String validate(EmailDto emailDto) {
        // Check if the email is null or empty
        if (Validator.stringNullCheck(emailDto.email())) {
            return "E-mail cannot be null";
        }

        // Check if the email matches the regex pattern
        if (!Validator.stringRegexCheck(emailDto.email(), emailRegex)) {
            return "E-mail does not match regex";
        }

        return "";
    }
}
