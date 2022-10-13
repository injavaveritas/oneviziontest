package com.example.bookonevizion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AuthorDTO {

    private static final long serialVersionUID = 1L;

    private String author;
    private List<String> books;

    public List<String> getBooks() {
        return books;
    }

}
