package com.example.demo.bounded_context.account.controller;

import com.example.demo.base.Resolver.AccessToken;
import com.example.demo.base.Resolver.AuthorizationHeader;
import com.example.demo.base.common.AuthConstants;
import com.example.demo.bounded_context.account.dto.AccountResponse;
import com.example.demo.bounded_context.account.dto.AccountUpdateRequest;
import com.example.demo.bounded_context.account.repository.AccountRepository;
import com.example.demo.bounded_context.account.service.AccountUseCase;
import com.example.demo.bounded_context.solution.dto.ContributeCreationListResponse;
import com.example.demo.bounded_context.solution.entity.ContributedCreationState;
import com.example.demo.bounded_context.solution.service.SolutionUseCase;
import com.example.demo.bounded_context.wiki.dto.ContributeModificationsResponse;
import com.example.demo.bounded_context.wiki.entity.WikiState;
import com.example.demo.bounded_context.wiki.service.WikiUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final AccountUseCase accountUseCase;
    private final SolutionUseCase solutionUseCase;
    private final WikiUseCase wikiUseCase;
    private final AccountRepository accountRepository;

    @PostMapping("/sign-out")
    @Operation(summary = "로그아웃", description = "access / refresh token 을 사용한 로그아웃")
    public ResponseEntity<?> signOut(@AuthorizationHeader AccessToken accessToken,
                                  @CookieValue(AuthConstants.REFRESH_TOKEN) String refreshToken) {
        accountUseCase.signOut(accessToken, refreshToken);
        return ResponseEntity.ok().body("로그아웃에 성공했습니다.");
    }

    @PostMapping("/duplicate-test/id")
    @Operation(summary = "아이디 중복검사", description = "아이디 중복검사")
    public ResponseEntity<?> testId(@RequestBody String id) {
        if(accountRepository.findByAccountName(id).isEmpty())
            return ResponseEntity.ok().body("사용가능한 아이디 입니다.");
        else
            return ResponseEntity.ok().body("중복된 아이디 입니다.");
    }

    @PostMapping("/duplicate-test/email")
    @Operation(summary = "이메일 중복검사", description = "이메일 중복검사")
    public ResponseEntity<?> testEmail(@RequestBody String email) {
        if(accountRepository.findByEmail(email).isEmpty())
            return ResponseEntity.ok().body("사용가능한 이메일 입니다.");
        else
            return ResponseEntity.ok().body("중복된 이메일 입니다.");
    }


    @PostMapping("/duplicate-test/nickname")
    @Operation(summary = "닉네임 중복검사", description = "닉네임 중복검사")
    public ResponseEntity<?> testNickName(@RequestBody String NickName) {
        if(accountRepository.findByNickname(NickName).isEmpty())
            return ResponseEntity.ok().body("사용가능한 닉네임 입니다.");
        else
            return ResponseEntity.ok().body("중복된 닉네임 입니다.");
    }

    @DeleteMapping("/withdrawal")
    @Operation(summary = "회원 탈퇴", description = "access / refresh token 을 사용한 회원 탈퇴")
    public ResponseEntity<?> withdrawal(@AuthorizationHeader AccessToken accessToken,
                                         @CookieValue(AuthConstants.REFRESH_TOKEN) String refreshToken) {
        accountUseCase.withdrawal(accessToken, refreshToken);
        return ResponseEntity.ok().body("회원탈퇴에 성공했습니다.");
    }

    @GetMapping("/me")
    @Operation(summary = "정보 조회", description = "로그인한 사용자는 자신의 정보를 조회할 수 있다.")
    public ResponseEntity<AccountResponse> readMe(@AuthorizationHeader Long id) {
        AccountResponse account = accountUseCase.read(id);
        return ResponseEntity.ok().body(account);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "정보 조회", description = "모든 사용자는 다른 사용자의 정보를 조회할 수 있다.")
    public ResponseEntity<AccountResponse> readOther(@PathVariable Long accountId) {
        AccountResponse account = accountUseCase.read(accountId);
        return ResponseEntity.ok().body(account);
    }

    @PutMapping("/me")
    @Operation(summary = "정보 수정", description = "access token 을 사용한 사용자 정보 수정")
    public ResponseEntity<?> update(@AuthorizationHeader Long id, @RequestBody AccountUpdateRequest request) {
        Long updateAccountId = accountUseCase.update(id, request);
        return ResponseEntity.ok().body(updateAccountId);
    }

    @GetMapping("/{accountId}/contributions/creation")
    @Operation(summary = "사용자 생성 요청 목록 조회", description = "모든 사용자는 자신을 포함한 다른 사용자들의 생성 요청 목록을 조회할 수 있다.")
    private ResponseEntity<?> readContributeCreation(@PathVariable Long accountId,
                                                       @RequestParam(name = "state", defaultValue = "pending") String state,
                                                       @PageableDefault(page = 0, size = 10) Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<ContributeCreationListResponse> creations =
                solutionUseCase.readContributeCreations(accountId, ContributedCreationState.valueOf(state.toUpperCase()), pageable);
        return ResponseEntity.ok(creations);
    }

    @GetMapping("/{accountId}/contributions/modification")
    @Operation(summary = "사용자 수정 요청 목록 조회", description = "모든 사용자는 자신을 포함한 다른 사용자들의 수정 요청 목록을 조회할 수 있다.")
    private ResponseEntity<?> readContributeModifications(@PathVariable Long accountId,
                                                       @RequestParam(name = "state", defaultValue = "pending") String state,
                                                       @PageableDefault(page = 0, size = 10) Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<ContributeModificationsResponse> modifications =
                wikiUseCase.readContributeModifications(accountId, WikiState.valueOf(state.toUpperCase()), pageable);
        return ResponseEntity.ok(modifications);
    }
}
