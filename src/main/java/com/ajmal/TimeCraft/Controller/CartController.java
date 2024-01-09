package com.ajmal.TimeCraft.Controller;

import com.ajmal.TimeCraft.Entity.*;
import com.ajmal.TimeCraft.Entity.EnumList.PaymentMode;
import com.ajmal.TimeCraft.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @Autowired
    OrderService orderService;

    @GetMapping("/view-cart")
    public String viewCart(Model model, @AuthenticationPrincipal(expression = "email") String email){

        List<Cart> cartItems = cartService.findByUser_Email(email);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findUserByEmail(email);

        if (authentication == null) {
            return "redirect:/login";
        }

        model.addAttribute("total", cartService.findTotal(cartItems));
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", user.orElse(null));

        return "cart";
    }

    @GetMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") Long productId,
                            @AuthenticationPrincipal(expression = "email") String email,
                            @RequestParam(name = "quantity", defaultValue = "1") int quantity,
                            Model model) {

        Product product = productService.getProductById(productId).orElse(null);
        Optional<User> userOptional = userService.findUserByEmail(email);

        if (userOptional.isPresent() && product != null) {
            User user = userOptional.get();
            cartService.addToCart(user, product, quantity);
        }

        return "redirect:/cart/view-cart";
    }



    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable("id") Long productId,
                                 @AuthenticationPrincipal(expression = "email") String email) {

        cartService.removeFromCart(productId,email);

        return "redirect:/cart/view-cart";
    }

    @PostMapping("/update-cart")
    public String itemQuantityUpdate(@RequestParam String id,
                                     @RequestParam int newQuantity,
                                     @AuthenticationPrincipal(expression = "email") String email){

        Optional<Cart> optionalCartItem = cartService.getCartById(id);
        if (newQuantity == 0) {
            cartService.removeFromCart(optionalCartItem.get().getProduct().getId(),email);

        } else if (optionalCartItem.isPresent()) {

            Cart cartItem = optionalCartItem.get();
            cartItem.setQuantity(newQuantity);
            cartService.saveToCart(cartItem);
        }




        return "redirect:/cart/view-cart";
    }

    @GetMapping("/checkout")
    public String getCheckout(Model model,@AuthenticationPrincipal(expression = "id") Long user_id,
                              RedirectAttributes redirectAttributes){



//        Optional<User> optionalUser = userService.findById(user_id);
//
//        if(optionalUser.isPresent()){
//            User user =optionalUser.get();
//            List<Address> addressList = addressService.findAllUserAddresses(user);
//
//
//            model.addAttribute("Addresses",addressList);
//            model.addAttribute("user",user);
//
//
//            Cart cart = user.getCart();
//
//            List<CartItems> cartItems = cart.getCartItems();
//            model.addAttribute("cartItems",cartItems);
//
//            double totalPrice = cartItems.stream()
//                    .mapToDouble(cartItem -> cartItem.getVariant().getPrice() * cartItem.getQuantity())
//                    .sum();
//            double totalPrice = cart.getTotal();
//            model.addAttribute("totalPrice",totalPrice);
//
//        }

        List<Cart> cartItems = cartService.findByUser_Id(user_id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Optional<User> user = userService.findById(user_id);
        Optional<User> optionalUser = userService.findById(user_id);



        if (authentication == null) {
            return "redirect:/login";
        }


        if(optionalUser.isPresent()){
            User user =optionalUser.get();
            List<Address> addressList = addressService.findAllUserAddresses(user);


            model.addAttribute("Addresses",addressList);
            model.addAttribute("user",user);


//            Cart cart = user.getCart();
//
//            List<CartItems> cartItems = cart.getCartItems();
//            model.addAttribute("cartItems",cartItems);

//            double totalPrice = cartItems.stream()
//                    .mapToDouble(cartItem -> cartItem.getVariant().getPrice() * cartItem.getQuantity())
//                    .sum();
            double totalPrice = cartService.findTotal(cartItems);
            model.addAttribute("totalPrice",totalPrice);

            model.addAttribute("total", cartService.findTotal(cartItems));
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("user", user);

        }



        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(Model model,
                             @RequestParam(value = "selectedAddressId", required = false) Long addressId,
                             @RequestParam(value = "paymentMethod", required = false) PaymentMode selectedPaymentMode,
                             @AuthenticationPrincipal(expression = "id") Long user_id,
                             Principal principal, RedirectAttributes redirectAttributes) {

        if (addressId == null) {
            model.addAttribute("errorAddress", "Please select an address.");
            return "redirect:/cart/checkout";
        }

        if (principal != null) {
            Optional<User> optionalUser= userService.findById(user_id);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                List<Cart> cartItems = cartService.findByUser_Id(user_id);

                double totalPrice = cartService.findTotal(cartItems);


                Optional<Address> optionalUserAddress = addressService.findAddressById(addressId);
                if (optionalUserAddress.isPresent()) {
                    Address userAddress = optionalUserAddress.get();
                    try {



                        if (isCod(selectedPaymentMode)) {
                            Order order = orderService.saveOrder(user, cartItems, totalPrice, selectedPaymentMode, userAddress);
                            LocalDate expectedDeliveryDate = order.getOrderDate().plusDays(7);
                            handleCodPayment(model, user, cartItems, order, expectedDeliveryDate);
                            return "order-confirmation";
                        } else if (isRazorpay(selectedPaymentMode)) {
                            return "redirect:/payment/" + totalPrice + "?addressId=" + addressId + "&selectedPaymentMode=" + selectedPaymentMode;

                        }
                        else {
                            redirectAttributes.addFlashAttribute("error", "payment method not selected");
                            return "redirect:/cart/checkout";
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return "redirect:/login";
    }






    private static boolean isCod(PaymentMode selectedPaymentMode) {
        return selectedPaymentMode == PaymentMode.COD;
    }

    private  static  boolean isRazorpay(PaymentMode selectedPaymentMode){
        return selectedPaymentMode == PaymentMode.RAZORPAY;
    }

    private static  boolean isWalletPay(PaymentMode selectedPaymentMode){
        return selectedPaymentMode == PaymentMode.WALLET;
    }

    private void handleCodPayment(Model model,
                                  User user,
                                  List<Cart> cartItems,
                                  Order order,
                                  LocalDate expectedDeliveryDate) {

        productService.reduceProductStock(cartItems);
        cartService.deleteAllCartItems(user, cartItems);

        model.addAttribute("order", order);
        model.addAttribute("expectedDeliveryDate", expectedDeliveryDate);


    }

}
