package com.codecampus.AssignmentSubmissionApp.controller;

import com.codecampus.AssignmentSubmissionApp.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	private AuthorityService authorityService;

	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createauthority(@RequestBody CreateAuthority request){
		Authority response=authorityService.createNewAuthority(request);
		return ResponseEntity.ok(response);
	}

}
