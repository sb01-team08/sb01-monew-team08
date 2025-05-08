package com.example.monewteam08.repository;

import com.example.monewteam08.entity.UserActivity;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityMongoRepository extends MongoRepository<UserActivity, UUID> {

}
