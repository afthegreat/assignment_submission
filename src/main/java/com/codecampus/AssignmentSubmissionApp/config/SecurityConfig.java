package com.codecampus.AssignmentSubmissionApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codecampus.AssignmentSubmissionApp.filter.JwtFilter;

@Configuration
public class SecurityConfig {
	@Autowired
	private JwtFilter jwtFilter;
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// disable CSRF for API'S
				.csrf(csrf -> csrf.disable())

				// Stateless session
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Authorization Rules
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login", "/users/register").permitAll()
						.anyRequest().authenticated())
				.authenticationProvider(authenticationProvider())
				// Add Jwt Filter
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		// .logout(logout -> logout.permitAll());

		return http.build();
	}

	//Authentication Manager for login API
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
	
	    authProvider.setPasswordEncoder(passwordEncoder());
	
	    return authProvider;
	}
	
}
