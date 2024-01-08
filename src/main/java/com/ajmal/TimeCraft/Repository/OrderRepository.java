package com.ajmal.TimeCraft.Repository;

import com.ajmal.TimeCraft.Entity.Order;
import com.ajmal.TimeCraft.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);

}
