package study.games.flashcard.wars.exception.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import study.games.flashcard.wars.models.dtos.Response;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandling.class);

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Response<String>> accountDisabledException() {
        logger.error("Attempted login of disabled user");
        return createHttpResponse(HttpStatus.FORBIDDEN,
                "Your account is disabled. If this is a mistake please create a help ticket");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response<String>> badCredentialsException() {
        logger.error("Attempted login with bad credentials");
        return createHttpResponse(HttpStatus.FORBIDDEN, "Incorrect username and/or password");
    }

    

    private ResponseEntity<Response<String>> createHttpResponse(HttpStatus status, String message) {
        Response<String> response = Response.<String>builder()
                .timeStamp(LocalDateTime.now())
                .httpStatusCode(status.value())
                .httpStatus(status)
                .message(status.getReasonPhrase())
                .data(message)
                .build();
        return new ResponseEntity<>(response, status);
    }
}
