package com.codecampus.AssignmentSubmissionApp.service;

import com.codecampus.AssignmentSubmissionApp.domain.User;
import com.codecampus.AssignmentSubmissionApp.dto.RegisterUser;
import com.codecampus.AssignmentSubmissionApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
   @Autowired
   private UserRepository userRepository;
   @Autowired
   private PasswordEncoder passwordEncoder;
   //register user
   @Transactional
   public User registerNewUser(RegisterUser request){
     //check for duplicate of user
     if(userRepository.findByUsername(request.getUsername()).isPresent()){
        throw new RuntimeException("User name already exists") ;
     }

     //Data transformation
     User user=new User();
     user.setUsername(request.getUsername());
     user.setPassword(passwordEncoder.encode(request.getPassword()));

     // return and save the transformed user data
      return userRepository.save(user);
   }
   @Transactional
    public String blockUser(Long userId){
       User user=userRepository.findById(userId)
               .orElseThrow(()-> new RuntimeException("user not found" + userId));
       user.setAccountNonLocked(false);

       userRepository.save(user);
       return "user blocked";
   }

   @Transactional
    public String unblockUser(Long userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new RuntimeException("no user found" + userId));
       user.setAccountNonLocked(true);
       user.setFailedAttempts(0);
       return "user Unblocked";
   }
}
