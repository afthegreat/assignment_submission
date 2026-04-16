package com.codecampus.AssignmentSubmissionApp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;        // e.g., "Bad Request"
        private String exception;    // e.g., "RuntimeException"
        private String message;      // e.g., "User not found"
        private String path;
        private String stackTrace;   // The detailed debug info

        public ErrorResponse(int status, String error, String exception, String message, String path, String stackTrace) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.error = error;
            this.exception = exception;
            this.message = message;
            this.path = path;
            this.stackTrace = stackTrace;
        }
    }

