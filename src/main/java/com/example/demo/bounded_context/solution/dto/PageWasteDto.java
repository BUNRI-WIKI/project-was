package com.example.demo.bounded_context.solution.dto;

import com.example.demo.bounded_context.solution.entity.Waste;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageWasteDto {

    private Long id;

    private String name;

    private String imageUrl;

    public PageWasteDto(Waste waste){
        this.id = waste.getId();
        this.name = waste.getName();
        this.imageUrl = waste.getImageUrl();
    }

}
