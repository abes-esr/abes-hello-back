package fr.abes.helloabes.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.abes.helloabes.web.exception.ApplicationError;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private void sendInternalServerError(String urlPath, HttpServletResponse httpServletResponse) throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        ApplicationError errorMessageHandler = new ApplicationError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"INTERNAL_SERVER_ERROR",urlPath);
        String json = mapper.findAndRegisterModules().writeValueAsString(errorMessageHandler);
        httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(json);
    }

    private void sendForbiddenError(String urlPath, HttpServletResponse httpServletResponse) throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        ApplicationError errorMessageHandler = new ApplicationError(HttpServletResponse.SC_FORBIDDEN,"FORBIDDEN",urlPath);
        String json = mapper.findAndRegisterModules().writeValueAsString(errorMessageHandler);
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(json);

    }

    private void sendUnAuthorizedError(String urlPath, HttpServletResponse httpServletResponse) throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        ApplicationError errorMessageHandler = new ApplicationError(HttpServletResponse.SC_UNAUTHORIZED,"UNAUTHORIZED",urlPath);
        String json = mapper.findAndRegisterModules().writeValueAsString(errorMessageHandler);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(json);

    }

}

