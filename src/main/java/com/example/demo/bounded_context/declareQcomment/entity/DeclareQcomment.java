package com.example.demo.bounded_context.declareQcomment.entity;

import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.questionComment.entity.QuestionComment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DeclareQcomment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    private QuestionComment questionComment;

    @Column(nullable = false)
    private Integer type;

    @Column(nullable = true)
    private String description;

    @Builder
    public DeclareQcomment(Account account, QuestionComment questionComment, Integer type, String description){
        this.account=account;
        this.questionComment=questionComment;
        this.type=type;
        this.description=description;
    }

}
