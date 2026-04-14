package com.codecampus.AssignmentSubmissionApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
