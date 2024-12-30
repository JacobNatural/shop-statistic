package com.app.security.service.impl;

import com.app.persistence.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link UserDetailsService} interface for loading user details.
 * <p>
 * This service is used to retrieve user details from the database by username and return a Spring Security
 * {@link User} object containing the user's credentials and authorities. The service is responsible for
 * converting the retrieved user entity into a format suitable for Spring Security's authentication mechanism.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user details by the username.
     * <p>
     * This method retrieves a user by username from the database using the {@link UserRepository},
     * then converts the user entity into a Spring Security {@link User} object with the required
     * information like username, password, enabled status, and roles.
     * If the user is not found, it throws an {@link EntityNotFoundException}.
     * </p>
     *
     * @param username the username of the user to be loaded
     * @return the {@link UserDetails} object containing the user's credentials and authorities
     * @throws UsernameNotFoundException if the user with the given username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .map(userFromDb -> {
                    var userDetails = userFromDb.toUserDetailsDto();
                    return new User(
                            userDetails.username(),
                            userDetails.password(),
                            userDetails.enable(), true, true, true,
                            List.of(new SimpleGrantedAuthority(userDetails.role())));
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
