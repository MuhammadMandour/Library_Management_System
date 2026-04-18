package com.example.library.service;

import com.example.library.dto.BookRequestDTO;
import com.example.library.dto.BookResponseDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.exception.DuplicateResourceException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.mapper.BookMapper;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return bookRepository.findAll(pageable)
                .map(bookMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public BookResponseDTO findById(Long id) {
        return bookRepository.findWithAuthorById(id)
                .map(bookMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    public BookResponseDTO create(BookRequestDTO request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author", request.getAuthorId()));

        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Book with ISBN " + request.getIsbn() + " already exists");
        }

        Book book = bookMapper.toEntity(request);
        book.setAuthor(author);
        Book saved = bookRepository.save(book);
        return bookMapper.toResponseDTO(saved);
    }

    public BookResponseDTO update(Long id, BookRequestDTO request) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));

        // If authorId changed, re-resolve author
        if (!existing.getAuthor().getId().equals(request.getAuthorId())) {
            Author author = authorRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author", request.getAuthorId()));
            existing.setAuthor(author);
        }

        // Check ISBN uniqueness if it changed
        if (!existing.getIsbn().equals(request.getIsbn()) && 
            bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Book with ISBN " + request.getIsbn() + " already exists");
        }

        bookMapper.updateEntityFromDTO(request, existing);
        Book saved = bookRepository.save(existing);
        return bookMapper.toResponseDTO(saved);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", id);
        }
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> search(String title, String genre, Integer publishedYear) {
        List<Book> books = bookRepository.searchBooks(title, genre, publishedYear);
        return bookMapper.toResponseDTOList(books);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> findByAuthorId(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author", authorId);
        }
        List<Book> books = bookRepository.findByAuthorId(authorId);
        return bookMapper.toResponseDTOList(books);
    }
}

