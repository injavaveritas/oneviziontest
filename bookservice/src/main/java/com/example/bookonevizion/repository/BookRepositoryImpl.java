package com.example.bookonevizion.repository;

import com.example.bookonevizion.dto.AuthorDTO;
import com.example.bookonevizion.dto.AuthorSymbolCountDTO;
import com.example.bookonevizion.dto.BookDTO;
import com.example.bookonevizion.entity.BookEntity;
import com.example.bookonevizion.mapper.BookMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private JdbcTemplate jdbcTemplate;
    private BookMapper mapper;

    @Override
    public List<BookDTO> findAllOrderByTitleDesc() {
        List<BookEntity> entityList = jdbcTemplate.query(
                "SELECT * FROM one_vizion.book ORDER BY title DESC",
                this::rowToBookEntity
        );
        return entityList
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO save(BookDTO dto) {
        BookEntity entity = mapper.dtoToEntity(dto);
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO one_vizion.book " +
                            "(title, author, description) " +
                            "VALUES (?,?,?)",
                        new String[] {"id"});
                preparedStatement.setString(1, entity.getTitle());
                preparedStatement.setString(2, entity.getAuthor());
                preparedStatement.setString(3, entity.getDescription());
                return preparedStatement;
            }
        };
        jdbcTemplate.update(preparedStatementCreator, generatedKeyHolder);
        entity.setId(generatedKeyHolder.getKey().longValue());
        dto = mapper.entityToDto(entity);
        return dto;
    }

    @Override
    public AuthorDTO findByAuthor(String author) {
        List<BookEntity> entityList = jdbcTemplate.query(
                "SELECT * FROM one_vizion.book WHERE author = ?",
                this::rowToBookEntity,
                author
        );

        if(entityList.isEmpty()) {
            return AuthorDTO.builder().build();
        } else {
            return AuthorDTO.builder()
                    .author(entityList.get(0).getAuthor())
                    .books(entityList.stream().map(BookEntity::getTitle).collect(Collectors.toList()))
                    .build();
        }
    }

    @Override
    public List<AuthorSymbolCountDTO> findAuthorBySymbol(String symbol) {
        List<AuthorDTO> authorDTOList = jdbcTemplate.query(
                "SELECT author, string_agg(title, ';') AS books FROM one_vizion.book GROUP BY author",
                this::rowToAuthorDto
        );
        return authorDTOList
                .stream()
                .map(authorDTO -> new AuthorSymbolCountDTO(
                        authorDTO.getAuthor(),
                        authorDTO.getBooks().stream().flatMap(book -> Arrays.stream(book.split(""))).filter(c -> c.equalsIgnoreCase(symbol)).count()))
                .sorted(Collections.reverseOrder())
                .limit(10)
                .collect(Collectors.toList());
    }

    private BookEntity rowToBookEntity(ResultSet row, int rowNum) throws SQLException {
        return new BookEntity(
            Long.parseLong(row.getString("id")),
            row.getString("title"),
            row.getString("author"),
            row.getString("description")
        );
    }

    private AuthorDTO rowToAuthorDto(ResultSet row, int rowNum) throws SQLException {
        return AuthorDTO.builder()
                .author(row.getString("author"))
                .books(Arrays.asList(row.getString("books").split(";")))
                .build();
    }

}
