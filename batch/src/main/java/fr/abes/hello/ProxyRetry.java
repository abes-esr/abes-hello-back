package fr.abes.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProxyRetry {

	@Retryable(maxAttempts = 2)
	public String getLine(String line) {
		log.info("traitement susceptible de necessiter un retry...");
		line = line.toUpperCase();
		return line;
	}
}
