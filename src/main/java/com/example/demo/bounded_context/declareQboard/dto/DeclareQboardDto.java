package com.example.demo.bounded_context.declareQboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeclareQboardDto {

    private Integer type;

    private String description;

}
