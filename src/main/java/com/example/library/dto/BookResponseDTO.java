package com.example.library.dto;

/**
 * Response DTO for Book. Includes nested AuthorResponseDTO so the client
 * sees full author details on GET /api/books/{id} (section 5.2).
 */
public class BookResponseDTO {

    private Long id;
    private String title;
    private String isbn;
    private String genre;
    private Integer publishedYear;
    private AuthorResponseDTO author;

    public BookResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public AuthorResponseDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorResponseDTO author) {
        this.author = author;
    }
}
