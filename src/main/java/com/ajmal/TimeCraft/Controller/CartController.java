package com.ajmal.TimeCraft.Controller;

import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Service.CartService;
import com.ajmal.TimeCraft.Service.ProductService;
import com.ajmal.TimeCraft.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                            Model model) {

        Product product = productService.getProductById(productId).orElse(null);

        Optional<User> userOptional = userService.findUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            int currentStock = product.getQuantity();

            if (currentStock >= 1) {
                Cart existingCartItem = cartService.findCartItem(Optional.of(user), product);

                if (existingCartItem != null) {
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                    cartService.saveToCart(existingCartItem);
                } else {
                    Cart cartItem = new Cart();
                    cartItem.setProduct(product);
                    cartItem.setUser(user);
                    cartItem.setQuantity(1);
                    cartService.saveToCart(cartItem);
                }
            }
        }

        return "redirect:/cart/view-cart";
    }



    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable("id") Long productId,
                                 @AuthenticationPrincipal(expression = "email") String email) {

        cartService.removeFromCart(productId,email);

        return "redirect:/cart/view-cart";
    }
}
