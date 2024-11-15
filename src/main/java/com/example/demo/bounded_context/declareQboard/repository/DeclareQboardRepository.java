package com.example.demo.bounded_context.declareQboard.repository;

import com.example.demo.bounded_context.declareQboard.entity.DeclareQboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeclareQboardRepository extends JpaRepository<DeclareQboard, Long> {

    @Query(
            value = "SELECT id FROM declare_qboard WHERE board_id = :boardId AND account_id = :accountId  ",
            nativeQuery = true
    )
    Long findByBoardAndAccount(@Param(value="boardId") Long boardId,
                               @Param(value="accountId") Long accountId);

}
