package com.kd.activityService.controller;

import com.kd.activityService.dto.ActivityRequest;
import com.kd.activityService.dto.ActivityResponse;
import com.kd.activityService.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/activities/")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/create")
    public ResponseEntity<ActivityResponse> trackActivity(
            @RequestBody ActivityRequest request,
            @RequestHeader("X-User-ID") String userId
    ){
        if (userId != null) {
            request.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.trackActivity(request));
    }

    @GetMapping("/get-activity")
    public ResponseEntity<List<ActivityResponse>> getActivities(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(activityService.getActivities(userId));
    }

    @GetMapping("/get-activity/{activityId}")
    public ResponseEntity<ActivityResponse> getActivity(@PathVariable("activityId") String activityId){
        return ResponseEntity.ok(activityService.getActivitesById(activityId));
    }

}
