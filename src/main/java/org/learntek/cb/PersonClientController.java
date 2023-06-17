/**
 * 
 */
package org.learntek.cb;

import java.util.function.Supplier;

import org.learntek.cb.exception.MyError;
import org.learntek.cb.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
/**
 * @author HP
 *
 */
@RestController
public class PersonClientController {
	
	@Autowired
	private CircuitBreaker breaker;
	
	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/person/{id}")
	public ResponseEntity<Person> getPerson(@PathVariable("id") int id) {
		ResponseEntity<Person> responseEntity = null;
		//try {
			for (int i = 0; i < 100; i++) {
				responseEntity = breaker
						.decorateSupplier(
								() -> restTemplate.getForEntity("http://localhost:8090/person/{id}", Person.class, id))
						.get();
				System.out.println("Back end service called.............");
			}
		return responseEntity;
	}
	
	
	@io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "mycb", fallbackMethod = "myError")
	@GetMapping("/person/{id}")
	public ResponseEntity<Person> getPersonbyId(@PathVariable("id") int id) {
		ResponseEntity<Person> responseEntity = null;
		for (int i = 0; i < 100; i++) {
			responseEntity = breaker
					.decorateSupplier(
							() -> restTemplate.getForEntity("http://localhost:8090/person/{id}", Person.class, id))
					.get();
			System.out.println("Back end service called.............");
		}
		return responseEntity;
	} 
	
	public ResponseEntity<MyError> myError() {
		MyError myError = new MyError();
		myError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		myError.setErrorMessage("We are experiencing some issues, plz try after sometime.............");
		ResponseEntity<MyError> myErrEntity = new ResponseEntity<MyError>(myError, HttpStatus.INTERNAL_SERVER_ERROR);
		return myErrEntity;
	}
}