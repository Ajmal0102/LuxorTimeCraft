package com.ajmal.TimeCraft.Repository;

import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    List<Cart> findByUser_EmailAndProduct_Id(String email, Long productId);

    List<Cart> findByUser_Email(String email);

//    Cart findByUserAndProduct(Optional<User> user, Product product);

        Cart findByUserAndProduct(User user, Product product);

}
