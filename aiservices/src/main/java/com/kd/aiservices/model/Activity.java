package com.kd.aiservices.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Map;
@Data
public class Activity {

    @Id
    private String id;
    private String userId;
    private String type;
    private int duration;
    private int caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
