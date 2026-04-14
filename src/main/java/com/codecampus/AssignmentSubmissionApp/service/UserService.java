package com.codecampus.AssignmentSubmissionApp.service;

import com.codecampus.AssignmentSubmissionApp.domain.User;
import com.codecampus.AssignmentSubmissionApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
   @Autowired
   private UserRepository userRepository;

   @Transactional
    public void blockUser(Long userId){
       User user=userRepository.findById(userId)
               .orElseThrow(()-> new RuntimeException("user not found" + userId));
       user.setAccountNonLocked(false);

       userRepository.save(user);
   }

   @Transactional
    public void unblockUser(Long userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new RuntimeException("no user found" + userId));
       user.setAccountNonLocked(true);
   }
}
