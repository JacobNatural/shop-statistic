package com.app.service;

/**
 * Interface for sending emails. This service provides a method to send an email with a subject and body to a specified recipient.
 */
public interface EmailService {

    /**
     * Sends an email with the given subject and body to the specified recipient.
     *
     * @param email   the recipient's email address
     * @param subject the subject of the email
     * @param body    the body content of the email
     */
    void send(String email, String subject, String body);
}
