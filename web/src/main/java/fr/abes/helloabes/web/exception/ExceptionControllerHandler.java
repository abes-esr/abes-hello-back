package fr.abes.helloabes.web.exception;

import fr.abes.helloabes.core.exception.UserAlreadyExistsException;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionControllerHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiReturnError apiReturnError) {
        return new ResponseEntity<>(apiReturnError, apiReturnError.getStatus());
    }

    /**
     * Si le Token passé dans le header est dans un format incorrect
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        String error = "Malformed JSON request";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Si la méthode d'une requête ne correspond pas avec la méthode attendue du controller
     * @param ex HttpRequestMethodNotSupportedException
     * @return ResponseEntity
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String error = "Method is not supported for this request";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.METHOD_NOT_ALLOWED, error, ex));
    }

    /**
     * Si le nom d'utilisateur ou le mot de passe est/sont invalide(s)
     * @param ex Exception de type MethodArgumentNotValidException ou HandlerMethodValidationException
     * @return ResponseEntity
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(Exception ex) {
        String error = "The credentials are not valid";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Si la requête pointe vers une url non disponible (Page 404)
     * @param ex NoHandlerFoundException
     * @return ResponseEntity
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String error = "Page not found";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.NOT_FOUND, error, ex));
    }

    /**
     * Si un utilisateur non authentifié tente d'accéder à une ressource requérant une authentification
     * @param ex AuthenticationException
     * @return ResponseEntity
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        String error = "This ressource requires an authentification";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.UNAUTHORIZED, error, ex));
    }

    /**
     * Si un utilisateur tente de s'enregistrer avec un nom d'utilisateur existant déjà
     * @param ex UserAlreadyExistException
     * @return ResponseEntity
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        String error = "Username not available";
        log.error(error);
        return buildResponseEntity(new ApiReturnError(HttpStatus.BAD_REQUEST, error, ex));
    }

}