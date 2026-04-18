package com.example.library.repository;

import com.example.library.entity.BorrowRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    // Check if a book is currently borrowed (returnDate == null).
    // Used before creating a new borrow record -> if present, return 409 Conflict.
    Optional<BorrowRecord> findByBookIdAndReturnDateIsNull(Long bookId);

    // Active borrows across the library (returnDate is null).
    @EntityGraph(attributePaths = {"book", "member"})
    List<BorrowRecord> findByReturnDateIsNull();

    // All borrow records for a given member.
    @EntityGraph(attributePaths = {"book", "member"})
    List<BorrowRecord> findByMemberId(Long memberId);
}
