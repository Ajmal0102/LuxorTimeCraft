package com.ajmal.TimeCraft.Service;

import com.ajmal.TimeCraft.Entity.Cart;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProductById(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProductsByCategoryId(long id) {
        return productRepository.findAllByCategory_Id(id);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public void reduceProductStock(List<Cart> cartItems) {

        for (Cart cartItem: cartItems) {
            Product product = cartItem.getProduct();
            int orderQuantity = cartItem.getQuantity();
            int currentStock = product.getQuantity();
            if(currentStock >= orderQuantity){
                product.setQuantity(currentStock-orderQuantity);
            }else {
                new RuntimeException("out of stock");
            }
            productRepository.save(product);

        }
    }
}
