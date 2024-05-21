package study.games.flashcard.wars.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import study.games.flashcard.wars.auth.UserPrinciple;
import study.games.flashcard.wars.auth.service.LoginAttemptService;
import study.games.flashcard.wars.model.entities.AppUser;
import study.games.flashcard.wars.service.UserService;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationSuccessListener {
    private final LoginAttemptService loginAttemptService;
    private final UserService userService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent authenticationSuccessEvent) {
        Object principle = authenticationSuccessEvent.getAuthentication().getPrincipal();
        if(principle instanceof UserPrinciple userPrinciple) {
            AppUser user = userPrinciple.getAppUser();
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
            loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
            userService.updateUserLastLoginToNow(user.getId());
            log.info("user has successfully logged in: " + user.getUsername());
        }
    }
}