package fr.abes.helloabes.web.configuration;

import fr.abes.helloabes.web.exception.ApplicationError;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        String urlPath = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
        log.error(e.getLocalizedMessage());

        this.sendUnAuthorizedError(urlPath,httpServletResponse); 
    }

    private void sendUnAuthorizedError(String urlPath, HttpServletResponse httpServletResponse) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        ApplicationError errorMessageHandler = new ApplicationError(HttpStatus.UNAUTHORIZED,"This ressource requires an authentification",urlPath);
        String json = mapper.findAndRegisterModules().writeValueAsString(errorMessageHandler);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(json);
    }
}

