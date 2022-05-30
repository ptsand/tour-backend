package kea.dat3.error;

import org.springframework.http.HttpStatus;

public class Client4xxException extends RuntimeException{
    HttpStatus status;
    public Client4xxException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
    public Client4xxException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
