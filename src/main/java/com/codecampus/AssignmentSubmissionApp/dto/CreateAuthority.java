package com.codecampus.AssignmentSubmissionApp.dto;

import com.codecampus.AssignmentSubmissionApp.domain.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateAuthority {

	private String authority;
	private User user;

}
