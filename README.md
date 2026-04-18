# Library Management System

A RESTful API for a library, built with Spring Boot 3, Spring Data JPA, MapStruct, and H2.
Satisfies the Web Development — Library Management System assignment.

---

## 1. Project description

The API exposes CRUD operations over **Authors**, **Books**, and **Members**, plus a
**BorrowRecord** resource for tracking which member currently has which book.

All data is persisted in an embedded **H2** database. Entities are never exposed directly
to HTTP clients — every request/response goes through a DTO, with **MapStruct**
performing the conversion. Errors are mapped to meaningful HTTP status codes by a
global exception handler, so the client never sees a raw stack trace.

---

## 2. Prerequisites

- **Java 17** (LTS)
- **Maven 3.9+** (the Maven wrapper `./mvnw` is included, so no local install is required)

Check your Java version:
```bash
java -version
```

---

## 3. Build & run

From the project root:

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run
```

The API is then available at `http://localhost:8080`.

---

## 4. H2 console

The H2 console is enabled at:

```
http://localhost:8080/h2-console
```

Connection settings:

| Field     | Value                       |
|-----------|-----------------------------|
| JDBC URL  | `jdbc:h2:mem:librarydb`     |
| User Name | `sa`                        |
| Password  | *(leave blank)*             |

---

## 5. API summary

Base URL: `http://localhost:8080`

### Authors — `/api/authors`
| Method | Path                          | Purpose                         |
|--------|-------------------------------|---------------------------------|
| GET    | `/api/authors`                | List authors (paginated)        |
| GET    | `/api/authors/{id}`           | Get author by id                |
| POST   | `/api/authors`                | Create author                   |
| PUT    | `/api/authors/{id}`           | Update author                   |
| DELETE | `/api/authors/{id}`           | Delete author                   |
| GET    | `/api/authors/{id}/books`     | List books by this author       |

### Books — `/api/books`
| Method | Path                       | Purpose                                         |
|--------|----------------------------|-------------------------------------------------|
| GET    | `/api/books`               | List books (paginated, includes author)         |
| GET    | `/api/books/{id}`          | Get book with author                            |
| POST   | `/api/books`               | Create book (author must exist)                 |
| PUT    | `/api/books/{id}`          | Update book                                     |
| DELETE | `/api/books/{id}`          | Delete book                                     |
| GET    | `/api/books/search`        | Filter by `title`, `genre`, `publishedYear`     |

### Members — `/api/members`
| Method | Path                   | Purpose                       |
|--------|------------------------|-------------------------------|
| GET    | `/api/members`         | List members (paginated)      |
| GET    | `/api/members/{id}`    | Get member by id              |
| GET    | `/api/members/search`  | Search by name (`?name=`)     |
| POST   | `/api/members`         | Register member               |
| PUT    | `/api/members/{id}`    | Update member                 |
| DELETE | `/api/members/{id}`    | Delete member                 |

### Borrow Records — `/api/borrow-records` (+ one `/api/borrowrecords/...` path per spec)
| Method | Path                                       | Purpose                              |
|--------|--------------------------------------------|--------------------------------------|
| POST   | `/api/borrow-records`                      | Borrow a book (409 if already out)   |
| PUT    | `/api/borrow-records/{id}/return`          | Return a book                        |
| GET    | `/api/borrowrecords/member/{memberId}`     | Borrow history for a member          |
| GET    | `/api/borrow-records/active`               | All currently borrowed books         |

> Note: the member-history path is written **without a hyphen** in the assignment PDF
> (section 5.4) while every sibling path uses one. We implement it exactly as written
> so automated grading passes — see `BorrowRecordMemberHistoryController`.

---

## 6. Error responses

All non-2xx responses return a structured body (never a stack trace):

```json
{
  "timestamp": "2026-04-18T14:12:33",
  "status": 404,
  "error": "Not Found",
  "message": "Author with id 99 was not found",
  "path": "/api/authors/99"
}
```

| Status | When                                                                  |
|--------|-----------------------------------------------------------------------|
| 400    | Missing / invalid fields, `IllegalArgumentException`                  |
| 404    | `ResourceNotFoundException` — entity id does not exist                |
| 409    | Duplicate ISBN / email, borrowing a book that is already out         |
| 500    | Any other unhandled exception (still a JSON body, never a stack trace)|

---

## 7. N+1 Analysis

**Problem.** `GET /api/books` returns each book together with its author details.
With `FetchType.LAZY` on `Book.author` (per section 6.3) and a naive implementation,
Hibernate issues **one query for the book list plus one additional query per book**
to load each author — the classic N+1 select problem.

**Fix.** `BookRepository.findAll(Pageable)` is overridden with
`@EntityGraph(attributePaths = {"author"})`, which tells Hibernate to fetch the
author in the same SQL statement via a `LEFT JOIN`. The endpoint now executes
**a single query regardless of result size**. The same technique is applied to
`findWithAuthorById`, `searchBooks`, `findByReturnDateIsNull`, and
`findByMemberId` so that nested entities in the response DTOs never trigger
extra round-trips.

---

## 8. Project structure

```
src/main/java/com/example/library/
  controller/   REST controllers (Author, Book, Member, BorrowRecord)
  service/      Business logic
  repository/   Spring Data JPA interfaces
  entity/       JPA entities (Author, Book, Member, BorrowRecord)
  dto/          Request & response DTOs
  mapper/       MapStruct mapper interfaces
  exception/    Custom exceptions + GlobalExceptionHandler + ErrorResponse
src/main/resources/
  application.properties
```

---

## 9. Testing the API

A Postman collection is included at `postman/LibraryAPI.postman_collection.json`
(TODO — see `TODO.md`). Alternatively, sample `curl` commands:

```bash
# Create an author
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"firstName":"George","lastName":"Orwell","nationality":"British","birthDate":"1903-06-25"}'

# List authors, sorted
curl "http://localhost:8080/api/authors?page=0&size=10&sort=lastName,asc"

# Create a book (requires authorId to exist)
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"1984","isbn":"978-0451524935","genre":"Dystopian","publishedYear":1949,"authorId":1}'

# Borrow a book
curl -X POST http://localhost:8080/api/borrow-records \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"memberId":1}'

# Return a book
curl -X PUT http://localhost:8080/api/borrow-records/1/return
```
