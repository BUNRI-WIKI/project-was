package com.example.demo.bounded_context.questionBoard.controller;

import com.example.demo.base.Resolver.AuthorizationHeader;
import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.account.service.AccountService;
import com.example.demo.bounded_context.questionBoard.dto.CreateQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.PageQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.ReadQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.UpdateQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.service.QuestionBoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questionBoard")
public class QuestionBoardController {
    private final QuestionBoardService questionBoardService;
    private final AccountService accountService;

    @PostMapping("/create") // - 로그인한 사용자는 게시글을 작성할 수 있다
    @Operation(summary = "Q&A 게시글 생성", description = "로그인시 Q&A 게시글 생성 가능")
    public ResponseEntity<?> create(@AuthorizationHeader Long id,
                                    @RequestBody CreateQuestionBoardDto createQuestionBoardDto){
        Account writer = accountService.findByAccountId(id);; // writer가 현재 누구
        questionBoardService.create(createQuestionBoardDto, writer);

        return ResponseEntity.ok("게시글 생성 완료");
    }

    // 게시글 목록(리스트) 보기(페이징 필요?)

    @GetMapping("/read/{questionBoardId}") // - 게시글 자세히 보기, (댓글-페이지네이션 필요?)
    @Operation(summary = "Q&A 게시글 불러오기", description = "댓글을 포함한 게시글 부름")
    public ResponseEntity<ReadQuestionBoardDto> readId(@PathVariable Long questionBoardId) {
        ReadQuestionBoardDto readQuestionBoardDto = questionBoardService.read(questionBoardId);

        return ResponseEntity.ok(readQuestionBoardDto);
    }

    // @PageableDefault(page = 1) : page는 기본으로 1페이지를 보여준다.
    @GetMapping("/read/{option}/paging")
    @Operation(summary = "Q&A 게시글 페이징", description = "/read/option/paging?page=번호(1~),{option}1-최신순,2-추천순,3-조회순")
    public ResponseEntity<?> readPage(@PageableDefault(page = 1) Pageable pageable,
                                      @PathVariable Integer option) {
        Page<PageQuestionBoardDto> boardPages = questionBoardService.paging(pageable,option);

        return ResponseEntity.ok(boardPages);
    }

    @GetMapping("/search/{option}/paging")
    @Operation(summary = "Q&A 게시글 검색", description = "/search/option/paging?page=번호(1~)&keyword=검색내용,{option}1-최신순,2-추천순,3-조회순")
    public ResponseEntity<?> searchPage(@PageableDefault(page = 1) Pageable pageable,
                                        @PathVariable Integer option,
                                        @RequestParam("keyword") String keyword) {
        System.out.println(keyword);
        if(keyword==null)
            return ResponseEntity.ok("검색할 키워드를 입력해주세요");
        Page<PageQuestionBoardDto> boardPages = questionBoardService.search(pageable,option,keyword);

        return ResponseEntity.ok(boardPages);
    }

    @PutMapping("/update/{questionBoardId}") // - 게시글 업데이트
    @Operation(summary = "Q&A 게시글 수정", description = "자신이 작성한 Q&A 게시글 수정")
    public ResponseEntity<?> update(@AuthorizationHeader Long id,
                                    @PathVariable Long questionBoardId,
                                    @RequestBody UpdateQuestionBoardDto updateQuestionBoardDto) {
        Account user = accountService.findByAccountId(id);
        if(questionBoardService.readWriter(questionBoardId)==user){
            questionBoardService.update(questionBoardId,updateQuestionBoardDto);
            return ResponseEntity.ok("게시글 수정완료");
        }
        else{
            return ResponseEntity.ok("권한이 없습니다");
        }
    }

    @GetMapping("/delete/{questionBoardId}")  // - 게시글 삭제
    @Operation(summary = "Q&A 게시글 삭제", description = "자신이 작성한 Q&A 게시글 삭제")
    public ResponseEntity<?> delete(@AuthorizationHeader Long id, @PathVariable Long questionBoardId) {
        Account user = accountService.findByAccountId(id);
        if(questionBoardService.readWriter(questionBoardId)==user){
            questionBoardService.delete(questionBoardId);
            return ResponseEntity.ok("게시글 삭제완료");
        }
        else{
            return ResponseEntity.ok("권한이 없습니다");
        }
    }

    @PostMapping("/read/{questionBoardId}/{questionCommentId}") // - 댓글 채택
    @Operation(summary = "Q&A 게시글 댓글 채택", description = "자신이 작성한 Q&A 게시글에서 댓글 채택/이미 채택시 불가능")
    public ResponseEntity<?> adopting(@AuthorizationHeader Long id,
                                      @PathVariable Long questionBoardId,
                                      @PathVariable Long questionCommentId){
        Account user = accountService.findByAccountId(id);
        ReadQuestionBoardDto readQuestionBoardDto = questionBoardService.read(questionBoardId);
        if(questionBoardService.readWriter(questionBoardId)==user&& !readQuestionBoardDto.isAdopted()){
            questionBoardService.adopting(questionBoardId,questionCommentId);
            return ResponseEntity.ok("댓글 채택 완료");
        }
        else{
            return ResponseEntity.ok("권한이 없습니다");
        }
    }




}
