package com.example.demo.bounded_context.account.service;

import com.example.demo.base.Resolver.AccessToken;
import com.example.demo.base.blacklist_token.BlacklistTokenService;
import com.example.demo.base.exception.CustomException;
import com.example.demo.base.exception.ExceptionCode;
import com.example.demo.base.refresh_token.RefreshToken;
import com.example.demo.base.refresh_token.RefreshTokenRepository;
import com.example.demo.base.refresh_token.RefreshTokenService;
import com.example.demo.bounded_context.account.dto.AccountInfoRequest;
import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistTokenService blacklistTokenService;

    public Account read(String accountName){ //loadByUsername
        return accountRepository.findByAccountName(accountName)
                .orElseThrow(() -> new CustomException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

    public Account read(Long accountId){
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }

    /**
     * 로그아웃
     * 1. access token 정보로 blacklist token 생성
     * 2. refresh token 삭제
     */
    public void signOut(AccessToken accessToken, String refreshToken){
        RefreshToken refresh = refreshTokenService.read(refreshToken);
        if(refresh.getAccountId() != accessToken.getAccountId()){
            throw new CustomException(ExceptionCode.INVALID_SIGN_OUT);
        }
        blacklistTokenService.create(accessToken);
        refreshTokenRepository.delete(refresh);
    }

    /**
     * 회원탈퇴
     * 1. refresh token 삭제
     * 2. account 삭제 - account 를 조회할 수 없으므로 access token 정보로 blacklist token 생성하지 않아도 된다.
     */
    public void withdrawal(AccessToken accessToken, String refreshToken){
        RefreshToken refresh = refreshTokenService.read(refreshToken);
        if(refresh.getUserId() != accessToken.getAccountId()){
            throw new CustomException(ExceptionCode.INVALID_SIGN_OUT);
        }
        refreshTokenRepository.delete(refresh);
        remove(refresh.getUserId());
    }

    public Account update(Long accountId, AccountInfoRequest request){
        Account account = read(accountId);
        return accountRepository.save(account.update(request));
    }

    public Account remove(Long accountId){
        Account account = read(accountId);
        accountRepository.delete(account);
        return account;
    }
}
