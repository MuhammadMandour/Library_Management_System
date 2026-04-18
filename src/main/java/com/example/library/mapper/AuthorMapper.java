package com.example.library.mapper;

import com.example.library.dto.AuthorRequestDTO;
import com.example.library.dto.AuthorResponseDTO;
import com.example.library.entity.Author;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorRequestDTO dto);

    AuthorResponseDTO toResponseDTO(Author author);

    List<AuthorResponseDTO> toResponseDTOList(List<Author> authors);

    // For PUT /api/authors/{id} -> updates an existing entity from a request DTO
    // without replacing non-null fields with null.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(AuthorRequestDTO dto, @MappingTarget Author entity);
}
