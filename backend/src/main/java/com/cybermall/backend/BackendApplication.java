package com.cybermall.backend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main class for the backend application.
 */
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

        // 4. User APIs
        // UserService userService = new UserService();
        // User user = userService.getUserById(1780429641937326081L);
        // System.out.println("User ID: " + user.getUserId());
        // System.out.println("User Name: " + user.getUserName());

    }
}
