package com.example.library.dto;

import java.time.LocalDateTime;

public class BorrowRecordResponseDTO {

    private Long id;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate; // null means still borrowed

    // Nested DTOs - avoid exposing full entities.
    private BookResponseDTO book;
    private MemberResponseDTO member;

    public BorrowRecordResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public BookResponseDTO getBook() {
        return book;
    }

    public void setBook(BookResponseDTO book) {
        this.book = book;
    }

    public MemberResponseDTO getMember() {
        return member;
    }

    public void setMember(MemberResponseDTO member) {
        this.member = member;
    }
}
