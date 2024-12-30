package com.app.validator.impl;

import com.app.controller.dto.ClientDto;
import com.app.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Implementation of the {@link Validator} interface for validating {@link ClientDto} objects.
 * This class is responsible for checking the validity of a client’s details, including name, surname, age, and available cash.
 * It ensures that the name and surname are provided, have a minimum length, and contain only valid characters.
 * Additionally, it validates that the client is old enough and has sufficient cash.
 */
@Component
@RequiredArgsConstructor
public class ClientDtoValidator implements Validator<ClientDto> {

    /**
     * Regular expression for validating the client's name.
     */
    @Value("${validate.client.name.regex}")
    private String nameRegex;

    /**
     * Minimum length required for the client's name.
     */
    @Value("${validate.client.name.min.length}")
    private int minNameLength;

    /**
     * Regular expression for validating the client's surname.
     */
    @Value("${validate.client.surname.regex}")
    private String surnameRegex;

    /**
     * Minimum length required for the client's surname.
     */
    @Value("${validate.client.surname.min.length}")
    private int minSurnameLength;

    /**
     * Minimum age required for a valid client.
     */
    @Value("${validate.client.min.age}")
    private int minAge;

    /**
     * Minimum amount of cash required for a valid client.
     */
    @Value("${validate.client.min.cash}")
    private BigDecimal minCash;

    /**
     * Validates the given {@link ClientDto} object by checking the following:
     * <ul>
     *     <li>The name is provided and has the correct length and valid characters.</li>
     *     <li>The surname is provided and has the correct length and valid characters.</li>
     *     <li>The client's age is greater than or equal to the minimum allowed age.</li>
     *     <li>The client has sufficient cash (greater than the minimum required amount).</li>
     * </ul>
     * If any of the validations fail, an appropriate error message is returned.
     *
     * @param client the {@link ClientDto} object to be validated
     * @return an error message if validation fails; an empty string if validation passes
     */
    @Override
    public String validate(ClientDto client) {

        // Check if the client's name is provided
        if (Validator.stringNullCheck(client.name())) {
            return "Client name cannot be null";
        }

        // Check if the client's name meets the minimum length requirement
        if (Validator.stringMinLength(client.name(), minNameLength)) {
            return "Client name must have at least " + minNameLength + " characters";
        }

        // Check if the client's name matches the regular expression
        if (!Validator.stringRegexCheck(client.name(), nameRegex)) {
            return "Client name contains invalid characters";
        }

        // Check if the client's surname is provided
        if (Validator.stringNullCheck(client.surname())) {
            return "Client surname cannot be null";
        }

        // Check if the client's surname meets the minimum length requirement
        if (Validator.stringMinLength(client.surname(), minSurnameLength)) {
            return "Client surname must have at least " + minSurnameLength + " characters";
        }

        // Check if the client's surname matches the regular expression
        if (!Validator.stringRegexCheck(client.surname(), surnameRegex)) {
            return "Client surname contains invalid characters";
        }

        // Check if the client's age is greater than or equal to the minimum allowed age
        if (client.age() < minAge) {
            return "Client is too young, min age is: " + minAge;
        }

        // Check if the client's cash is greater than the minimum required amount
        if (client.cash().compareTo(minCash) <= 0) {
            return "Cash is too low";
        }

        return "";
    }
}
