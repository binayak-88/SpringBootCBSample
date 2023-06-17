/**
 * 
 */
package org.learntek.cb.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

/**
 * @author HP
 *
 */
@Configuration
public class AppConfig {

	@Bean
	public CircuitBreaker circuitBreaker() {
		
		CircuitBreakerConfig config = CircuitBreakerConfig.custom().slidingWindowType(SlidingWindowType.COUNT_BASED)
		.slidingWindowSize(1)
		.failureRateThreshold(65.0f)
		.slowCallDurationThreshold(Duration.ofMillis(2000))
		.permittedNumberOfCallsInHalfOpenState(3)
		.maxWaitDurationInHalfOpenState(Duration.ofSeconds(4)).
		 waitDurationInOpenState(Duration.ofMillis(6000)).build();
		
		CircuitBreakerRegistry cbr = CircuitBreakerRegistry.of(config);
		
		CircuitBreaker cb = cbr.circuitBreaker("person-service");
		
		return cb;
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
