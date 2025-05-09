package com.example.monewteam08.repository;

import com.example.monewteam08.entity.User;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsUserByEmail(String email);

  User findUserByEmailAndPassword(String email, String password);

  List<User> findByIdIn(Set<UUID> ids);
}
