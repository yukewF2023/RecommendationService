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

import com.cybermall.backend.model.ViewHistory;
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
    CommandLineRunner commandLineRunner(ViewHistoryRepository viewHistoryRepository) {
        return args -> {
            // Inserting a couple of view histories
            ViewHistory viewHistory1 = new ViewHistory(1L, 101L);
            ViewHistory viewHistory2 = new ViewHistory(1L, 102L);
            
            // Save these to the database
            viewHistoryRepository.save(viewHistory1);
            viewHistoryRepository.save(viewHistory2);
        };
    }
}
