package fr.abes.helloabes.web.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    // Gestion des erreurs standard
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleStandardException(Exception ex, WebRequest request) {

        log.error(ex.getMessage(),ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errors", ex.getClass());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Gestion des erreurs levées par Spring
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        log.error(ex.getMessage(),ex);

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("errors", ex.getClass().getSimpleName());

        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        body.put("message", messages);

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Gestion des exceptions spécifiques
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCrendentialsException(
            BadCredentialsException ex, WebRequest request) {

        log.error(ex.getMessage(),ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", ex.getMessage());
        body.put("errors", ex.getClass().getSimpleName());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}