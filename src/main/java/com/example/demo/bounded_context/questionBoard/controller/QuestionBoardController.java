package com.example.demo.bounded_context.questionBoard.controller;

import com.example.demo.base.Resolver.AuthorizationHeader;
import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.account.service.AccountService;
import com.example.demo.bounded_context.questionBoard.dto.CreateQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.ReadQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.UpdateQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.service.QuestionBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questionBoard")
public class QuestionBoardController {
    private final QuestionBoardService questionBoardService;
    private final AccountService accountService;

    @PostMapping // - �α����� ����ڴ� �Խñ��� �ۼ��� �� �ִ�
    public ResponseEntity<?> createQuestionBoard(@AuthorizationHeader Long id, @RequestBody CreateQuestionBoardDto createQuestionBoardDto){
        Account writer = accountService.read(id);; // writer�� ���� ����
        questionBoardService.create(createQuestionBoardDto, writer);

        return ResponseEntity.ok("�Խñ� �ۼ� ����");
    }

    // �Խñ� ���(����Ʈ) ����(����¡ �ʿ�?)

    @GetMapping("/read/{questionBoardId}") // - �Խñ� �ڼ��� ����, (���-���������̼� �ʿ�?)
    public ResponseEntity<ReadQuestionBoardDto> readQuestionBoardById(@PathVariable Long questionBoardId) {
        ReadQuestionBoardDto readQuestionBoardDto = questionBoardService.read(questionBoardId);

        return ResponseEntity.ok(readQuestionBoardDto);
    }



    @PostMapping("/update/{questionBoardId}") // - �Խñ� ������Ʈ
    public ResponseEntity<?> updateQuestionBoard(@AuthorizationHeader Long id,
                                                 @PathVariable Long questionBoardId,
                                                 @RequestBody UpdateQuestionBoardDto updateQuestionBoardDto) {
        Account user = accountService.read(id);
        ReadQuestionBoardDto readQuestionBoardDto = questionBoardService.read(questionBoardId);
        if(readQuestionBoardDto.getWriter()==user){
            questionBoardService.update(questionBoardId,updateQuestionBoardDto);
            return ResponseEntity.ok("�Խñ� �����Ϸ�");
        }
        else{
            return ResponseEntity.ok("������ �����ϴ�");
        }
    }

    @GetMapping("/delete/{questionBoardId}") // - �Խñ� ����
    public ResponseEntity<?> deleteQuestionBoard(@AuthorizationHeader Long id, @PathVariable Long questionBoardId) {
        Account user = accountService.read(id);
        ReadQuestionBoardDto readQuestionBoardDto = questionBoardService.read(questionBoardId);
        if(readQuestionBoardDto.getWriter()==user){
            questionBoardService.delete(questionBoardId);
            return ResponseEntity.ok("�Խñ� �����Ϸ�");
        }
        else{
            return ResponseEntity.ok("������ �����ϴ�");
        }
    }

    @PostMapping("/read/{questionBoardId}/{questionCommentId}") // - ��� ä��
    public ResponseEntity<?> adoptingQuestionBoardComment(@AuthorizationHeader Long id,
                                                          @PathVariable Long questionBoardId,
                                                          @PathVariable Long questionCommentId){
        Account user = accountService.read(id);
        ReadQuestionBoardDto readQuestionBoardDto = questionBoardService.read(questionBoardId);
        if(readQuestionBoardDto.getWriter()==user&& !readQuestionBoardDto.isAdopted()){
            questionBoardService.adopting(questionBoardId,questionCommentId);
            return ResponseEntity.ok("��� ä�� �Ϸ�");
        }
        else{
            return ResponseEntity.ok("������ �����ϴ�");
        }
    }




}
