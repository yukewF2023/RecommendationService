package com.cybermall.backend;

import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.User;
import com.cybermall.backend.model.ViewHistory;
import com.cybermall.backend.repository.ViewHistoryRepository;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.cybermall.backend.service.*;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        // Test the API calls
        // 1. getProductById
        // ProductService productService = new ProductService();
        // Long productId = 1781548549725499394L;
        // Product example = productService.getProductById(productId);
        // System.out.println("Product ID: " + example.getProductId());
        // System.out.println("Product Name: " + example.getName());
        // System.out.println("Product Description: " + example.getDescription());

        // 2. getAllProducts
        // List<Product> products = productService.getAllProducts();
        // System.out.println("Number of products: " + products.size());
        // print every product's description
        // for (Product product : products) {
        //     System.out.println("Product ID: " + product.getProductId());
        //     System.out.println("Product Name: " + product.getName());
        //     System.out.println("Product Description: " + product.getDescription());
        // }

        // 3. updateProductView
        // productService.updateProductView(1781548549725499394L, 10);

        // 4. loginUser
        // UserService userService = new UserService();
        // userService.loginUser("test", "testtest");
        // User user = userService.getCurrentLoggedInUser();
        // System.out.println("User ID: " + user.getUserId());
        // System.out.println("User Name: " + user.getUserName());

    }
}
