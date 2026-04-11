package com.codecampus.AssignmentSubmissionApp.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codecampus.AssignmentSubmissionApp.repository.UserRepository;
import com.codecampus.AssignmentSubmissionApp.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Get Authorization Header	
		final String authHeader = request.getHeader("Authorization");
		
		String jwt= null;
		String username=null;
		
		//Check if header conatins Bearer token
		if(authHeader!=null && authHeader.startsWith("Bearer ")) {
			jwt=authHeader.substring(7);//reamove "Bearer"
			username=jwtUtil.extractUsername(jwt);
		}
		
		//If username exists and user is not already authenticated
		if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			//Load user from DB
			UserDetails userDetails=userDetailsService.loadUserByUsername(username);
			
			//validate token
			if(jwtUtil.validateToken(jwt, userDetails)) {
				
				//create authentication object
				UsernamePasswordAuthenticationToken authToken=
						new UsernamePasswordAuthenticationToken(
								userDetails,null, userDetails.getAuthorities());
				
				//attach request details
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				//set authentication in context
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		//continue filter chain
		filterChain.doFilter(request, response);
		
	}
}
