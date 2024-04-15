// package com.cybermall.backend;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
// public class BackendApplication {

//     public static void main(String[] args) {
//         SpringApplication.run(BackendApplication.class, args);
//     }
// }

package com.cybermall.backend;

import com.cybermall.backend.model.Order;
import com.cybermall.backend.model.Product;
import com.cybermall.backend.model.User;
import com.cybermall.backend.model.ViewHistory;
import com.cybermall.backend.repository.OrderRepository;
import com.cybermall.backend.repository.ProductRepository;
import com.cybermall.backend.repository.UserRepository;
import com.cybermall.backend.repository.ViewHistoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, 
                                   ProductRepository productRepository, 
                                   ViewHistoryRepository viewHistoryRepository, 
                                   OrderRepository orderRepository) {
        return args -> {
            // Create mock users
            User user = userRepository.save(new User("yuketest"));

            // Create mock products
            Product product1 = productRepository.save(new Product("Apple", "Description 1", 10.0, "url1", "fruit"));
            Product product2 = productRepository.save(new Product("Banana", "Description 2", 20.0, "url2", "fruit"));
            
            // Create mock view history
            viewHistoryRepository.save(new ViewHistory(user, product1));
            viewHistoryRepository.save(new ViewHistory(user, product2));

            // Create mock orders
            orderRepository.save(new Order(user, product1));
        };
    }
}
