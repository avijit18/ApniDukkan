package in.ApniDukaan.Security;

import in.ApniDukaan.Entities.UserEntity;
import in.ApniDukaan.Repositories.UserRepository;
import in.ApniDukaan.Services.Interfaces.UserEntityService;
import in.ApniDukaan.Utils.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserRepository userRepository;
    private final UserEntityService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("username");

        UserEntity userDtls = userRepository.findByEmail(email);

        if (userDtls != null) {

            if (userDtls.getIsEnable()) {

                if (userDtls.getAccountNonLocked()) {

                    if (userDtls.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
                        userService.increaseFailedAttempt(userDtls);
                    } else {
                        userService.userAccountLock(userDtls);
                        exception = new LockedException("Your account is locked !! failed attempt 3");
                    }
                } else {

                    if (userService.unlockAccountTimeExpired(userDtls)) {
                        exception = new LockedException("Your account is unlocked !! Please try to login");
                    } else {
                        exception = new LockedException("your account is Locked !! Please try after sometimes");
                    }
                }

            } else {
                exception = new LockedException("your account is inactive");
            }
        } else {
            exception = new LockedException("Email & password invalid");
        }

        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
