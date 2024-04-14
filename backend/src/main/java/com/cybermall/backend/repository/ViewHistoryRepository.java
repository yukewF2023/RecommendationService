package com.cybermall.backend.repository;

import com.cybermall.backend.model.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {

    List<ViewHistory> findByUserId(Long userId);
    List<ViewHistory> findByProductId(Long productId);
}
