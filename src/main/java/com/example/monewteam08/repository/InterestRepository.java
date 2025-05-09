package com.example.monewteam08.repository;

import com.example.monewteam08.entity.Interest;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestRepository extends JpaRepository<Interest, UUID> {

  @EntityGraph(attributePaths = "keywords")
  List<Interest> findByIdIn(Set<UUID> ids);

}
