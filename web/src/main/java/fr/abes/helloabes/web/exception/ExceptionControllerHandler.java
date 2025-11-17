package fr.abes.helloabes.web.exception;

import fr.abes.helloabes.core.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class ExceptionControllerHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiReturnError apiReturnError) {
        return new ResponseEntity<>(apiReturnError, apiReturnError.getStatus());
    }

    /**
     * Vérifier le Token passé dans le header avec un format correct
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        String error = "Malformed JSON request";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Vérifier les méthodes correspondent avec les URI dans le controller
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String error = "Method is not supported for this request";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.METHOD_NOT_ALLOWED, error, ex));
    }

    /**
     * Vérifier le nom d'utilisateur et le mot de passe lors de l'inscription
     * @param ex
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(Exception ex) {
        String error = "The credentials are not valid";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Page 404
     * @param ex
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String error = "Page not found";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.NOT_FOUND, error, ex));
    }

    /**
     * Si l'authentification d'un utilisateur a echoué
     * @param ex AuthenticationException
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        String error = "This ressource requires an authentification";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.UNAUTHORIZED, error, ex));
    }

    /**
     * Si un utilisateur avec le nom d'utilisateur existe déjà
     * @param ex UserAlreadyExistException
     * @return
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        String error = "Username not available";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.BAD_REQUEST, error, ex));
    }

}