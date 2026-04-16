package com.codecampus.AssignmentSubmissionApp.service;

import com.codecampus.AssignmentSubmissionApp.domain.Authority;
import com.codecampus.AssignmentSubmissionApp.domain.User;
import com.codecampus.AssignmentSubmissionApp.dto.CreateAuthority;
import com.codecampus.AssignmentSubmissionApp.repository.AuthorityRepository;
import com.codecampus.AssignmentSubmissionApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthorityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Transactional
    public Authority createNewAuthority(@RequestBody CreateAuthority request){

        //check the availability of the user
        User user= userRepository.findById(request.getUserId())
                .orElseThrow(()-> new RuntimeException("User not found"));
        //check if the role already exists for the user
        if (authorityRepository.findByAuthority(request.getAuthority(), user).isPresent()){
        throw new RuntimeException("user with the role already found");
        }
        Authority authority= new Authority();
        authority.setAuthority(request.getAuthority());
        authority.setUser(user);

        return authorityRepository.save(authority);
    }


}
