package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Entity.User;

import java.util.List;
import java.util.Optional;

public interface CartService {

    List<Cart> findByUser_Email(String email);

    Double findTotal(List<Cart> cart);

    Cart findCartItem(Optional<User> user, Product product);

    void saveToCart(Cart cartItem);

    void removeFromCart(Long productId, String email);

}
