package com.example.demo.bounded_context.recommendComment.repository;

import com.example.demo.bounded_context.recommendComment.entity.RecommendComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendCommentRepository extends JpaRepository<RecommendComment, Long> {

    @Query(
            value = "SELECT id FROM recommend_comment WHERE comment_id = :commentId AND account_id = :accountId  ",
            nativeQuery = true
    )
    Long findByCommentAndAccount(@Param(value="commentId") Long commentId,
                               @Param(value="accountId") Long accountId);

}
