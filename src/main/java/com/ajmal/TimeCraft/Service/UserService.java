package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    List<User> findAllUsers();
//    Optional<User> findUserByEmail(String email);
   Optional<User> findUserByEmail(String email);

    void save(User user);

    Optional<User> findById(Long id);

    void delete(User user1);

    void otpManagement(User user);

    int verifyAccount(String email, String otp);

}
