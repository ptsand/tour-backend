package kea.dat3.error;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends Client4xxException {

    public UnauthorizedException() {
        super("Unauthorized to access this resource", HttpStatus.UNAUTHORIZED);
    }

}
