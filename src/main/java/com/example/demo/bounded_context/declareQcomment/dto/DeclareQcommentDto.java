package com.example.demo.bounded_context.declareQcomment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeclareQcommentDto {

    private Integer type;

    private String description;

}
