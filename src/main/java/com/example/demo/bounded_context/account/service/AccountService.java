package com.example.demo.bounded_context.account.service;

import com.example.demo.base.exception.CustomException;
import com.example.demo.base.exception.ExceptionCode;
import com.example.demo.bounded_context.account.dto.AccountUpdateRequest;
import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account findByAccountName(String accountName){ //loadByUsername
        return accountRepository.findByAccountName(accountName)
                .orElseThrow(() -> new CustomException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Account findByEmail(String email){ //loadByEmail
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Account findByNickName(String nickName){ //loadByNickName
        return accountRepository.findByNickname(nickName)
                .orElseThrow(() -> new CustomException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Account findByAccountId(Long accountId){
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public Account update(Long accountId, AccountUpdateRequest request){
        Account foundUser = findByAccountId(accountId);
        foundUser.update(request);
        return foundUser;
    }

    @Transactional
    public void delete(Account account){
        accountRepository.delete(account);
    }
}
