package com.kd.aiservices.service;

import com.kd.aiservices.model.Recommendation;
import com.kd.aiservices.repo.RecommendationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {
    @Autowired
    private RecommendationRepo recommendationRepo;

    public List<Recommendation> getUserRecommendations(String userId) {
        return recommendationRepo.findByUserId(userId);
    }

    public Recommendation getActivityRecommendations(String activityId) {
        return recommendationRepo.findByActivityId(activityId)
                .orElseThrow(()-> new RuntimeException("No recommendations for this ActivityId: "+activityId));
    }
}
