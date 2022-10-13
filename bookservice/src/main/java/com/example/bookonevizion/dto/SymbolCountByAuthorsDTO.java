package com.example.bookonevizion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SymbolCountByAuthorsDTO implements Comparable<SymbolCountByAuthorsDTO> {

    private static final long serialVersionUID = 1L;

    private String author;
    private Long count;

    @Override
    public int compareTo(SymbolCountByAuthorsDTO symbolCountByAuthorsDTO) {
        return this.count.compareTo(symbolCountByAuthorsDTO.count);
    }

}
