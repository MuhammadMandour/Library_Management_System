package com.example.library.mapper;

import com.example.library.dto.BorrowRecordResponseDTO;
import com.example.library.entity.BorrowRecord;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { BookMapper.class, MemberMapper.class })
public interface BorrowRecordMapper {
    BorrowRecordResponseDTO toResponseDTO(BorrowRecord borrowRecord);
    List<BorrowRecordResponseDTO> toResponseDTOList(List<BorrowRecord> records);
}