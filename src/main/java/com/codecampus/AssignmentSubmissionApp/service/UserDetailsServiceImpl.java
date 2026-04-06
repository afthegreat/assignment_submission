package com.codecampus.AssignmentSubmissionApp.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.codecampus.AssignmentSubmissionApp.domain.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user=new User();
		user.setUsername(username);
		user.setPassword("asdfasdf");
	    user.setPassword(new BCryptPasswordEncoder().encode("asdfasdf"));
		user.setId(1);
		return user;
		
	}

}
