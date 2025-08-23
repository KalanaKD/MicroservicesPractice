package com.kd.aiservices.service;

import com.kd.aiservices.model.Activity;
import com.kd.aiservices.model.Recommendation;
import com.kd.aiservices.repo.RecommendationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityServiceListner {

    private final ActivityAi activityAi;
    private final RecommendationRepo recommendationRepo;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        log.info("Received activity for processing {}", activity.getId());
        //log.info("Generated Recommendation : {}", activityAi.generateRecommendation(activity));
        Recommendation recommendation = activityAi.generateRecommendation(activity);
        recommendationRepo.save(recommendation);
    }
}
