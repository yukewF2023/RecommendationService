package com.cybermall.backend.repository;

import com.cybermall.backend.model.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing view history data.
 */
@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {

    List<ViewHistory> findByUserId(Long userId);
    Optional<ViewHistory> findByUserIdAndProductId(Long userId, Long productId);

}
