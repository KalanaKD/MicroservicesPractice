package com.kd.activityService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId){
        log.info("Calling User Validation API for userId: {}", userId);

        try{
            return Boolean.TRUE.equals(userServiceWebClient.get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());

        }catch(WebClientResponseException e){
            if(e.getRawStatusCode() == 404){
                throw new RuntimeException("User not found - UserId : " + userId);
            } else if (e.getRawStatusCode()== 400) {
                throw new RuntimeException("Bad Request - Invalid UserId : " + userId);
            }
            throw new RuntimeException("Error validating user: " + e.getMessage());
        }
    }

}
