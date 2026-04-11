package com.codecampus.AssignmentSubmissionApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/login")
	public AuthResponse login(@RequestBody AuthRequest request) {
		System.out.println("USERNAME: [" + request.getUsername() + "]");
		System.out.println("PASSWORD: [" + request.getPassword() + "]");
		//Authentication user (username + password)
		Authentication authentication= authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(),
						request.getPassword()));
		
		//Get authentication user
		UserDetails userDetails= (UserDetails) authentication.getPrincipal();
		
		//Generate token
		String token= jwtUtil.generateToken(userDetails);
		
		//Return token
		return new AuthResponse(token);
	}
}
