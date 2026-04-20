package com.example.library.service;

import com.example.library.dto.BorrowRecordRequestDTO;
import com.example.library.dto.BorrowRecordResponseDTO;
import com.example.library.entity.Book;
import com.example.library.entity.BorrowRecord;
import com.example.library.entity.Member;
import com.example.library.exception.BookAlreadyBorrowedException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.mapper.BorrowRecordMapper;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRecordRepository;
import com.example.library.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordMapper borrowRecordMapper;

    public BorrowRecordService(BorrowRecordRepository borrowRecordRepository,
                               BookRepository bookRepository,
                               MemberRepository memberRepository,
                               BorrowRecordMapper borrowRecordMapper) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRecordMapper = borrowRecordMapper;
    }

    public BorrowRecordResponseDTO borrowBook(BorrowRecordRequestDTO request) {
        // 1. Fetch Book — 404 if not found
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", request.getBookId()));

        // 2. Fetch Member — 404 if not found
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member", request.getMemberId()));

        // 3. Check if book is already borrowed — 409 if it is
        borrowRecordRepository.findByBookIdAndReturnDateIsNull(request.getBookId())
                .ifPresent(r -> { throw new BookAlreadyBorrowedException(request.getBookId()); });

        // 4. Create, save, and return
        BorrowRecord record = new BorrowRecord(book, member);
        BorrowRecord saved = borrowRecordRepository.save(record);
// Re-fetch with eager loading so the mapper can access book and member
        BorrowRecord fetched = borrowRecordRepository.findById(saved.getId())
                .orElseThrow(() -> new ResourceNotFoundException("BorrowRecord", saved.getId()));
        return borrowRecordMapper.toResponseDTO(fetched);
    }

    public BorrowRecordResponseDTO returnBook(Long recordId) {
        // 1. Find record — 404 if not found
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("BorrowRecord", recordId));

        // 2. Reject if already returned — 400
        if (record.getReturnDate() != null) {
            throw new IllegalArgumentException("Book has already been returned");
        }

        // 3. Set return date, save, and return
        record.setReturnDate(LocalDateTime.now());
        borrowRecordRepository.save(record);
        BorrowRecord fetched = borrowRecordRepository.findById(record.getId())
                .orElseThrow(() -> new ResourceNotFoundException("BorrowRecord", record.getId()));
        return borrowRecordMapper.toResponseDTO(fetched);
    }

    @Transactional(readOnly = true)
    public List<BorrowRecordResponseDTO> findByMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member", memberId);
        }
        return borrowRecordMapper.toResponseDTOList(
                borrowRecordRepository.findByMemberId(memberId));
    }

    @Transactional(readOnly = true)
    public List<BorrowRecordResponseDTO> findActive() {
        return borrowRecordMapper.toResponseDTOList(
                borrowRecordRepository.findByReturnDateIsNull());
    }
}