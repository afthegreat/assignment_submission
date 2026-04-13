package com.codecampus.AssignmentSubmissionApp.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
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
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path= request.getServletPath();
		System.out.println("The Path is "+ path);
		return path.startsWith("/auth/login") ||
				path.startsWith("/users/register")||
				path.startsWith("/swagger-ui") ||  // Add this
				path.startsWith("/v3/api-docs");
	}
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
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// We still load UserDetails to check if the account is disabled/locked
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtUtil.validateToken(jwt, userDetails)) {

				// --- THIS IS THE EXTRACTION STEP ---
				// 1. Get the "ROLE_ADMIN,ROLE_STUDENT" string from the token
				String authoritiesStr = jwtUtil.extractAuthorities(jwt);

				// 2. Convert that string into Spring Security's list format
				var authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesStr);

				// 3. Create the auth object using the roles from the TOKEN
				UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
				// ------------------------------------

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}		//continue filter chain
		filterChain.doFilter(request, response);
		
	}
}
