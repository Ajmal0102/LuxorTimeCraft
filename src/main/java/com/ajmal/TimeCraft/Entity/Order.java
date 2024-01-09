package com.ajmal.TimeCraft.Entity;

import com.ajmal.TimeCraft.Entity.EnumList.PaymentMode;
import com.ajmal.TimeCraft.Entity.EnumList.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<OrderItems> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "address_id")
    Address address;

//    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    PaymentMode paymentMode;

//    @Column(length = 20) // Adjust the length according to your needs
    @Enumerated(EnumType.STRING)
    Status status;

    Double totalPrice;

    private String orderedAddress;
    private LocalDate orderDate;
    private LocalDate shippingDate;
    private LocalDate deliveryDate;
}
