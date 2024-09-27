package com.example.demo.bounded_context.questionBoard.repository;

import com.example.demo.bounded_context.questionBoard.entity.QuestionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {

    @Query("SELECT q FROM QuestionBoard q WHERE q.title LIKE %:title%")
    Page<QuestionBoard> findByTitleContaining(String title, Pageable pageable);

    Page<QuestionBoard> findByNickName(String nickName, Pageable pageable);
}
