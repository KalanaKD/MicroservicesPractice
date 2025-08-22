package com.kd.aiservices.service;

import com.kd.aiservices.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityServiceListner {

    private final ActivityAi activityAi;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        log.info("Received activity for proecssing {}", activity.getId());
        log.info("Generated Recommendation : {}", activityAi.generateRecommendation(activity));
    }
}
