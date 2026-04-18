package com.example.library.mapper;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * TODO(team): implement Book <-> DTO mapping.
 *
 * Hints:
 *  - Book.author is set by the SERVICE (it needs to fetch the Author by authorId),
 *    so ignore `authorId` when mapping from request DTO to entity (use @Mapping(target="author", ignore=true)).
 *  - For toResponseDTO, MapStruct will automatically map Book.author -> BookResponseDTO.author
 *    as long as AuthorMapper is a known component. Either:
 *      (a) add `uses = AuthorMapper.class` to the @Mapper annotation, OR
 *      (b) define the mapping methods yourself.
 */
@Mapper(componentModel = "spring", uses = { AuthorMapper.class })
public interface BookMapper {

    // TODO(team): ensure authorId is ignored here; service will set the Author manually.
    Book toEntity(BookRequestDTO dto);

    BookResponseDTO toResponseDTO(Book book);

    List<BookResponseDTO> toResponseDTOList(List<Book> books);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(BookRequestDTO dto, @MappingTarget Book entity);
}
