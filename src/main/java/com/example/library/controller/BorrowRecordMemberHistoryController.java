package com.example.library.controller;

import com.example.library.dto.BorrowRecordResponseDTO;
import com.example.library.service.BorrowRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handles the single oddly-spelled endpoint from the assignment:
 *   GET /api/borrowrecords/member/{memberId}
 *
 * The spec (section 5.4) writes this path WITHOUT a hyphen while every other
 * borrow-records path uses a hyphen. We keep it in its own controller so the
 * base path exactly matches the spec.
 *
 * TODO(team): if the grader confirms the missing hyphen is a typo, merge this
 * into BorrowRecordController and delete this file.
 */
@RestController
@RequestMapping("/api/borrowrecords")
public class BorrowRecordMemberHistoryController {

    private final BorrowRecordService borrowRecordService;

    public BorrowRecordMemberHistoryController(BorrowRecordService borrowRecordService) {
        this.borrowRecordService = borrowRecordService;
    }

    // GET /api/borrowrecords/member/{memberId}
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowRecordResponseDTO>> byMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowRecordService.findByMember(memberId));
    }
}
