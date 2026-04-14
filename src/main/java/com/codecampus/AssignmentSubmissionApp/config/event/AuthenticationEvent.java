package com.codecampus.AssignmentSubmissionApp.config.event;

import com.codecampus.AssignmentSubmissionApp.repository.UserRepository;
import com.codecampus.AssignmentSubmissionApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvent{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService; // Using your block logic from earlier

    private static final int MAX_ATTEMPTS = 5;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        String username = success.getAuthentication().getName();
        userRepository.findByUsername(username).ifPresent(user -> {
            if (user.getFailedAttempts() > 0) {
                user.setFailedAttempts(0); // Reset on successful login
                userRepository.save(user);
            }
        });
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent failure) {
        String username = failure.getAuthentication().getName();

        userRepository.findByUsername(username).ifPresent(user -> {
            int newAttempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(newAttempts);

            if (newAttempts >= MAX_ATTEMPTS) {
                user.setAccountNonLocked(false); // Trigger the block!
            }
            userRepository.save(user);
        });
    }
}
