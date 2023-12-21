package com.ajmal.TimeCraft.otp.repository;

import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.otp.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp,Long> {


    Otp findByUser(User user);
}
