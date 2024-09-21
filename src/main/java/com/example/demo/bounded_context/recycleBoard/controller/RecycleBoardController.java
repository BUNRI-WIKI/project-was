package com.example.demo.bounded_context.recycleBoard.controller;

import com.example.demo.base.Resolver.AuthorizationHeader;
import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.account.service.AccountService;
import com.example.demo.bounded_context.questionBoard.dto.CreateQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.PageQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.ReadQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.UpdateQuestionBoardDto;
import com.example.demo.bounded_context.recycleBoard.dto.CreateRecycleBoardDto;
import com.example.demo.bounded_context.recycleBoard.dto.PageRecycleBoardDto;
import com.example.demo.bounded_context.recycleBoard.dto.ReadRecycleBoardDto;
import com.example.demo.bounded_context.recycleBoard.dto.UpdateRecycleBoardDto;
import com.example.demo.bounded_context.recycleBoard.service.RecycleBoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recycleBoard")
public class RecycleBoardController {

    private final RecycleBoardService recycleBoardService;
    private final AccountService accountService;

    @PostMapping("/create") // - 로그인한 사용자는 게시글을 작성할 수 있다
    @Operation(summary = "나눔 게시글 생성", description = "로그인시 나눔 게시글 생성 가능")
    public ResponseEntity<?> create(@AuthorizationHeader Long id,
                                    @RequestBody CreateRecycleBoardDto createRecycleBoardDto){
        Account writer = accountService.findByAccountId(id);; // writer가 현재 누구
        recycleBoardService.create(createRecycleBoardDto, writer);

        return ResponseEntity.ok("게시글 생성 완료");
    }

    @GetMapping("/read/{recycleBoardId}") // - 게시글 자세히 보기
    @Operation(summary = "나눔 게시글 불러오기", description = "댓글을 포함한 나눔 게시글 부름")
    public ResponseEntity<ReadRecycleBoardDto> readId(@PathVariable Long recycleBoardId) {
        ReadRecycleBoardDto readRecycleBoardDto = recycleBoardService.read(recycleBoardId);

        return ResponseEntity.ok(readRecycleBoardDto);
    }

    // @PageableDefault(page = 1) : page는 기본으로 1페이지를 보여준다.
    @GetMapping("/read/{option}/paging")
    @Operation(summary = "나눔 게시글 페이징", description = "/read/{option}/paging?page=번호(1~),{option}1-최신순,2-조회순")
    public ResponseEntity<?> readPage(@PageableDefault(page = 1) Pageable pageable,
                                      @PathVariable Integer option) {
        Page<PageRecycleBoardDto> boardPages = recycleBoardService.paging(pageable,option);

        return ResponseEntity.ok(boardPages);
    }

    @GetMapping("/search/{option}/paging")
    @Operation(summary = "나눔 게시글 검색", description = "/search/{option}/paging?page=번호(1~),{option}1-최신순,2-조회순")
    public ResponseEntity<?> searchPage(@PageableDefault(page = 1) Pageable pageable,
                                        @PathVariable Integer option,
                                        @RequestParam("keyword") String keyword) {
        if(keyword==null)
            return ResponseEntity.ok("검색할 키워드를 입력해주세요");

        Page<PageRecycleBoardDto> boardPages = recycleBoardService.search(pageable,option,keyword);

        return ResponseEntity.ok(boardPages);
    }

    @PutMapping("/update/{recycleBoardId}") // - 게시글 업데이트
    @Operation(summary = "나눔 게시글 수정", description = "자신이 작성한 나눔 게시글 수정")
    public ResponseEntity<?> update(@AuthorizationHeader Long id,
                                    @PathVariable Long recycleBoardId,
                                    @RequestBody UpdateRecycleBoardDto updateRecycleBoardDto) {
        Account user = accountService.findByAccountId(id);
        if(recycleBoardService.readWriter(recycleBoardId)==user){
            recycleBoardService.update(recycleBoardId,updateRecycleBoardDto);
            return ResponseEntity.ok("게시글 수정완료");
        }
        else{
            return ResponseEntity.ok("권한이 없습니다");
        }
    }

    @GetMapping("/delete/{recycleBoardId}")  // - 게시글 삭제
    @Operation(summary = "나눔 게시글 삭제", description = "자신이 작성한 나눔 게시글 삭제")
    public ResponseEntity<?> delete(@AuthorizationHeader Long id, @PathVariable Long recycleBoardId) {
        Account user = accountService.findByAccountId(id);
        if(recycleBoardService.readWriter(recycleBoardId)==user){
            recycleBoardService.delete(recycleBoardId);
            return ResponseEntity.ok("게시글 삭제완료");
        }
        else{
            return ResponseEntity.ok("권한이 없습니다");
        }
    }

    @PutMapping("/finish/{recycleBoardId}") // - 나눔여부 변경
    @Operation(summary = "나눔 여부 완료로 변경", description = "자신이 작성한 나눔 게시글의 나눔여부 완료로 변경")
    public ResponseEntity<?> finish(@AuthorizationHeader Long id,
                                    @PathVariable Long recycleBoardId) {
        Account user = accountService.findByAccountId(id);
        if(recycleBoardService.readWriter(recycleBoardId)==user){
            recycleBoardService.finish(recycleBoardId);
            return ResponseEntity.ok("나눔 완료");
        }
        else{
            return ResponseEntity.ok("권한이 없습니다");
        }
    }

}
