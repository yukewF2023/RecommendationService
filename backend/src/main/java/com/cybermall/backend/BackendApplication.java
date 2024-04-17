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

    @SuppressWarnings("unused")
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, 
                                   ProductRepository productRepository, 
                                   ViewHistoryRepository viewHistoryRepository, 
                                   OrderRepository orderRepository) {
        return args -> {
            // Create mock users
            User user1 = userRepository.save(new User("yuketest1"));
            User user2 = userRepository.save(new User("yuketest2"));
            User user3 = userRepository.save(new User("yuketest3"));
            System.out.println("Created user with ID: " + user1.getUserId());
            System.out.println("Created user with ID: " + user2.getUserId());
            System.out.println("Created user with ID: " + user3.getUserId());

            // Create mock products
            Product product1 = productRepository.save(new Product("Apple", "Description 1", 10.0, "url1", "fruit"));
            Product product2 = productRepository.save(new Product("Banana", "Description 2", 20.0, "url2", "fruit"));
            // Create 20 more products
            Product book1 = productRepository.save(new Product("Book 1", "Book Description 1", 15.0, "url3", "book"));
            Product book2 = productRepository.save(new Product("Book 2", "Book Description 2", 25.0, "url4", "book"));
            Product book3 = productRepository.save(new Product("Book 3", "Book Description 3", 30.0, "url5", "book"));
            Product book4 = productRepository.save(new Product("Book 4", "Book Description 4", 35.0, "url6", "book"));
            Product book5 = productRepository.save(new Product("Book 5", "Book Description 5", 40.0, "url7", "book"));

            Product vegetable1 = productRepository.save(new Product("Vegetable 1", "Vegetable Description 1", 5.0, "url8", "vegetable"));
            Product vegetable2 = productRepository.save(new Product("Vegetable 2", "Vegetable Description 2", 7.0, "url9", "vegetable"));
            Product vegetable3 = productRepository.save(new Product("Vegetable 3", "Vegetable Description 3", 8.0, "url10", "vegetable"));
            Product vegetable4 = productRepository.save(new Product("Vegetable 4", "Vegetable Description 4", 6.0, "url11", "vegetable"));
            Product vegetable5 = productRepository.save(new Product("Vegetable 5", "Vegetable Description 5", 9.0, "url12", "vegetable"));

            Product dairy1 = productRepository.save(new Product("Dairy 1", "Dairy Description 1", 3.0, "url13", "dairy"));
            Product dairy2 = productRepository.save(new Product("Dairy 2", "Dairy Description 2", 4.0, "url14", "dairy"));
            Product dairy3 = productRepository.save(new Product("Dairy 3", "Dairy Description 3", 2.0, "url15", "dairy"));
            Product dairy4 = productRepository.save(new Product("Dairy 4", "Dairy Description 4", 5.0, "url16", "dairy"));
            Product dairy5 = productRepository.save(new Product("Dairy 5", "Dairy Description 5", 6.0, "url17", "dairy"));

            Product meat1 = productRepository.save(new Product("Meat 1", "Meat Description 1", 12.0, "url18", "meat"));
            Product meat2 = productRepository.save(new Product("Meat 2", "Meat Description 2", 15.0, "url19", "meat"));
            Product meat3 = productRepository.save(new Product("Meat 3", "Meat Description 3", 18.0, "url20", "meat"));
            Product meat4 = productRepository.save(new Product("Meat 4", "Meat Description 4", 20.0, "url21", "meat"));
            Product meat5 = productRepository.save(new Product("Meat 5", "Meat Description 5", 22.0, "url22", "meat"));;

            // create 10 more products, maybe 3 kitchen, 3 electronics, 4 clothing
            Product kitchen1 = productRepository.save(new Product("Kitchen 1", "Kitchen Description 1", 10.0, "url23", "kitchen"));
            Product kitchen2 = productRepository.save(new Product("Kitchen 2", "Kitchen Description 2", 20.0, "url24", "kitchen"));
            Product kitchen3 = productRepository.save(new Product("Kitchen 3", "Kitchen Description 3", 30.0, "url25", "kitchen"));

            Product electronics1 = productRepository.save(new Product("Electronics 1", "Electronics Description 1", 40.0, "url26", "electronics"));
            Product electronics2 = productRepository.save(new Product("Electronics 2", "Electronics Description 2", 50.0, "url27", "electronics"));
            Product electronics3 = productRepository.save(new Product("Electronics 3", "Electronics Description 3", 60.0, "url28", "electronics"));
            
            Product clothing1 = productRepository.save(new Product("Clothing 1", "Clothing Description 1", 70.0, "url29", "clothing"));
            Product clothing2 = productRepository.save(new Product("Clothing 2", "Clothing Description 2", 80.0, "url30", "clothing"));
            Product clothing3 = productRepository.save(new Product("Clothing 3", "Clothing Description 3", 90.0, "url31", "clothing"));
            Product clothing4 = productRepository.save(new Product("Clothing 4", "Clothing Description 4", 100.0, "url32", "clothing"));
        };
    }
}
