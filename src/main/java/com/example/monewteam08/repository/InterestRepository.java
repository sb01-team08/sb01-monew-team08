package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Interest;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, UUID> {

}
