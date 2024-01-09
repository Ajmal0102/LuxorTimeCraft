package com.ajmal.TimeCraft.Controller;

import com.ajmal.TimeCraft.Entity.Address;
import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.EnumList.PaymentMode;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Service.*;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class RazorPayController {


    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;
    @Autowired
    AddressService addressService;

    @GetMapping("/payment/{totalAmount}")
    public String razorPay(@PathVariable Double totalAmount,
                           @RequestParam("addressId") long addressId,
                           @RequestParam("selectedPaymentMode") PaymentMode selectedPaymentMode,
                           Model model) throws RazorpayException {

//        RazorpayClient razorpay = new RazorpayClient("rzp_test_wSP8EmmFjEbRbD", "wDhpCDheW2ZzApA89aD6hhAu");
//
//        JSONObject orderRequest = new JSONObject();
//        orderRequest.put("amount", totalAmount * 100);
//        orderRequest.put("currency", "INR");
//        Order order = razorpay.orders.create(orderRequest);
//        model.addAttribute("amount", totalAmount * 100);
//        model.addAttribute("orderId", order.get("id"));
//        model.addAttribute("addressId", addressId);
//        model.addAttribute("selectedPaymentMode", selectedPaymentMode);
//        model.addAttribute("totalAmount",totalAmount);
//
//        return "razorPay";

        RazorpayClient razorpay = new RazorpayClient("rzp_test_MNnp3fJToM6Dfn", "un6XvAhAlY3tqSysbaA84lQP");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",totalAmount * 100);
        orderRequest.put("currency","INR");
        Order order = razorpay.orders.create(orderRequest);


        model.addAttribute("amount", totalAmount * 100);
        model.addAttribute("orderId", order.get("id"));
        model.addAttribute("addressId", addressId);
        model.addAttribute("selectedPaymentMode", selectedPaymentMode);
        model.addAttribute("totalAmount",totalAmount);


        return "razorPay";

    }


    @GetMapping("/razorPaySuccess")
    public String success(Model model,
                          @AuthenticationPrincipal(expression = "id") long userId,
                          @RequestParam("addressId") long addressId,
                          @RequestParam("selectedPaymentMode")PaymentMode selectedPaymentMode,
                          @RequestParam("totalAmount") Double totalPrice
    ) {

        Optional<User> optionalUser = userService.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            List<Cart> cartItem = cartService.findByUser_Id(userId);
            Optional<Address> userAddress1 = addressService.findAddressById(addressId);
            Address userAddress = userAddress1.get();
            com.ajmal.TimeCraft.Entity.Order order = orderService.saveOrder(user, cartItem, totalPrice, selectedPaymentMode, userAddress);
            LocalDate expectedDeliveryDate = order.getOrderDate().plusDays(7);
//            variantService.reduceVariantStock(cartItem);
            productService.reduceProductStock(cartItem);
//            userService.deleteCart(userCart);
            cartService.deleteAllCartItems(user,cartItem);

            model.addAttribute("expectedDeliveryDate", expectedDeliveryDate);
            model.addAttribute("order", order);
            return "order-confirmation";

        }
        return "redirect:/login";

    }


}
