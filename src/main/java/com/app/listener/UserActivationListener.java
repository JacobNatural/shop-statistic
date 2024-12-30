package com.app.listener;

import com.app.controller.dto.user.UserActivationDto;
import com.app.persistence.entity.VerificationTokenEntity;
import com.app.persistence.repository.UserRepository;
import com.app.persistence.repository.VerificationTokenRepository;
import com.app.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

/**
 * Listener class responsible for handling user activation events.
 * <p>
 * This class listens for user activation events and triggers the sending of an activation email to the user.
 * The listener generates a unique verification token, saves it, and sends the email to the user with the token.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class UserActivationListener {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    /**
     * The expiration time for the activation email in nanoseconds.
     * <p>
     * This value is read from the application's properties configuration file and determines how long the
     * activation link remains valid after it is sent to the user.
     * </p>
     */
    @Value("${activation-mail-expiration-time}")
    private Long activationMailExpireTime;

    /**
     * Listens to the user activation event and sends an activation email with a unique token to the user.
     * <p>
     * This method is executed asynchronously and ensures the activation email is only sent after the transaction
     * has been successfully committed. It generates a verification token, stores it in the database, and sends an
     * email with the token to the user.
     * </p>
     *
     * @param userActivationDto the data transfer object containing the user information for activation
     * @throws IllegalArgumentException if the user is not found in the repository
     */
    @Async
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendActivationEmail(UserActivationDto userActivationDto) {
        // Fetch the user to activate based on user ID
        var userToActivation = userRepository
                .findById(userActivationDto.id())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Generate a unique token for user activation
        var token = UUID.randomUUID().toString().replaceAll("\\W", "");

        // Set the expiration timestamp for the token
        var timeStamps = System.nanoTime() + activationMailExpireTime;

        // Create a verification token entity and save it to the repository
        var verificationTokenEntity = VerificationTokenEntity
                .builder()
                .token(token)
                .timeStamp(timeStamps)
                .user(userToActivation)
                .build();

        verificationTokenRepository.save(verificationTokenEntity);

        // Send the activation email with the token to the user
        emailService.send(
                userToActivation.getEmail(),
                "Activation link",
                "Use this code to activate your account: " + token
        );
    }
}
