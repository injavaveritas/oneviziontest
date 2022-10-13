package com.example.bookonevizion.controller;

import com.example.bookonevizion.dto.AuthorDTO;
import com.example.bookonevizion.dto.AuthorSymbolCountDTO;
import com.example.bookonevizion.dto.BookDTO;
import com.example.bookonevizion.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private BookRepository repository;

    @GetMapping
    public ResponseEntity<List<BookDTO>> findAllOrderByTitleDesc() {
        List<BookDTO> list = repository.findAllOrderByTitleDesc();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<BookDTO> save(@RequestBody BookDTO dto) {
        dto = repository.save(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping(params = "author")
    public ResponseEntity<AuthorDTO> findByAuthor(@RequestParam String author) {
        AuthorDTO dto = repository.findByAuthor(author);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(params = "symbol")
    public ResponseEntity<List<AuthorSymbolCountDTO>> findAuthorBySymbol(@RequestParam String symbol) {
        List<AuthorSymbolCountDTO> list = repository.findAuthorBySymbol(symbol);
        return ResponseEntity.ok().body(list);
    }

}
