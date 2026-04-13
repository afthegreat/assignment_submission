package com.codecampus.AssignmentSubmissionApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codecampus.AssignmentSubmissionApp.domain.Authority;
import com.codecampus.AssignmentSubmissionApp.domain.User;
import com.codecampus.AssignmentSubmissionApp.dto.CreateAuthority;
import com.codecampus.AssignmentSubmissionApp.repository.AuthorityRepository;
import com.codecampus.AssignmentSubmissionApp.repository.UserRepository;

@RestController
@RequestMapping("/authority")
public class AuthorityController {

	@Autowired
	private AuthorityRepository authorityRepository;
	
	@Autowired
	private UserRepository userRepository;
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public String createauthority(@RequestBody CreateAuthority request) {
		System.out.println("inside the authority controller ");
		//check if the user is existing
		User user= userRepository.findById(request.getUserId())
				.orElseThrow(()-> new RuntimeException("User not found"));
		//check if the user has that role already
		if(authorityRepository
				.findByAuthorityAndUser(request.getAuthority(),user).isPresent()) {
			return "User already has this role!";
		}
		//create the authority
		
		Authority authority= new Authority();
		authority.setAuthority(request.getAuthority());
		authority.setUser(user);
		authorityRepository.save(authority);
		return "Role Created Successfully for the user "+ user.getUsername();
	}
}
