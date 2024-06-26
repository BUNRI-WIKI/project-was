package com.example.demo.bounded_context.wiki.service;

import com.example.demo.bounded_context.wiki.dto.ContributeModificationsResponse;
import com.example.demo.bounded_context.wiki.dto.WikiListResponse;
import com.example.demo.bounded_context.wiki.entity.Wiki;
import com.example.demo.bounded_context.wiki.entity.WikiState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WikiUseCase {
    private final WikiService wikiService;

    public Page<ContributeModificationsResponse> readContributeModifications(Long accountId,
                                                                          WikiState state,
                                                                          Pageable pageable){
        Page<Wiki> modifications = wikiService.findByAccountIdAndState(accountId, state, pageable);
        return modifications.map(ContributeModificationsResponse::fromEntity);
    }

    public Page<WikiListResponse> readByWasteId(Long wasteId, Pageable pageable){
        Page<Wiki> wikiList = wikiService.findByWasteId(wasteId, pageable);
        return wikiList.map(WikiListResponse::fromEntity);
    }

    public Page<WikiListResponse> readAll(Pageable pageable){
        Page<Wiki> wikiList = wikiService.findAll(pageable);
        return wikiList.map(WikiListResponse::fromEntity);
    }
}
