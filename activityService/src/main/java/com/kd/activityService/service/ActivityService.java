package com.kd.activityService.service;

import com.kd.activityService.dto.ActivityRequest;
import com.kd.activityService.dto.ActivityResponse;
import com.kd.activityService.entity.Activity;
import com.kd.activityService.repo.ActivityRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepo activityRepo;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserId());

        if (!isValidUser) {
            throw new RuntimeException("Invalid user id" + request.getUserId()) ;
        }
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .activityType(request.getActivityType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepo.save(activity);

        //publish to rabbitmq for Ai processing
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);

        }catch (Exception e){
            log.error("Failed to send activity to RabbitMQ");
        }

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setActivityType(activity.getActivityType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());
        return activityResponse;
    }

    public List<ActivityResponse> getActivities(String userId) {
        List<Activity> activities = activityRepo.findByUserId(userId);
        return activities.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ActivityResponse getActivitiesById(String activityId) {
        return activityRepo.findByActivityId(activityId).map(this::mapToResponse)
                .orElseThrow(()->new RuntimeException("Activity not found with id: "+activityId));
    }
}
