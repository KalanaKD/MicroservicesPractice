package com.kd.fitness_tracker.service;

import com.kd.fitness_tracker.dto.RegisterRequest;
import com.kd.fitness_tracker.dto.UserResponse;
import com.kd.fitness_tracker.entity.User;
import com.kd.fitness_tracker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return  userResponse;
    }

    public UserResponse register(RegisterRequest request) {

        if( userRepo.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = userRepo.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(savedUser.getUserId());
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

    public Boolean existByUserId(String userId) {
        return userRepo.existsById(userId);
    }
}
