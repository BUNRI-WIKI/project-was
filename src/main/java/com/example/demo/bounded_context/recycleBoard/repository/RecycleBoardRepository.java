package com.example.demo.bounded_context.recycleBoard.repository;

import com.example.demo.bounded_context.recycleBoard.entity.RecycleBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecycleBoardRepository extends JpaRepository<RecycleBoard, Long> {

    Page<RecycleBoard> findByTitleContaining(String title, Pageable pageable);

    Page<RecycleBoard> findByNickName(String nickName, Pageable pageable);
}
