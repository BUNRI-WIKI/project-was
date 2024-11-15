package com.example.demo.bounded_context.declareQcomment.repository;

import com.example.demo.bounded_context.declareQcomment.entity.DeclareQcomment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeclareQcommentRepository extends JpaRepository<DeclareQcomment, Long> {

    @Query(
            value = "SELECT id FROM declare_qcomment WHERE comment_id = :commentId AND account_id = :accountId  ",
            nativeQuery = true
    )
    Long findByCommentAndAccount(@Param(value="commentId") Long commentId,
                                 @Param(value="accountId") Long accountId);

}