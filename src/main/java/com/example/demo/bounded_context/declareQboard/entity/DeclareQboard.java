package com.example.demo.bounded_context.declareQboard.entity;

import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.questionBoard.entity.QuestionBoard;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class DeclareQboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private QuestionBoard questionBoard;

    @Column(nullable = false)
    private Integer type;

    @Column(nullable = true)
    private String description;

    @Builder
    public DeclareQboard(Account account, QuestionBoard questionBoard, Integer type, String description){
        this.account=account;
        this.questionBoard=questionBoard;
        this.type=type;
        this.description=description;
    }

}
