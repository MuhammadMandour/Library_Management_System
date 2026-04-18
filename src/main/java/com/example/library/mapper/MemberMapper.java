package com.example.library.mapper;

import com.example.library.dto.MemberRequestDTO;
import com.example.library.dto.MemberResponseDTO;
import com.example.library.entity.Member;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * TODO(team): this mapper should be straightforward — Member has no nested entities
 * in its request/response DTOs. membershipDate is set by @PrePersist on the entity,
 * so it simply flows through in the response.
 */
@Mapper(componentModel = "spring")
public interface MemberMapper {

    Member toEntity(MemberRequestDTO dto);

    MemberResponseDTO toResponseDTO(Member member);

    List<MemberResponseDTO> toResponseDTOList(List<Member> members);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(MemberRequestDTO dto, @MappingTarget Member entity);
}
