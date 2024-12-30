package com.app.validator;

/**
 * A generic interface for validating objects of type {@link T}.
 * Implementations of this interface should define the validation logic for the specific type {@link T}.
 *
 * @param <T> the type of object to be validated
 */
public interface Validator<T> {

    /**
     * Validates the given object of type {@link T}.
     * The validation logic should be implemented by classes that implement this interface.
     *
     * @param t the object to validate
     * @return a string containing the validation error message, or an empty string if validation passes
     */
    String validate(T t);

    /**
     * A utility method to validate an object using the provided {@link Validator}.
     * If the validation fails (i.e., the error message is not empty), an {@link IllegalArgumentException} is thrown.
     *
     * @param t the object to validate
     * @param validator the validator to use for validation
     * @param <T> the type of object to be validated
     * @throws IllegalArgumentException if validation fails
     */
    static <T> void validate(T t, Validator<T> validator) {
        var errors = validator.validate(t);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors);
        }
    }

    /**
     * Checks if a given string is null.
     *
     * @param s the string to check
     * @return {@code true} if the string is null, {@code false} otherwise
     */
    static boolean stringNullCheck(String s) {
        return s == null;
    }

    /**
     * Checks if the length of a given string is less than the specified minimum length.
     *
     * @param s the string to check
     * @param minLength the minimum length
     * @return {@code true} if the string's length is less than the minimum length, {@code false} otherwise
     */
    static boolean stringMinLength(String s, int minLength) {
        return s.length() < minLength;
    }

    /**
     * Checks if a given string matches the specified regular expression.
     *
     * @param s the string to check
     * @param regex the regular expression
     * @return {@code true} if the string matches the regular expression, {@code false} otherwise
     */
    static boolean stringRegexCheck(String s, String regex) {
        return s.matches(regex);
    }
}
