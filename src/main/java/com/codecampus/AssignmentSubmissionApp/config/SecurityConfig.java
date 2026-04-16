package com.codecampus.AssignmentSubmissionApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codecampus.AssignmentSubmissionApp.filter.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	private JwtFilter jwtFilter;
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http ) throws Exception {
		http
				.cors(cors->cors.configurationSource(corsConfigurationSource()))
				// disable CSRF for API'S
				.csrf(csrf -> csrf.disable())
				//add custom exception handlers
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(authEntryPoint)
				)
				// Stateless session
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Authorization Rules
				.authorizeHttpRequests(auth -> auth.requestMatchers(
								"/auth/login",
								"/auth/refresh",
								"/v3/api-docs/**",     // Swagger metadata
								"/swagger-ui/**",      // Swagger UI HTML
								"/swagger-ui.html").permitAll()
						.anyRequest().authenticated())
				.authenticationProvider(authenticationProvider())
				// Add Jwt Filter
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	//CORS configuration

	@Bean
	public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
		org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();

		// Allow the Angular dev server origin
		configuration.setAllowedOrigins(java.util.List.of("http://localhost:4200"));

		// Allow standard methods
		configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		// Allow all headers (important for Authorization and Content-Type)
		configuration.setAllowedHeaders(java.util.List.of("*"));

		// Allow cookies/credentials to be sent (since you're using cookies in Angular)
		configuration.setAllowCredentials(true);

		org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	//Authentication Manager for login API
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	//password encryption mechanism definition
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		// Use the setter methods instead of the constructor
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}
}
