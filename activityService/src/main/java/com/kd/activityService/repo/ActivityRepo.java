package com.kd.activityService.repo;

import com.kd.activityService.entity.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ActivityRepo extends MongoRepository<Activity, String> {
    @Query(value = "{ 'userId': ?0 }", collation = "{ 'locale': 'simple' }")
    List<Activity> findByUserId(String userId);

    @Query(value = "{ '_id': ?0 }", collation = "{ 'locale': 'en' }")
    Optional<Activity> findByActivityId(String activityId);
}
