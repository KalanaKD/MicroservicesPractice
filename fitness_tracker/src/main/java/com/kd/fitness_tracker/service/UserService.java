package com.kd.fitness_tracker.service;

import com.kd.fitness_tracker.dto.RegisterRequest;
import com.kd.fitness_tracker.dto.UserResponse;
import com.kd.fitness_tracker.entity.User;
import com.kd.fitness_tracker.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserResponse getProfileById(String userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getUserId());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setKeyCloakId(user.getKeyCloakId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return  userResponse;
    }

    public UserResponse register(RegisterRequest request) {

        if( userRepo.existsByEmail(request.getEmail())){
            User existingUser = userRepo.findByEmail(request.getEmail());
            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(existingUser.getUserId());
            userResponse.setKeyCloakId(existingUser.getKeyCloakId());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setPassword(existingUser.getPassword());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            userResponse.setUpdatedAt(existingUser.getUpdatedAt());
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setKeyCloakId(request.getKeyCloakId());

        User savedUser = userRepo.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(savedUser.getUserId());
        userResponse.setKeyCloakId(savedUser.getKeyCloakId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;
    }

    public String deleteUser(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found"));
        userRepo.delete(user);
        return "User deleted successfully";
    }

    public Boolean existsByKeyCloakId(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return userRepo.existsByKeyCloakId(userId);
    }
}
