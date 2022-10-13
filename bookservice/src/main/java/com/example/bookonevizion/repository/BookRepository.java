package com.example.bookonevizion.repository;

import com.example.bookonevizion.dto.AuthorDTO;
import com.example.bookonevizion.dto.AuthorSymbolCountDTO;
import com.example.bookonevizion.dto.BookDTO;
import java.util.List;

public interface BookRepository {

    List<BookDTO> findAllOrderByTitleDesc();

    BookDTO save (BookDTO dto);

    AuthorDTO findByAuthor(String author);

    List<AuthorSymbolCountDTO> findAuthorBySymbol(String symbol);

}
