/**
 * 
 */
package org.learntek.cb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

/**
 * @author HP
 *
 */
@ControllerAdvice
public class MyExceptionHandler {

	@ExceptionHandler(CallNotPermittedException.class)
	public ResponseEntity<MyError> handleCircuitBreakerException(CallNotPermittedException e) {
		MyError myError = new MyError();
		myError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		myError.setErrorMessage("We are experiencing some issues, plz try after sometime.............");
		ResponseEntity<MyError> myErrEntity = new ResponseEntity<MyError>(myError, HttpStatus.INTERNAL_SERVER_ERROR);
		return myErrEntity;
	}
}
