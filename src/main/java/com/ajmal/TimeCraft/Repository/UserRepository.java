package com.ajmal.TimeCraft.Repository;

import com.ajmal.TimeCraft.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
//    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByEmail(String email);

}
