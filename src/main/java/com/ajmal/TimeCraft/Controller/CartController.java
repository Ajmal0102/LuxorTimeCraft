package com.ajmal.TimeCraft.Controller;

import com.ajmal.TimeCraft.Entity.Address;
import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Service.AddressService;
import com.ajmal.TimeCraft.Service.CartService;
import com.ajmal.TimeCraft.Service.ProductService;
import com.ajmal.TimeCraft.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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

//    @GetMapping("/add-to-cart/{id}")
//    public String addToCart(@PathVariable("id") Long productId,
//                            @AuthenticationPrincipal(expression = "email") String email,
//                            Model model){
//
//        Product product = productService.getProductById(productId).orElse(null);
//        System.out.println(product);
//
//
//        Optional<User> user = userService.findUserByEmail(email);
//
//        User user1=user.get();
//
//        int currentStock = product.getQuantity();
//
//        if (currentStock>=1) {
//            Cart existingCartItem = cartService.findCartItem(user1, product);
//
//            System.out.println("eeeeeeeeeeeeeee"+existingCartItem);
//            System.out.println("User: " + user);
//            System.out.println("Product: " + product);
//            if (existingCartItem != null) {
//                System.out.println("Cart is not null");
//                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
//                cartService.saveToCart(existingCartItem);
//            } else {
//
//                Cart cartItem = new Cart();
//                cartItem.setProduct(product);
//                cartItem.setUser(user1);
//                cartItem.setQuantity(1);
//                cartService.saveToCart(cartItem);
//            }
//        }
//        return "redirect:/cart/view-cart";
//    }

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
}
