package com.codecampus.AssignmentSubmissionApp.controller;

import com.codecampus.AssignmentSubmissionApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.codecampus.AssignmentSubmissionApp.domain.User;
import com.codecampus.AssignmentSubmissionApp.dto.RegisterUser;
import com.codecampus.AssignmentSubmissionApp.repository.UserRepository;

@RestController
@RequestMapping("/users")
@EnableMethodSecurity
public class UserController {

	@Autowired
	private UserService userService;
	@PostMapping("/register")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> register(@RequestBody RegisterUser request){
	userService.registerNewUser(request);
	return ResponseEntity.ok("user registered successfully");
	}

	@PostMapping("/{userId}/block")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> blockUser(@PathVariable Long userId){
		String response=userService.blockUser(userId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{userId}/unblock")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> unblockUser(@PathVariable Long userId){
		String response=userService.unblockUser(userId);
		return ResponseEntity.ok(response);
	}
}
