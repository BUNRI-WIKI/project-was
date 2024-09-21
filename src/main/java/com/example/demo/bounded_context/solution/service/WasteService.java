package com.example.demo.bounded_context.solution.service;

import com.example.demo.base.exception.CustomException;
import com.example.demo.base.exception.ExceptionCode;
import com.example.demo.bounded_context.account.entity.Account;
import com.example.demo.bounded_context.questionBoard.dto.PageQuestionBoardDto;
import com.example.demo.bounded_context.solution.dto.PageWasteDto;
import com.example.demo.bounded_context.solution.entity.ContributedCreationState;
import com.example.demo.bounded_context.solution.entity.Waste;
import com.example.demo.bounded_context.solution.repository.WasteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WasteService {
    private final WasteRepository wasteRepository;

    @Transactional(readOnly = true)
    public Waste findById(Long id){
        return wasteRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.WASTE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<Long> findByName(String name){
        log.info("솔루션 이름 조회");
        return wasteRepository.findIdByName(name);
    }

    @Transactional(readOnly = true)
    public Page<Waste> findFetchByCategory(String name, Pageable pageable){
        log.info("카테고리별 솔루션 조회(Waste, Category, Tag)");
        return wasteRepository.findByCategoriesName(name, pageable);
    }

    @Transactional(readOnly = true)
    public Waste findFetchById(Long id){
        log.info("솔루션 조회(Waste, Category, Tag)");
        return wasteRepository.findFetchById(id);
    }

    @Transactional
    public Waste create(Account account, Waste waste){
        log.info("솔루션 생성");
        waste.setWriter(account);
        return wasteRepository.save(waste);
    }

    @Transactional(readOnly = true)
    public Page<Waste> findByAccountIdAndStateWithPaging(Long accountId, ContributedCreationState state, Pageable pageable){
        log.info("페이징을 사용한 사용자별 솔루션 목록 조회");
        return wasteRepository.findByAccountIdAndState(accountId, state, pageable);
    }
    @Transactional(readOnly = true)
    public Page<Waste> findAllFetchWithPaging(Pageable pageable){
        log.info("페이징을 사용한 모든 솔루션 목록 조회");
        return wasteRepository.findAllFetchByPage(pageable);
    }

    @Transactional
    public Waste accept(Long wasteId){
        log.info("솔루션 승인");
        Waste foundWaste = findById(wasteId);
        foundWaste.accept();
        return foundWaste;
    }

    @Transactional
    public Waste reject(Long wasteId){
        log.info("솔루션 거부");
        Waste foundWaste = findById(wasteId);
        foundWaste.reject();
        return foundWaste;
    }

    @Transactional
    public void updateWriter(Account writer){
        wasteRepository.updateWriter(writer);
    }


    /**
     * Tag, Waste로 검색
     */
    public Page<PageWasteDto> WandTSearch(Pageable pageable, String keyword) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 10; // 한페이지에 보여줄 waste 개수
        Page<Waste> wastePages;
        wastePages = wasteRepository.findDistinctByWasteNameOrTagNameContaining(keyword, PageRequest.of(page, pageLimit));

        return wastePages.map(
                PageWasteDto::new);
    }
}
