package com.example.bookonevizion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookDTO {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String author;
    private String description;

}
