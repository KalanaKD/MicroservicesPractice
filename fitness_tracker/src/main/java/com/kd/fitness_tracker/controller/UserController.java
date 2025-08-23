package com.kd.fitness_tracker.controller;

import com.kd.fitness_tracker.dto.RegisterRequest;
import com.kd.fitness_tracker.dto.UserResponse;
import com.kd.fitness_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users/")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.existByUserId(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @DeleteMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable("userId") String userId){
        return userService.deleteUser(userId);
    }
}
