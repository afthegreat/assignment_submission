package com.codecampus.AssignmentSubmissionApp.exception;

import com.codecampus.AssignmentSubmissionApp.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // 1. Prepare the JSON Mapper
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Needed for LocalDateTime

        // 2. Build your standard ErrorResponse
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized Access",
                authException.getClass().getSimpleName(),
                "You must be logged in to access this resource. Please provide a valid token.",
                request.getRequestURI(),
                null // Keep stack trace null here as this is a standard security block
        );

        // 3. Write to response
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 is better than 403 for "Not Logged In"
        response.getWriter().write(mapper.writeValueAsString(error));
    }


}
