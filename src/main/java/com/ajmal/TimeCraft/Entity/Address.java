package com.ajmal.TimeCraft.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String houseName;
    private String city;
    private String state;
    private Long pincode;
    private String landmark;
    private  boolean enabled;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
