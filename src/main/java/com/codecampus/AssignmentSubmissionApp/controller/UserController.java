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
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;
	@PostMapping("/register")
	@PreAuthorize("hasRole('ADMIN')")
	public String register(@RequestBody RegisterUser request) {
		System.out.println("inside the user controller ");

		// check if user already exists
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			return "Username already exists!";
		}
		User user= new User();
		user.setUsername(request.getUsername());
		
		//encode password
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		userRepository.save(user);
		
		return "user registered successfully";
	}

	@PostMapping("/{userId}/block")
	@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> blockUser(@PathVariable Long userId){
		userService.blockUser(userId);
		return ResponseEntity.ok("User with ID" + userId + "has been blocked");
	}

	@PostMapping("/{userId}/unblock")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> unblockUser(@PathVariable Long userId){
		userService.unblockUser(userId);
		return ResponseEntity.ok("User with ID "+userId+"has been unblocked");
	}
}
