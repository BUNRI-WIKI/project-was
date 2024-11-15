package com.example.demo.bounded_context.declareQcomment.service;

import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.account.service.AccountService;
import com.example.demo.bounded_context.declareQcomment.dto.DeclareQcommentDto;
import com.example.demo.bounded_context.declareQcomment.entity.DeclareQcomment;
import com.example.demo.bounded_context.declareQcomment.repository.DeclareQcommentRepository;
import com.example.demo.bounded_context.questionComment.entity.QuestionComment;
import com.example.demo.bounded_context.questionComment.repository.QuestionCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeclareQcommentService {

    private final DeclareQcommentRepository declareQcommentRepository;
    private final QuestionCommentRepository questionCommentRepository;
    private final AccountService accountService;

    //c
    @Transactional
    public void create(Long questionCommentId, Long accountId, DeclareQcommentDto dto) {
        QuestionComment questionComment = questionCommentRepository.findById(questionCommentId)
                .orElseThrow(() -> new IllegalArgumentException("QUESTIONBOARD_NOT_FOUND"));
        Account account = accountService.findByAccountId(accountId);

        DeclareQcomment declareQcomment = DeclareQcomment.builder()
                .account(account)
                .questionComment(questionComment)
                .type(dto.getType())
                .description(dto.getDescription())
                .build();

        declareQcommentRepository.save(declareQcomment);
    }

}
