package com.example.bookonevizion.mapper;

import com.example.bookonevizion.dto.BookDTO;
import com.example.bookonevizion.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface BookMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "author", target = "author"),
            @Mapping(source = "description", target = "description")
    })
    BookEntity dtoToEntity(BookDTO dto);
    BookDTO entityToDto(BookEntity entity);

}
