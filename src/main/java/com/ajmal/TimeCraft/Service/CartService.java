package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Entity.User;

import java.util.List;
import java.util.Optional;

public interface CartService {

    List<Cart> findByUser_Email(String email);

    Double findTotal(List<Cart> cart);

    Cart findCartItem(User user, Product product);

    void saveToCart(Cart cartItem);

    public Cart addToCart(User user, Product product, int quantity);

    void removeFromCart(Long productId, String email);



    Optional<Cart> getCartById(String id);


    List<Cart> findByUser_Id(Long userId);

    void deleteCart(Cart userCart);


    void deleteAllCartItems(User user, List<Cart> cartItems);

}
