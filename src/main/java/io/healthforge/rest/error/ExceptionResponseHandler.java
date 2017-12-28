package io.healthforge.rest.error;

import io.healthforge.exception.InvalidModelException;
import io.healthforge.models.GenericResponse;
import io.healthforge.models.GenericResponseCode;
import io.healthforge.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Extension of the {@link ResponseEntityExceptionHandler} for handling Exceptions thrown from Controllers and
 * returning custom responses
 */
@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles a {@link NotFoundException} thrown from the API and returns a 404 response
     *
     * @param exception the exception thrown
     * @return a Not Found response
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<GenericResponse> handleNotFoundException(final NotFoundException exception) {
        final GenericResponse response = new GenericResponse(GenericResponseCode.ERROR, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles a {@link InvalidModelException} thrown from the API and returns a 400 response
     *
     * @param exception the exception thrown
     * @return a Bad Request response
     */
    @ExceptionHandler(value = InvalidModelException.class)
    public ResponseEntity<GenericResponse> handleInvalidModelException(final InvalidModelException exception) {
        final GenericResponse response = new GenericResponse(GenericResponseCode.ERROR, exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
