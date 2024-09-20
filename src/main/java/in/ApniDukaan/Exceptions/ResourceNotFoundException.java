package in.ApniDukaan.Exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {

    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
