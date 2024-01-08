package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Address;
import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.EnumList.PaymentMode;
import com.ajmal.TimeCraft.Entity.Order;
import com.ajmal.TimeCraft.Entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order saveOrder(User user, List<Cart> cartItems, double totalPrice, PaymentMode selectedPaymentMode, Address userAddress);

    List<Order> findByUser(User user);

    Optional<Order> findByOrderId(long orderId);

    List<Order> findAll();

    void save(Order order);

}
