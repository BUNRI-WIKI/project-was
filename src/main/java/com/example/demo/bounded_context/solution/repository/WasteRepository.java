package com.example.demo.bounded_context.solution.repository;

import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.solution.entity.ContributedCreationState;
import com.example.demo.bounded_context.solution.entity.Waste;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WasteRepository extends JpaRepository<Waste, Long> {

    /**
     * 반영된 솔루션 이름으로 조회
     * full text search 도입 고민
     */
    @Query("select w.id from Waste w where w.name = :name and w.state = 1")
    Optional<Long> findIdByName(String name);

    /**
     * 사용자의 상태 별 생성 기여 게시물 목록 조회
     * ToOne 으로 Fetch join 사용
     */
    @Query("select w from Waste w join fetch w.writer a where a.id = :accountId and w.state = :state")
    Page<Waste> findByAccountIdAndState(Long accountId, ContributedCreationState state, Pageable pageable);

    /**
     * 모든 솔루션 목록 조회
     * ToOne 으로 Fetch join 사용
     */
    @Query("select w from Waste w join fetch w.writer order by w.createdDate DESC")
    Page<Waste> findAllFetchByPage(Pageable pageable);

    /**
     * 솔루션 상세 조회
     * 1. select query
     * 2. category query
     * 3. tag query
     */
    @Query("select w from Waste w join fetch w.writer join w.categories left join w.tags where w.id = :id")
    Waste findFetchById(Long id);

    /**
     * 카테고리별 솔루션 조회
     * 1. select query (size 로 인해 batch size query 실행하지 않음)
     * 2. paging query
     * 3. batch size tag query
     */
    @Query("select distinct w from Waste w join w.categories m left join w.tags WHERE SIZE(w.categories) = 1 and m.name = :name and w.state = 1")
    Page<Waste> findByCategoriesName(String name, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Waste w set w.writer = null where w.writer = :account")
    void updateWriter(Account account);

    @Query("SELECT DISTINCT w FROM Waste w left join w.tags t WHERE w.name LIKE %:keyword% OR t.name LIKE %:keyword%")
    Page<Waste> findDistinctByWasteNameOrTagNameContaining(String keyword, Pageable pageable);
}
