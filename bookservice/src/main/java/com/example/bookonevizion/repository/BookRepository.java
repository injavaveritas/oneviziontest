package com.example.bookonevizion.repository;

import com.example.bookonevizion.dto.SymbolCountByAuthorsDTO;
import com.example.bookonevizion.dto.BookDTO;

import java.util.List;
import java.util.Map;

public interface BookRepository {

    List<BookDTO> findAll(String order);

    BookDTO save (BookDTO dto);

    Map<String, List<BookDTO>> groupBy(String groupBy);

    List<SymbolCountByAuthorsDTO> symbolCountByAuthors(String symbol);

}
