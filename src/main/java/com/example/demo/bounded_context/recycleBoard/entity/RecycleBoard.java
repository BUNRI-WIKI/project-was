package com.example.demo.bounded_context.recycleBoard.entity;

import com.example.demo.base.common.BaseTimeEntity;
import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.questionBoard.dto.UpdateQuestionBoardDto;
import com.example.demo.bounded_context.questionComment.entity.QuestionComment;
import com.example.demo.bounded_context.recycleBoard.dto.UpdateRecycleBoardDto;
import com.example.demo.bounded_context.recycleComment.entity.RecycleComment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RecycleBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String shareTarget;

    private String imageUrl;

    private String content;

    private String nickName;

    private String location;

    private boolean collection;

    @OneToMany(mappedBy = "recycleBoard", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) //게시글 삭제시 삭제, question도 lazy가능한지 지켜보기
    private List<RecycleComment> comments;

    @JoinColumn(name = "RECYCLE_WRITER_ID", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Account writer;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer view;

    @Builder
    public RecycleBoard(String title, String shareTarget, String content, String location, boolean collection, Account writer, Integer view, String imageUrl, String nickName){
        this.title = title;
        this.shareTarget = shareTarget;
        this.content = content;
        this.location = location;
        this.collection = collection;
        this.writer = writer;
        this.view = view;
        this.imageUrl=imageUrl;
        this.nickName=nickName;
    }

    public void finish() {
        this.collection=true;
    }

    public void view(){
        this.view++;
    }

    public void update(UpdateRecycleBoardDto updateRecycleBoardDto) {
        this.title = updateRecycleBoardDto.getTitle();
        this.content = updateRecycleBoardDto.getContent();
        this.shareTarget = updateRecycleBoardDto.getShareTarget();
        this.location = updateRecycleBoardDto.getLocation();
        this.imageUrl=updateRecycleBoardDto.getImageUrl();
    }

}
