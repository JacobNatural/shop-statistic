package com.app.validator.impl;

import com.app.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link Validator} interface for validating password strings.
 * This class ensures that the password meets the required length and contains at least one character matching
 * each specified regular expression from the configuration.
 */
@Component
public class PasswordValidator implements Validator<String> {

    /**
     * List of regular expressions to check the password against.
     * These are used to ensure the password contains at least one character matching each regex.
     */
    @Value("${validate.user.password.regexs}")
    private List<String> passwordRegexs = new ArrayList<>();

    /**
     * The minimum required length for the password.
     */
    @Value("${validate.user.password.min.length}")
    private int passwordMinLength;

    /**
     * Validates the provided password by ensuring it meets the required length and
     * matches all the configured regular expressions.
     *
     * @param password the password string to validate
     * @return a validation error message if the password is invalid; otherwise, an empty string
     */
    @Override
    public String validate(String password) {
        return passwordCheck(password);
    }

    /**
     * Checks if the password meets the required criteria:
     * - It is not null.
     * - It has a minimum length as defined in the configuration.
     * - It contains at least one character matching each of the regular expressions.
     *
     * @param password the password string to check
     * @return a validation error message if the password is invalid; otherwise, an empty string
     */
    public String passwordCheck(String password) {

        // Check if the password is null or empty
        if (Validator.stringNullCheck(password)) {
            return "Password cannot be null";
        }

        // Check if the password meets the minimum length requirement
        if (Validator.stringMinLength(password, passwordMinLength)) {
            return "Password must be at least " + passwordMinLength + " characters";
        }

        // Check if the password matches each regular expression
        for (var regex : passwordRegexs) {
            var pattern = Pattern.compile(regex);
            var matcher = pattern.matcher(password);
            // If any regex is not matched, return an appropriate error message
            if (!matcher.find()) {
                return "Password should have at least one: " + regex + " character";
            }
        }

        // If all checks pass, return an empty string
        return "";
    }
}
