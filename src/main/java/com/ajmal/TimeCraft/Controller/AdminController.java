package com.ajmal.TimeCraft.Controller;

import com.ajmal.TimeCraft.DTO.ProductDTO;
import com.ajmal.TimeCraft.Entity.Category;
import com.ajmal.TimeCraft.Entity.Product;
import com.ajmal.TimeCraft.Entity.User;
import com.ajmal.TimeCraft.Service.CategoryService;
import com.ajmal.TimeCraft.Service.ProductService;
import com.ajmal.TimeCraft.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {


    // Adding image for the product to the folder
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";


    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @GetMapping("/admin")
    public String getAdminHome(){

        return "adminHome";
    }



    /* Category Section Start */

    @GetMapping("/admin/categories")
    public String getCategories(Model model) {

        model.addAttribute("categories",categoryService.getAllCategory());
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String addCategories(Model model) {

        model.addAttribute("category",new Category());
        return "add-categories";
    }

    @PostMapping("/admin/categories/add")
    public String postAddCategories(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable long id) {

        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable long id, Model model){
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()){
            model.addAttribute("category",category.get());
            return "add-categories";
        } else {
            return "404";
        }
    }

    /* Category Section End */


    /* Product Section Start */

    @GetMapping("/admin/products")
    public String getProducts(Model model) {

        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String addProduct(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "add-products";
    }

    @PostMapping("/admin/products/add")
    public String postProductsAdd(@ModelAttribute("productDTO")ProductDTO productDTO,
                                  @RequestParam("productImage") MultipartFile file,
                                  @RequestParam("imgName") String imgName) throws IOException {



        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).get());
        product.setPrice(productDTO.getPrice());
//        product.setCount(productDTO.getCount());
        product.setQuantity(productDTO.getQuantity());
        product.setDescription(productDTO.getDescription());
        String imageUUID;
        if (!file.isEmpty()){
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            imageUUID = imgName;
        }
        product.setImageName(imageUUID);
        productService.addProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id) {

        productService.deleteProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable long id, Model model){
        Product product = productService.getProductById(id).get();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
//        productDTO.setCount(product.getCount());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("productDTO", productDTO);

        return "add-products";
    }

    /* Product Section End */

    /* User Section Start */

    @GetMapping("/admin/users")
    public String users(Model model){
        List<User> users = userService.findAllUsers();
        model.addAttribute("users",users);
        return "users";
    }

    // block and unblock
    @GetMapping("/admin/block/{id}")
    public String blockUser(@PathVariable Long id,
                            Principal principal,
                            RedirectAttributes redirectAttributes){
        User user = userService.findById(id).get();
        user.setActive(false);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMsg","User blocked successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/unblock/{id}")
    public String unblockUser(@PathVariable Long id,
                              RedirectAttributes redirectAttributes
    ){

        Optional<User> optionalUser = userService.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setActive(true);
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMsg","user unblocked successfully!");
            return "redirect:/admin/users";
        }else {
            return "redirect:/login";
        }

    }

}
