package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.*;
import com.ajmal.TimeCraft.Entity.EnumList.PaymentMode;
import com.ajmal.TimeCraft.Entity.EnumList.Status;
import com.ajmal.TimeCraft.Repository.OrderItemRepository;
import com.ajmal.TimeCraft.Repository.OrderRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;


    @Override
    public Order saveOrder(User user, List<Cart> cartItems, double totalPrice, PaymentMode selectedPaymentMode, Address userAddress) {

        Order order = getOrder(user, totalPrice, selectedPaymentMode, userAddress);
        String orderId = generateOrderId();
        order.setOrderId(orderId);

        List<OrderItems> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItems orderItem = getOrderItem(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();
        order.setOrderItems(orderItems);
        order.setStatus(Status.CONFIRMED);
        orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Optional<Order> findByOrderId(long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }


    @NotNull
    private static OrderItems getOrderItem(Cart cartItem) {
        OrderItems orderItem = new OrderItems();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setProductPrice(cartItem.getProduct().getPrice());
        return orderItem;
    }


    @NotNull
    private static Order getOrder(User user, Double totalPrice, PaymentMode selectedPaymentMode,
                                  Address userAddress) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(totalPrice);
        order.setPaymentMode(selectedPaymentMode);
        order.setAddress(userAddress);
        order.setOrderedAddress(userAddress.getFullAddress());
        return order;
    }



    // For Generating OrderID
    public String generateOrderId() {
        int orderIdLength = 11;
        String prefix = "OD";
        String allowedChars = "0123456789";
        Random random =new Random();
        StringBuilder orderId = new StringBuilder(prefix);
        for (int i = 0; i < orderIdLength; i++) {
            int index = random.nextInt(allowedChars.length());
            orderId.append(allowedChars.charAt(index));
        }
        return orderId.toString();
    }
}
