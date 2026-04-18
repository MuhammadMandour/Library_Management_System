package com.example.library.controller;

import com.example.library.dto.MemberRequestDTO;
import com.example.library.dto.MemberResponseDTO;
import com.example.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO(team - assignee: ____):
 * Wire these endpoints to MemberService. See AuthorController for the pattern.
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // GET /api/members
    @GetMapping
    public ResponseEntity<Page<MemberResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(memberService.findAll(pageable));
    }

    // GET /api/members/search?name=...  (declare BEFORE /{id})
    @GetMapping("/search")
    public ResponseEntity<List<MemberResponseDTO>> search(@RequestParam String name) {
        return ResponseEntity.ok(memberService.searchByName(name));
    }

    // GET /api/members/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.findById(id));
    }

    // POST /api/members
    @PostMapping
    public ResponseEntity<MemberResponseDTO> create(@Valid @RequestBody MemberRequestDTO request) {
        MemberResponseDTO created = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/members/{id}
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody MemberRequestDTO request) {
        return ResponseEntity.ok(memberService.update(id, request));
    }

    // DELETE /api/members/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
