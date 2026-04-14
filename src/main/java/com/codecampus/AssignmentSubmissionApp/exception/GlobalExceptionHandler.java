package com.codecampus.AssignmentSubmissionApp.exception;

import com.codecampus.AssignmentSubmissionApp.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    //to show the stacktrace only in dev mode

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {

        // Convert StackTrace to String
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTrace = sw.toString();

        String debugInfo="dev".equalsIgnoreCase(activeProfile)?stackTrace: "Stack trace hidded.set to dev profile mode to see.";
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                debugInfo // This will show up in Swagger/Postman
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex, WebRequest request) {

        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(), // 401 instead of 500
                "Authentication Failed",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                "dev".equalsIgnoreCase(activeProfile) ? sw.toString() : null
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleMethodAccessDenied(AccessDeniedException ex, WebRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                ex.getClass().getSimpleName(),
                "You do not have the required permissions to execute this operation.",
                request.getDescription(false).replace("uri=", ""),
                "dev".equalsIgnoreCase(activeProfile) ? "Method-level security blocked this request" : null
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}
