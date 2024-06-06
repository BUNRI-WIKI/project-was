package com.example.demo.bounded_context.questionComment.dto;

import com.example.demo.bounded_context.questionComment.entity.QuestionComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCommentResponseDto {
    private Long id;
    private String content;
    private Integer recommend;
    private Long boardId;
    private String nickname;
    private String accountName;
    private LocalDateTime createdDate;

    public QuestionCommentResponseDto(QuestionComment questionComment){
        this.id = questionComment.getId();
        this.content = questionComment.getContent();
        this.recommend = questionComment.getRecommend();
        this.boardId = questionComment.getQuestionBoard().getId();
        this.nickname = questionComment.getWriter().getNickname();
        this.accountName = questionComment.getWriter().getAccountName();
        this.createdDate = questionComment.getCreatedDate();
    }

}
