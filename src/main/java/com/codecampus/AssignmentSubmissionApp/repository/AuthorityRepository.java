package com.codecampus.AssignmentSubmissionApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codecampus.AssignmentSubmissionApp.domain.Authority;
import com.codecampus.AssignmentSubmissionApp.domain.User;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	Optional<Authority>findByAuthority(String authority);
	Optional<Authority>findByAuthorityAndUser(String authority, User user);

}
