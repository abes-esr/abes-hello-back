package fr.abes.helloabes.web.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApplicationError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String message;
    private String path;

    public ApplicationError(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }
}

