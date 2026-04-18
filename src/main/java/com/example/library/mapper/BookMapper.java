package com.example.library.mapper;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for Book entity and DTOs.
 */
@Mapper(componentModel = "spring", uses = { AuthorMapper.class })
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "borrowRecords", ignore = true)
    Book toEntity(BookRequestDTO dto);

    BookResponseDTO toResponseDTO(Book book);

    List<BookResponseDTO> toResponseDTOList(List<Book> books);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "borrowRecords", ignore = true)
    void updateEntityFromDTO(BookRequestDTO dto, @MappingTarget Book entity);
}

