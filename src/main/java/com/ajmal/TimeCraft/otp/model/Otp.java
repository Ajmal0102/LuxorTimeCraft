package com.ajmal.TimeCraft.otp.model;

import com.ajmal.TimeCraft.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "otp")
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String otp;
    private String name;

    private LocalDateTime time;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
