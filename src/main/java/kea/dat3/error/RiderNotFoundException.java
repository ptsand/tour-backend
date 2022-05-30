package kea.dat3.error;

import org.springframework.http.HttpStatus;

public class RiderNotFoundException extends Client4xxException {

    private static final String message = "Rider with id '%s' not found";

    public RiderNotFoundException(long id) {
        super(String.format(message, id), HttpStatus.NOT_FOUND);
    }

}
