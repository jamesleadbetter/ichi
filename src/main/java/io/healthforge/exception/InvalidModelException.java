package io.healthforge.exception;

/**
 * Exception to be thrown when a model is invalid
 */
public class InvalidModelException extends Exception {

    /**
     * Constructs a new {@link InvalidModelException}
     *
     * @param message the error message
     */
    public InvalidModelException(final String message) {
        super(message);
    }
}
