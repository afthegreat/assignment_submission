package com.codecampus.AssignmentSubmissionApp.service;

import com.codecampus.AssignmentSubmissionApp.domain.RefreshToken;
import com.codecampus.AssignmentSubmissionApp.dto.AuthRequest;
import com.codecampus.AssignmentSubmissionApp.dto.AuthResponse;
import com.codecampus.AssignmentSubmissionApp.dto.TokenRefreshRequest;
import com.codecampus.AssignmentSubmissionApp.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Transactional
    public AuthResponse login(@RequestBody AuthRequest request) {
        System.out.println("USERNAME: [" + request.getUsername() + "]");
        System.out.println("PASSWORD: [" + request.getPassword() + "]");
        //Authentication user (username + password)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        //Get authentication user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //Generate stateless access token
        String accessToken = jwtUtil.generateToken(userDetails);

        //casting userDetails to user entity to get the ID;
        com.codecampus.AssignmentSubmissionApp.domain.User user = (com.codecampus.AssignmentSubmissionApp.domain.User) userDetails;

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        //Return token
        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponse generateRefreshToken(@RequestBody TokenRefreshRequest request) {

        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    // Generate a new stateless Access Token (JWT)
                    String accessToken = jwtUtil.generateToken(user);
                    // Return the new access token along with the SAME refresh token
                    return new AuthResponse(accessToken, request.getRefreshToken());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    @Transactional
    public String logout(@RequestBody TokenRefreshRequest request){

        refreshTokenService.deleteByToken(request.getRefreshToken());
        return "Logged out successfully";

    }

}
