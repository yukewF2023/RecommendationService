package com.cybermall.backend.service;

import com.cybermall.backend.model.User;
import com.cybermall.backend.repository.UserRepository;
import com.cybermall.backend.repository.ViewHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ViewHistoryRepository viewHistoryRepository;

    public UserService(UserRepository userRepository, ViewHistoryRepository viewHistoryRepository) {
        this.userRepository = userRepository;
        this.viewHistoryRepository = viewHistoryRepository;
    }

    @Transactional
    public void updateUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new RuntimeException("User not found!"));

        int viewCount = viewHistoryRepository.findByUserId(userId).size();
        user.setIsNewUser(viewCount <= 10); // User is not new if they have viewed more than 10 products

        userRepository.save(user);
    }

}
