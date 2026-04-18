package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // N+1 FIX: eagerly fetch the author in the same query when listing books.
    // Without @EntityGraph, Hibernate would issue 1 query for the book list + 1 per book for the author.
    @Override
    @EntityGraph(attributePaths = {"author"})
    Page<Book> findAll(Pageable pageable);

    // Same idea for single book lookup (used by GET /api/books/{id})
    @EntityGraph(attributePaths = {"author"})
    Optional<Book> findWithAuthorById(Long id);

    List<Book> findByAuthorId(Long authorId);

    Optional<Book> findByIsbn(String isbn);

    // Search by optional title / genre / publishedYear.
    // Uses JPQL with nullable parameters: if a param is null it is skipped from the filter.
    @Query("""
            SELECT b FROM Book b
            WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:genre IS NULL OR LOWER(b.genre) = LOWER(:genre))
              AND (:publishedYear IS NULL OR b.publishedYear = :publishedYear)
            """)
    @EntityGraph(attributePaths = {"author"})
    List<Book> searchBooks(@Param("title") String title,
                           @Param("genre") String genre,
                           @Param("publishedYear") Integer publishedYear);
}
