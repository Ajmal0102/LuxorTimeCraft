package com.ajmal.TimeCraft.Controller;

import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Service.CategoryService;
import com.ajmal.TimeCraft.Service.ProductService;
import com.ajmal.TimeCraft.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @GetMapping({"/","/home"})
    public String home(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }
    @GetMapping("/home/category/{id}")
    public String shopByCategoryHome(@PathVariable int id, Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByCategoryId(id));
        return "index";
    }
    @GetMapping("/shop")
    public String shop(Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProducts());
        return "shop";
    }
    @GetMapping("/shop/category/{id}")
    public String shopByCategory(@PathVariable int id, Model model){
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByCategoryId(id));
        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(@PathVariable int id,Model model){
        model.addAttribute("product", productService.getProductById(id).get());
        return "view-product";
    }

    @GetMapping("/user/user-dashboard")
    public String userProfile(Model model, Principal principal) {

        Optional<User> optionalUser = userService.findUserByEmail(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("userProfile",user);
            return "user-dashboard";
        } else {
            return "redirect:/login";
        }

    }

}
