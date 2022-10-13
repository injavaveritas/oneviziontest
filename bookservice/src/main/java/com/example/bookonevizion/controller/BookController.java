package com.example.bookonevizion.controller;

import com.example.bookonevizion.dto.SymbolCountByAuthorsDTO;
import com.example.bookonevizion.dto.BookDTO;
import com.example.bookonevizion.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book-management")
@AllArgsConstructor
public class BookController {

    private BookRepository repository;

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> findAll(
            @RequestParam(required = false, value = "sort", defaultValue = "title,desc") String sort) {
        List<BookDTO> list = repository.findAll(sort);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/books")
    public ResponseEntity<BookDTO> save(@RequestBody BookDTO dto) {
        dto = repository.save(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping("/grouped-books")
    public ResponseEntity<Map<String, List<BookDTO>>> groupBy(
            @RequestParam(required = false, value = "group-by", defaultValue = "author") String groupBy) {
        Map<String, List<BookDTO>> dto = repository.groupBy(groupBy);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/symbol-count-by-authors")
    public ResponseEntity<List<SymbolCountByAuthorsDTO>> symbolCountByAuthors(
            @RequestParam(value = "symbol") String symbol) {
        List<SymbolCountByAuthorsDTO> list = repository.symbolCountByAuthors(symbol);
        return ResponseEntity.ok().body(list);
    }

}
