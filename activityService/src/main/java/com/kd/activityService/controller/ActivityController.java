package com.kd.activityService.controller;

import com.kd.activityService.dto.ActivityRequest;
import com.kd.activityService.dto.ActivityResponse;
import com.kd.activityService.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    // POST /api/activities-> create activity (frontend calls api.post('/activities', ...))
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(
            @RequestBody ActivityRequest request,
            @RequestHeader(value = "X-User-ID", required = false) String userId
    ){
        if (userId != null) {
            request.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

    // GET /api/activities-> list
    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getActivities(@RequestHeader(value = "X-User-ID", required = false) String userId){
        return ResponseEntity.ok(activityService.getActivities(userId));
    }

    // GET /api/activities/{activityId}
    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable("activityId") String activityId){
        return ResponseEntity.ok(activityService.getActivitiesById(activityId));
    }
}
