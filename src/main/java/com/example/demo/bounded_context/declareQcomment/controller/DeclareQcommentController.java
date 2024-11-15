package com.example.demo.bounded_context.declareQcomment.controller;


import com.example.demo.base.Resolver.AuthorizationHeader;
import com.example.demo.bounded_context.declareQcomment.dto.DeclareQcommentDto;
import com.example.demo.bounded_context.declareQcomment.repository.DeclareQcommentRepository;
import com.example.demo.bounded_context.declareQcomment.service.DeclareQcommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questionComment/declare")
public class DeclareQcommentController {

    private final DeclareQcommentService declareQcommentService;
    private final DeclareQcommentRepository declareQcommentRepository;

    @PostMapping("/{questionCommentId}") // - 신고
    @Operation(summary = "신고", description = "신고는 각 댓글당 한번만")
    public ResponseEntity<?> declare(@AuthorizationHeader Long accountId,
                                     @PathVariable Long questionCommentId,
                                     @RequestBody DeclareQcommentDto dto){
        Long declareId = declareQcommentRepository.findByCommentAndAccount(questionCommentId, accountId);
        if(declareId!=null)
        {
            return ResponseEntity.ok("이미 신고한 댓글 입니다.");
        }


        else{
            declareQcommentService.create(questionCommentId,accountId,dto);
            return ResponseEntity.ok("신고완료");
        }
    }

}
