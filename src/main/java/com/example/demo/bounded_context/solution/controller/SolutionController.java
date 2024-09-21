package com.example.demo.bounded_context.solution.controller;

import com.example.demo.base.Resolver.AuthorizationHeader;
import com.example.demo.bounded_context.solution.dto.DetectResponse;
import com.example.demo.bounded_context.solution.api.SolutionApi;
import com.example.demo.bounded_context.solution.dto.*;
import com.example.demo.bounded_context.solution.service.SolutionUseCase;
import com.example.demo.bounded_context.solution.service.WasteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/solution")
public class SolutionController implements SolutionApi {
    private final SolutionUseCase solutionUseCase;
    private final WasteService wasteService;

    public ResponseEntity<DetectResponse> searchByImage(@RequestParam("imageUrl") String imageUrlEncoding) {
        try{
            String imageUrl = URLDecoder.decode(imageUrlEncoding, "UTF-8");
            DetectResponse detectResponse = solutionUseCase.searchByImage(imageUrl);
            return ResponseEntity.ok().body(detectResponse);
        }catch (UnsupportedEncodingException e){
            throw new IllegalArgumentException("Invalid image URL");
        }
    }

    public ResponseEntity<SolutionResponse> searchByKeyword(@RequestParam("keyword") String keyword){
        SolutionResponse solution = solutionUseCase.searchByKeyword(keyword);
        return ResponseEntity.ok().body(solution);
    }

    public ResponseEntity<Page<SolutionListResponse>> searchByCategory(@RequestParam("category") String category,
                                                                       @PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<SolutionListResponse> response = solutionUseCase.searchByCategory(category, pageable);
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Long> create(@AuthorizationHeader Long accountId,
                                       @RequestBody SolutionRequest request){
        Long id = solutionUseCase.create(accountId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    public ResponseEntity<Page<ContributeCreationListResponse>> readContributeCreationList(@PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<ContributeCreationListResponse> creations = solutionUseCase.readContributeCreations(pageable);
        return ResponseEntity.ok(creations);
    }

    public ResponseEntity<SolutionResponse> readContributeCreation(@PathVariable Long wasteId){
        SolutionResponse creation = solutionUseCase.readContributeCreation(wasteId);
        return ResponseEntity.ok(creation);
    }

    public ResponseEntity<Long> accept(@PathVariable Long wasteId){
        Long acceptSolutionId = solutionUseCase.accept(wasteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(acceptSolutionId);
    }

    public ResponseEntity<Long> reject(@PathVariable Long wasteId) {
        Long rejectSolutionId = solutionUseCase.reject(wasteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(rejectSolutionId);
    }


    @GetMapping("/search")
    @Operation(summary = "강화검색", description = "/search?keyword=검색할것 Tag, Waste 한번에 검색 -> 커뮤니티 검색 -> 글작성")
    public ResponseEntity<?> searchWaste(@PageableDefault(page = 1) Pageable pageable,
                                         @RequestParam("keyword") String keyword) {
        if(keyword==null)
            return ResponseEntity.ok("검색할 키워드를 입력해주세요");
        Page<PageWasteDto> wastePages = wasteService.WandTSearch(pageable,keyword);

        return ResponseEntity.ok(wastePages);
    }

}
