package com.example.demo.bounded_context.account.repository;

import com.example.demo.bounded_context.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountName(String accountName);

    Optional<Account> findByNickname(String nickName);

    Optional<Account> findByEmail(String email);

    @Query("select a from Account a left join fetch a.wikis where a.id = :accountId")
    Optional<Account> findFetchByAccountId(Long accountId);
}
