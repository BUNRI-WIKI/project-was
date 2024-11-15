package com.example.demo.bounded_context.declareQboard.service;

import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.account.service.AccountService;
import com.example.demo.bounded_context.declareQboard.dto.DeclareQboardDto;
import com.example.demo.bounded_context.declareQboard.entity.DeclareQboard;
import com.example.demo.bounded_context.declareQboard.repository.DeclareQboardRepository;
import com.example.demo.bounded_context.questionBoard.entity.QuestionBoard;
import com.example.demo.bounded_context.questionBoard.repository.QuestionBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeclareQboardService {

    private final DeclareQboardRepository declareQboardRepository;
    private final QuestionBoardRepository questionBoardRepository;
    private final AccountService accountService;

    //c
    @Transactional
    public void create(Long questionBoardId, Long accountId, DeclareQboardDto dto) {
        QuestionBoard questionBoard = questionBoardRepository.findById(questionBoardId)
                .orElseThrow(() -> new IllegalArgumentException("QUESTIONBOARD_NOT_FOUND"));
        Account account = accountService.findByAccountId(accountId);

        DeclareQboard declareQboard = DeclareQboard.builder()
                .account(account)
                .questionBoard(questionBoard)
                .type(dto.getType())
                .description(dto.getDescription())
                .build();

        declareQboardRepository.save(declareQboard);
    }

}
