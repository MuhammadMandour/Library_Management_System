package com.example.library.controller;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Book management endpoints.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /api/books
    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    // GET /api/books/search?title=...&genre=...&publishedYear=...
    // IMPORTANT: declare this BEFORE the /{id} mapping so "search" is not interpreted as an id.
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> search(@RequestParam(required = false) String title,
                                                        @RequestParam(required = false) String genre,
                                                        @RequestParam(required = false) Integer publishedYear) {
        return ResponseEntity.ok(bookService.search(title, genre, publishedYear));
    }

    // GET /api/books/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    // POST /api/books
    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookRequestDTO request) {
        BookResponseDTO created = bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/books/{id}
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody BookRequestDTO request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    // DELETE /api/books/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
