package com.example.library.controller;

import com.example.library.dto.AuthorRequestDTO;
import com.example.library.dto.AuthorResponseDTO;
import com.example.library.service.AuthorService;
import com.example.library.service.BookService;
import com.example.library.dto.BookResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    // GET /api/authors?page=0&size=20&sort=lastName,asc
    @GetMapping
    public ResponseEntity<Page<AuthorResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(authorService.findAll(pageable));
    }

    // GET /api/authors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.findById(id));
    }

    // POST /api/authors
    @PostMapping
    public ResponseEntity<AuthorResponseDTO> create(@Valid @RequestBody AuthorRequestDTO request) {
        AuthorResponseDTO created = authorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/authors/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody AuthorRequestDTO request) {
        return ResponseEntity.ok(authorService.update(id, request));
    }

    // DELETE /api/authors/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/authors/{id}/books
    // Delegates to BookService so the "books by author" query lives next to the other Book logic.
    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookResponseDTO>> getBooksByAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findByAuthorId(id));
    }
}
