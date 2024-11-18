package com.example.demo.bounded_context.questionComment.entity;

import com.example.demo.base.common.BaseTimeEntity;
import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.questionBoard.entity.QuestionBoard;
import com.example.demo.bounded_context.recommendComment.entity.RecommendComment;
import com.example.demo.bounded_context.declareQcomment.entity.DeclareQcomment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class QuestionComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    private Integer recommend;

    @JoinColumn(name = "QUESTION_ID", nullable = false)
    @ManyToOne
    private QuestionBoard questionBoard;

    @JoinColumn(name = "COMMENT_WRITER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Account writer;

    @OneToMany(mappedBy = "questionComment", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) //게시글 삭제시 삭제
    private List<RecommendComment> recommendCommentList;

    @OneToMany(mappedBy = "questionComment", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) //게시글 삭제시 삭제
    private List<DeclareQcomment> declareQcommentList;

    @Builder
    public QuestionComment(String content, Integer recommend, QuestionBoard questionBoard, Account writer){
        this.content=content;
        this.recommend=recommend;
        this.questionBoard=questionBoard;
        this.writer=writer;
    }

    public void update(String content) {
        this.content = content;
    }

    public void recommend(){ this.recommend++; }

    public void unRecommend(){ this.recommend--; }
}
