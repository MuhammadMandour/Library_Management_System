package com.example.library.mapper;

import com.example.library.dto.BorrowRecordResponseDTO;
import com.example.library.entity.BorrowRecord;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * TODO(team): book and member fields in the response need to reuse the existing
 * BookMapper and MemberMapper. `uses = { BookMapper.class, MemberMapper.class }`
 * handles this automatically.
 *
 * Note: there is no `toEntity(BorrowRecordRequestDTO)` mapping here — the service
 * constructs the BorrowRecord manually from the fetched Book and Member entities.
 */
@Mapper(componentModel = "spring", uses = { BookMapper.class, MemberMapper.class })
public interface BorrowRecordMapper {

    BorrowRecordResponseDTO toResponseDTO(BorrowRecord borrowRecord);

    List<BorrowRecordResponseDTO> toResponseDTOList(List<BorrowRecord> records);
}
