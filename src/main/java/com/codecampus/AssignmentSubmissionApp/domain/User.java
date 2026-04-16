package com.codecampus.AssignmentSubmissionApp.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
@Getter
@Setter
@Entity
@Table(name="users")
public class User implements UserDetails{
    private static final long serialVersionUID = -443143418636020766L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
    private LocalDate cohortStartDate;
	private String username;
	private String password;
	@Column(nullable = false)
	private boolean isAccountNonExpired=true;
	@Column (nullable = false)
	private boolean isAccountNonLocked=true;
	@Column(name = "failed_attempts", nullable = false)
	private int failedAttempts = 0;
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Authority> authorities = new ArrayList<>();

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return true;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> roles = new ArrayList<>();

		// This now loops through the authorities from the DB
		if (authorities != null) {
			authorities.forEach(auth -> {
				//prefix the existing roles with ROLE_
				roles.add(new SimpleGrantedAuthority("ROLE_" + auth.getAuthority()));
			});
		}

		return roles;
	}
	
}
