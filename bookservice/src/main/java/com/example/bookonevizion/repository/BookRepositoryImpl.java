package com.example.bookonevizion.repository;

import com.example.bookonevizion.dto.SymbolCountByAuthorsDTO;
import com.example.bookonevizion.dto.BookDTO;
import com.example.bookonevizion.entity.BookEntity;
import com.example.bookonevizion.exception.InputValidationException;
import com.example.bookonevizion.mapper.BookMapper;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private JdbcTemplate jdbcTemplate;
    private BookMapper mapper;

    @Override
    public List<BookDTO> findAll(String sort) {
        String[] order = sort.split(",");
        List<BookEntity> entityList = jdbcTemplate.query(
                "SELECT * FROM one_vizion.book ORDER BY " + order[0] + " " + order[1],
                this::rowToBookEntity
        );
        return entityList
                .parallelStream()
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
    public Map<String, List<BookDTO>> groupBy(String groupBy) {
        List<BookEntity> entityList = jdbcTemplate.query(
                "SELECT * FROM one_vizion.book",
                this::rowToBookEntity
        );
        return entityList
                .parallelStream()
                .map(mapper::entityToDto)
                .collect(Collectors.groupingBy(getBookGroupMethod(groupBy)));
    }

    @Override
    public List<SymbolCountByAuthorsDTO> symbolCountByAuthors(String symbol) {
        if (Pattern.matches("[a-zA-Z0-9]{1}", symbol)) {
            PreparedStatementCreatorFactory preparedStatementCreatorFactory =
                    new PreparedStatementCreatorFactory(
                            "select symbol_occur.author, count(symbol) as symbol_count from (select author, regexp_matches(title, ?, 'gi') symbol from one_vizion.book) as symbol_occur group by symbol_occur.author order by symbol_count desc limit 10",
                            Types.VARCHAR
                    );
            PreparedStatementCreator preparedStatementCreator =
                    preparedStatementCreatorFactory.newPreparedStatementCreator(Collections.singletonList(symbol));
            return jdbcTemplate.query(preparedStatementCreator, this::rowToAuthorSymbolCountDTO);

        } else {
            throw new InputValidationException();
        }
    }

    private BookEntity rowToBookEntity(ResultSet row, int rowNum) throws SQLException {
        return new BookEntity(
            Long.parseLong(row.getString("id")),
            row.getString("title"),
            row.getString("author"),
            row.getString("description")
        );
    }

    private SymbolCountByAuthorsDTO rowToAuthorSymbolCountDTO(ResultSet row, int rowNum) throws SQLException {
        return SymbolCountByAuthorsDTO.builder()
                .author(row.getString("author"))
                .count(row.getLong("symbol_count"))
                .build();
    }

    private Function<BookDTO, String> getBookGroupMethod(String alias) {
        switch (alias) {
            case "title":
                return BookDTO::getTitle;
            case "description":
                return BookDTO::getDescription;
            default: return BookDTO::getAuthor;
        }
    }

}
