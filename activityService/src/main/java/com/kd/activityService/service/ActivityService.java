package com.kd.activityService.service;

import com.kd.activityService.dto.ActivityRequest;
import com.kd.activityService.dto.ActivityResponse;
import com.kd.activityService.entity.Activity;
import com.kd.activityService.repo.ActivityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    @Autowired
    private final ActivityRepo activityRepo;

    public ActivityResponse trackActivity(ActivityRequest request) {

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .activityType(request.getActivityType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepo.save(activity);

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

    public ActivityResponse getActivitesById(String activityId) {
        return activityRepo.findByActivityId(activityId).map(this::mapToResponse)
                .orElseThrow(()->new RuntimeException("Activity not found with id: "+activityId));
    }
}
