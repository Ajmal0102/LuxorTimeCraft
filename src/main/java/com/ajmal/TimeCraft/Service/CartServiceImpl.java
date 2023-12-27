package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.event.ListDataEvent;
import java.util.Arrays;
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
    public Cart findCartItem(User user, Product product) {
        return null;
    }

    public Cart addToCart(User user, Product product, int quantity) {
        Cart existingCartItem = cartRepository.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
            // Cart item already exists, update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            return cartRepository.save(existingCartItem);
        } else {
            // Cart item doesn't exist, create a new one
            Cart newCartItem = new Cart();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            return cartRepository.save(newCartItem);
        }
    }



//    @Override
//    public Cart findCartItem(User userOptional, Product product) {
//        User user = userOptional;
//
//        System.out.println("User in findCartItem: " + user.getId()+"name :"+user.getFirstName());
//        System.out.println("Product in findCartItem: " + product);
//
//     Cart cart=cartRepository.findByUserAndProduct(user, product);
//     try {
//         System.out.println("Cart is ...." + cart);
//     }catch (Exception e){
//         System.out.println("Cart is null");
//         e.printStackTrace();
//
//     }
//        return cart;
//    }

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

    @Override
    public void updateCartItemQuantity(String email, Long id, int newQuantity) {

//        Cart existingCartItem = cartRepository.findByUserAndProduct(user, cartRepository.findByProductId(id));

//        existingCartItem.setQuantity(newQuantity);
//        cartRepository.save(existingCartItem);
//        Product product = cartRepository.findByProductId(id);

//        List<Cart> existingCartItem = cartRepository.findByUser_EmailAndProduct_Id(email,id);

//        Cart existingCartItem = cartRepository.findByUserAndProduct(user, cartRepository.findByProductId(id));
//        existingCartItem.setQuantity(newQuantity);
//        cartRepository.save(existingCartItem);

    }

    @Override
    public Optional<Cart> getCartById(String id) {
        return cartRepository.findById(id);
    }



}
