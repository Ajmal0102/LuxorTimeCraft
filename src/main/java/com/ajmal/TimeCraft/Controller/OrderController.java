package com.ajmal.TimeCraft.Controller;

import com.ajmal.TimeCraft.Entity.EnumList.PaymentMode;
import com.ajmal.TimeCraft.Entity.EnumList.Status;
import com.ajmal.TimeCraft.Entity.Order;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Service.OrderService;
import com.ajmal.TimeCraft.Service.ProductService;
import com.ajmal.TimeCraft.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    @GetMapping("/order/view-order/{id}")
    public String viewOrderDetails(@PathVariable("id") long orderId,
                                   Model model) {
        Optional<Order> optionalOrder = orderService.findByOrderId(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            model.addAttribute("order",order);
            return "view-order";
        }
        return "404";
    }

    @GetMapping("/order/cancel-order/{id}")
    public String cancelOrder(@PathVariable("id")long orderId) {

        Optional<Order> optionalOrder = orderService.findByOrderId(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(Status.CANCELLED);
            orderService.save(order);
            return "redirect:/order/view-order/"+orderId;
        }
        return "redirect:/user/my-orders";
    }



}
