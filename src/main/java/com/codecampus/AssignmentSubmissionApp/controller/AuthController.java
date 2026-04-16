package com.codecampus.AssignmentSubmissionApp.controller;

import com.codecampus.AssignmentSubmissionApp.domain.RefreshToken;
import com.codecampus.AssignmentSubmissionApp.dto.TokenRefreshRequest;
import com.codecampus.AssignmentSubmissionApp.service.AuthService;
import com.codecampus.AssignmentSubmissionApp.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codecampus.AssignmentSubmissionApp.dto.AuthRequest;
import com.codecampus.AssignmentSubmissionApp.dto.AuthResponse;
import com.codecampus.AssignmentSubmissionApp.util.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest request){
		AuthResponse response=authService.login(request);
		return ResponseEntity.ok(response);
	}
	@PostMapping("/refresh")
	public AuthResponse refreshToken(@RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser)
				.map(user -> {
					// Generate a new stateless Access Token (JWT)
					String accessToken = jwtUtil.generateToken(user);
					// Return the new access token along with the SAME refresh token
					return new AuthResponse(accessToken, requestRefreshToken);
				})
				.orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestBody TokenRefreshRequest request){
		String response= authService.logout(request);
		return ResponseEntity.ok(response);
	}
}
