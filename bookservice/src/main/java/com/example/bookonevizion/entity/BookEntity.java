package com.example.bookonevizion.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BookEntity {

    private Long id;
    private String title;
    private String author;
    private String description;

}
