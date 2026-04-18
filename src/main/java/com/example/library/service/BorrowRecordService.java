package com.example.library.service;

import com.example.library.dto.BorrowRecordRequestDTO;
import com.example.library.dto.BorrowRecordResponseDTO;
import com.example.library.mapper.BorrowRecordMapper;
import com.example.library.repository.BookRepository;
import com.example.library.repository.BorrowRecordRepository;
import com.example.library.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO(team - assignee: ____):
 * This service carries the main business logic for borrowing/returning.
 * Use AuthorService as a reference for shape.
 *
 * Methods needed:
 *  - borrowBook(BorrowRecordRequestDTO)   -> POST /api/borrow-records
 *  - returnBook(Long recordId)            -> PUT  /api/borrow-records/{id}/return
 *  - findByMember(Long memberId)          -> GET  /api/borrowrecords/member/{memberId}   (note: the
 *                                            spec literally writes "borrowrecords" without a hyphen here;
 *                                            we keep it exactly as specified for the automated grader).
 *  - findActive()                         -> GET  /api/borrow-records/active
 *
 * borrowBook logic (IMPORTANT):
 *   1. Fetch Book by bookId -> else ResourceNotFoundException
 *   2. Fetch Member by memberId -> else ResourceNotFoundException
 *   3. Check borrowRecordRepository.findByBookIdAndReturnDateIsNull(bookId)
 *        - if present -> throw new BookAlreadyBorrowedException(bookId)  (maps to 409 Conflict)
 *   4. Create new BorrowRecord(book, member), save, map, return.
 *      borrowDate is auto-set by @PrePersist.
 *
 * returnBook logic:
 *   1. Find BorrowRecord by id -> else ResourceNotFoundException
 *   2. If returnDate != null -> throw IllegalArgumentException("Book has already been returned")  (400)
 *   3. Set returnDate = LocalDateTime.now(), save, map, return.
 */
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
        // TODO(team): see steps in class-level javadoc above.
        throw new UnsupportedOperationException("borrowBook not implemented yet");
    }

    public BorrowRecordResponseDTO returnBook(Long recordId) {
        // TODO(team): see steps in class-level javadoc above.
        throw new UnsupportedOperationException("returnBook not implemented yet");
    }

    @Transactional(readOnly = true)
    public List<BorrowRecordResponseDTO> findByMember(Long memberId) {
        // TODO(team):
        //   - if !memberRepository.existsById(memberId) -> ResourceNotFoundException
        //   - return borrowRecordRepository.findByMemberId(memberId) mapped to DTO list
        throw new UnsupportedOperationException("findByMember not implemented yet");
    }

    @Transactional(readOnly = true)
    public List<BorrowRecordResponseDTO> findActive() {
        // TODO(team): return borrowRecordRepository.findByReturnDateIsNull() mapped to DTO list
        throw new UnsupportedOperationException("findActive not implemented yet");
    }
}
