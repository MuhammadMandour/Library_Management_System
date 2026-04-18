package com.example.library.service;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO(team - assignee: ____):
 * Implement the Book service. Use AuthorService as a reference pattern.
 *
 * Methods needed:
 *  - findAll(Pageable)                     -> GET /api/books
 *  - findById(Long id)                     -> GET /api/books/{id}   (use bookRepository.findWithAuthorById)
 *  - create(BookRequestDTO)                -> POST /api/books       (author must exist -> else 404)
 *  - update(Long id, BookRequestDTO)       -> PUT /api/books/{id}
 *  - delete(Long id)                       -> DELETE /api/books/{id}
 *  - search(String title, String genre, Integer publishedYear) -> GET /api/books/search
 *  - findByAuthorId(Long authorId)         -> GET /api/authors/{id}/books
 *
 * Things to remember:
 *  - Throw ResourceNotFoundException when an id is not found (-> 404).
 *  - If authorId in request does not exist, throw ResourceNotFoundException("Author", authorId).
 *  - Duplicate ISBN -> throw DuplicateResourceException OR let DataIntegrityViolationException bubble up (both map to 409).
 *    Preferred: check with bookRepository.findByIsbn(...) first and throw the nicer message.
 *  - Keep @Transactional on the class and @Transactional(readOnly = true) on read methods.
 */
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> findAll(Pageable pageable) {
        // TODO(team): use bookRepository.findAll(pageable) and map to DTO.
        throw new UnsupportedOperationException("findAll not implemented yet");
    }

    @Transactional(readOnly = true)
    public BookResponseDTO findById(Long id) {
        // TODO(team): use bookRepository.findWithAuthorById(id).orElseThrow(...)
        throw new UnsupportedOperationException("findById not implemented yet");
    }

    public BookResponseDTO create(BookRequestDTO request) {
        // TODO(team):
        //   1. Look up Author by request.getAuthorId() -> else throw ResourceNotFoundException
        //   2. Check bookRepository.findByIsbn(request.getIsbn()).isPresent() -> throw DuplicateResourceException
        //   3. Map to entity, set author, save, return DTO
        throw new UnsupportedOperationException("create not implemented yet");
    }

    public BookResponseDTO update(Long id, BookRequestDTO request) {
        // TODO(team): similar pattern to AuthorService.update, but also re-resolve Author if authorId changed.
        throw new UnsupportedOperationException("update not implemented yet");
    }

    public void delete(Long id) {
        // TODO(team): existsById check + deleteById.
        throw new UnsupportedOperationException("delete not implemented yet");
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> search(String title, String genre, Integer publishedYear) {
        // TODO(team): delegate to bookRepository.searchBooks(title, genre, publishedYear)
        throw new UnsupportedOperationException("search not implemented yet");
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findByAuthorId(Long authorId) {
        // TODO(team):
        //   1. If !authorRepository.existsById(authorId) -> throw ResourceNotFoundException("Author", authorId)
        //   2. return bookRepository.findByAuthorId(authorId) mapped to DTO list
        throw new UnsupportedOperationException("findByAuthorId not implemented yet");
    }
}
