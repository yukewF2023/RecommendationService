package com.cybermall.backend.service;

import com.cybermall.backend.model.User;
import com.cybermall.backend.repository.*;

import java.util.Optional;

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

    public Optional<User> findById(Long userId) {
        return this.userRepository.findById(userId);
    }

}
