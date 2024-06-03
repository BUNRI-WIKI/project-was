package com.example.demo.bounded_context.account.dto;

import com.example.demo.bounded_context.account.entity.Account;

public record AccountResponse(
        String accountName,
        String email,
        String nickname,
        Double latitude,
        Double longitude
) {

    public static AccountResponse fromEntity(Account account){
        return new AccountResponse(
                account.getAccountName(),
                account.getEmail(),
                account.getNickname(),
                account.getLatitude(),
                account.getLongitude()
                );
    }
}