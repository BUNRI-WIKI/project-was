package com.example.demo.bounded_context.declareQboard.controller;

import com.example.demo.base.Resolver.AuthorizationHeader;
import com.example.demo.bounded_context.declareQboard.dto.DeclareQboardDto;
import com.example.demo.bounded_context.declareQboard.repository.DeclareQboardRepository;
import com.example.demo.bounded_context.declareQboard.service.DeclareQboardService;
import com.example.demo.bounded_context.questionBoard.dto.CreateQuestionBoardDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questionBoard/declare")
public class DeclareQboardController {

    private final DeclareQboardService declareQboardService;
    private final DeclareQboardRepository declareQboardRepository;

    @PostMapping("/{questionBoardId}") // - 신고
    @Operation(summary = "신고", description = "신고는 각 글당 한번만")
    public ResponseEntity<?> declare(@AuthorizationHeader Long accountId,
                                     @PathVariable Long questionBoardId,
                                     @RequestBody DeclareQboardDto dto){
        Long declareId = declareQboardRepository.findByBoardAndAccount(questionBoardId, accountId);
        if(declareId!=null)
        {
            return ResponseEntity.ok("이미 신고한 게시글 입니다.");
        }


        else{
            declareQboardService.create(questionBoardId,accountId,dto);
            return ResponseEntity.ok("신고완료");
        }
    }

}
