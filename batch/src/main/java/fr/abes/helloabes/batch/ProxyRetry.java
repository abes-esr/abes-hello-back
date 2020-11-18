package fr.abes.helloabes.batch;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProxyRetry {

	int i = 0;
	@Retryable(maxAttempts = 2)
	public String getLine(AppUser user) {
		i += 1;
		log.info("traitement susceptible de necessiter un retry...");

		return String.format(String.valueOf(i) + ";L'utilisateur;%s",user.getUserName());
	}
}
