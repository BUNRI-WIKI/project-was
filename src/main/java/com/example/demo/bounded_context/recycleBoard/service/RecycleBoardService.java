package com.example.demo.bounded_context.recycleBoard.service;

import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.questionBoard.dto.PageQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.ReadQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.dto.UpdateQuestionBoardDto;
import com.example.demo.bounded_context.questionBoard.entity.QuestionBoard;
import com.example.demo.bounded_context.recycleBoard.dto.CreateRecycleBoardDto;
import com.example.demo.bounded_context.recycleBoard.dto.PageRecycleBoardDto;
import com.example.demo.bounded_context.recycleBoard.dto.ReadRecycleBoardDto;
import com.example.demo.bounded_context.recycleBoard.dto.UpdateRecycleBoardDto;
import com.example.demo.bounded_context.recycleBoard.entity.RecycleBoard;
import com.example.demo.bounded_context.recycleBoard.repository.RecycleBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecycleBoardService {

    private final RecycleBoardRepository recycleBoardRepository;

    @Transactional
    public void create(CreateRecycleBoardDto createRecycleBoardDto, Account writer){
        RecycleBoard recycleBoard = RecycleBoard.builder()
                .title(createRecycleBoardDto.getTitle())
                .content(createRecycleBoardDto.getContent())
                .shareTarget(createRecycleBoardDto.getShareTarget())
                .location(createRecycleBoardDto.getLocation())
                .imageUrl(createRecycleBoardDto.getImageUrl())
                .collection(false)
                .writer(writer)
                .view(0)
                .nickName(writer.getNickname())
                .build();

        recycleBoardRepository.save(recycleBoard);
    }

    @Transactional
    public ReadRecycleBoardDto read (Long recycleBoardId){
        RecycleBoard recycleBoard = recycleBoardRepository.findById(recycleBoardId)
                .orElseThrow(() -> new IllegalArgumentException("RECYCLEBOARD_NOT_FOUND"));

        recycleBoard.view();
        return new ReadRecycleBoardDto(recycleBoard);
    }

    @Transactional
    public Account readWriter(Long recycleBoardId){
        RecycleBoard recycleBoard = recycleBoardRepository.findById(recycleBoardId)
                .orElseThrow(() -> new IllegalArgumentException("RECYCLEBOARD_NOT_FOUND"));
        return recycleBoard.getWriter();
    }

    @Transactional
    public void update(Long recycleBoardId, UpdateRecycleBoardDto updateRecycleBoardDto){
        RecycleBoard recycleBoard = recycleBoardRepository.findById(recycleBoardId)
                .orElseThrow(() -> new IllegalArgumentException("RECYCLEBOARD_NOT_FOUND"));

        recycleBoard.update(updateRecycleBoardDto);
    }

    //d
    @Transactional
    public void delete(Long recycleBoardId){
        RecycleBoard recycleBoard = recycleBoardRepository.findById(recycleBoardId)
                .orElseThrow(() -> new IllegalArgumentException("RECYCLE_NOT_FOUND"));

        recycleBoardRepository.delete(recycleBoard);
    }

    @Transactional
    public void finish(Long recycleBoardId){
        RecycleBoard recycleBoard = recycleBoardRepository.findById(recycleBoardId)
                .orElseThrow(() -> new IllegalArgumentException("RECYCLE_NOT_FOUND"));

        recycleBoard.finish();
    }

    public Page<PageRecycleBoardDto> paging(Pageable pageable, Integer option) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 10; // 한페이지에 보여줄 글 개수
        Page<RecycleBoard> recycleBoardPages;
        if(option==1) {
            recycleBoardPages = recycleBoardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "createdDate")));
        }
        else{
            recycleBoardPages = recycleBoardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "view","createdDate")));
        }

        return recycleBoardPages.map(
                PageRecycleBoardDto::new);
    }

    public Page<PageRecycleBoardDto> titleSearch(Pageable pageable, Integer option, String keyword) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 10; // 한페이지에 보여줄 글 개수
        Page<RecycleBoard> recycleBoardPages;
        if(option==1) {
            recycleBoardPages = recycleBoardRepository.findByTitleContaining(keyword,PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "createdDate")));
        }
        else{
            recycleBoardPages = recycleBoardRepository.findByTitleContaining(keyword,PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "view","createdDate")));
        }

        return recycleBoardPages.map(
                PageRecycleBoardDto::new);
    }

    public Page<PageRecycleBoardDto> nickNameSearch(Pageable pageable, Integer option, String nickName) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 10; // 한페이지에 보여줄 글 개수
        Page<RecycleBoard> recycleBoardPages;
        if(option==1) {
            recycleBoardPages = recycleBoardRepository.findByNickName(nickName,PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "createdDate")));
        }
        else{
            recycleBoardPages = recycleBoardRepository.findByNickName(nickName,PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "view","createdDate")));
        }

        return recycleBoardPages.map(
                PageRecycleBoardDto::new);
    }


}
