package com.example.bookonevizion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthorSymbolCountDTO implements Comparable<AuthorSymbolCountDTO> {

    private static final long serialVersionUID = 1L;

    private String author;
    private Long count;

    @Override
    public int compareTo(AuthorSymbolCountDTO authorSymbolCountDTO) {
        return this.count.compareTo(authorSymbolCountDTO.count);
    }

}
