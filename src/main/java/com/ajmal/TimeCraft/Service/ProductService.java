package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.Category;
import com.ajmal.TimeCraft.Entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();

    void addProduct(Product product);

    void unlistProductById(long id);

    Optional<Product> getProductById(long id);

    List<Product> getAllProductsByCategoryId(long id);

    Optional<Product> findById(Long productId);

    void reduceProductStock(List<Cart> cartItems);

    void listProductById(long id);

}
