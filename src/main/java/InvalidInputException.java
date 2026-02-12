/**
 * Exception handler for invalid inputs
 */
public class InvalidInputException extends Exception{

    /**
     * Default exception handler method
     */
    public InvalidInputException() {
        super("Invalid input.");
    }

    public InvalidInputException(String message) {
        super(message);
    }
}

