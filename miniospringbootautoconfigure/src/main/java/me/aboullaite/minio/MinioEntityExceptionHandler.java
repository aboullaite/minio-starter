package me.aboullaite.minio;

import io.minio.errors.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MinioEntityExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = {InvalidPortException.class})
    protected ResponseEntity<Object> handlePortError(RuntimeException ex, WebRequest request) {
        MinioError error = new MinioError(HttpStatus.BAD_REQUEST, "The given port number is not valid.");
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {InvalidEndpointException.class})
    protected ResponseEntity<Object> handleEndpointError(RuntimeException ex, WebRequest request) {
        MinioError error = new MinioError(HttpStatus.BAD_REQUEST, "The given endpoint number is not valid.");
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {RegionConflictException.class})
    protected ResponseEntity<Object> handleRegionConflictError(RuntimeException ex, WebRequest request) {
        MinioError error = new MinioError(HttpStatus.CONFLICT, "The passed region conflicts with the one previously specified.");
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {NoResponseException.class})
    protected ResponseEntity<Object> handleNoResponseError(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "No response is received!";
        MinioError error = new MinioError(HttpStatus.BAD_REQUEST, "No response is received!");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(value = {ErrorResponseException.class})
    protected ResponseEntity<Object> handleResponseError(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Received an error response while executing the requested operation!";
        MinioError error = new MinioError(HttpStatus.SERVICE_UNAVAILABLE, "The given endpoint number is not valid.");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(value = {InsufficientDataException.class})
    protected ResponseEntity<Object> handleInsufficientDataError(RuntimeException ex, WebRequest request) {
        MinioError error = new MinioError(HttpStatus.SERVICE_UNAVAILABLE, "Reading given InputStream gets EOFException before reading given length.");
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(value = {InvalidBucketNameException.class})
    protected ResponseEntity<Object> handleBucketNameError(RuntimeException ex, WebRequest request) {
        MinioError error = new MinioError(HttpStatus.BAD_REQUEST, "The given bucket name is not valid!");
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {InvalidExpiresRangeException.class})
    protected ResponseEntity<Object> handleExpiresRangeError(RuntimeException ex, WebRequest request) {
        MinioError error = new MinioError(HttpStatus.BAD_REQUEST, "The given expires value is out of range.");
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {InvalidObjectPrefixException.class})
    protected ResponseEntity<Object> handleObjectPrefixError(RuntimeException ex, WebRequest request) {
        MinioError error = new MinioError(HttpStatus.BAD_REQUEST, "The given object prefix is not valid");
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentError(RuntimeException ex, WebRequest request) {
        MinioError error = new MinioError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleError(RuntimeException ex, WebRequest request) {
        MinioError error = new MinioError(HttpStatus.BAD_REQUEST, "An unexpected internal error occured while processing the request");
        ex.printStackTrace();
        return handleExceptionInternal(ex, error,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
