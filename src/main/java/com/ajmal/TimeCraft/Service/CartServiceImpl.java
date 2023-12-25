package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Override
    public List<Cart> findByUser_Email(String email) {
        return cartRepository.findByUser_Email(email);
    }

    @Override
    public Double findTotal(List<Cart> cartItems) {
        Double total = 0.0;  // Initialize total to zero
        for (Cart cart : cartItems) {
            total += cart.getQuantity() * cart.getProduct().getPrice();  // Accumulate total
        }
        return total;
    }

    @Override
    public Cart findCartItem(Optional<User> user, Product product) {
        return cartRepository.findByUserAndProduct(user,product);
    }

    @Override
    public void saveToCart(Cart cartItem) {
        cartRepository.save(cartItem);
    }

    @Override
    public void removeFromCart(Long productId, String email) {
        List<Cart> cartItems = cartRepository.findByUser_EmailAndProduct_Id(email, productId);
        if (!cartItems.isEmpty()) {
            // Find the cart item that matches the product UUID
            Cart cartItemToRemove = null;
            for (Cart cartItem : cartItems) {
                if (cartItem.getProduct().getId().equals(productId)) {
                    cartItemToRemove = cartItem;
                    break; // Exit the loop after finding a match
                }
            }

            if (cartItemToRemove != null) {
                // Remove the found cart item
                cartRepository.delete(cartItemToRemove);
            }
        }
    }

}
