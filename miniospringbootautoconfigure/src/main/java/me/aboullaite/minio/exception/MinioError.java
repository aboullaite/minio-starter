package me.aboullaite.minio.exception;

import org.springframework.http.HttpStatus;

public class MinioError {

    private HttpStatus status;
    private String message;

    public MinioError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
