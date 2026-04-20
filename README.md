# Library Management System

A robust RESTful API for managing library operations, built with **Spring Boot 3**, **Spring Data JPA**, **MapStruct**, and **H2 Database**.
This project satisfies the Web Development — Library Management System assignment.

##  Table of Contents

- [Team Members](#team-members)
- [Project Description](#1-project-description)
- [Project Structure](#project-structure)
- [Prerequisites](#2-prerequisites)
- [Build & Run](#3-build--run)
- [H2 Console](#4-h2-console)
- [API Summary](#5-api-summary)
- [Error Responses](#6-error-responses)
- [N+1 Analysis](#7-n1-analysis)
- [Testing with curl](#8-testing-with-curl-commands)
- [Example Workflow](#9-example-workflow)
- [Postman Collection](#10-postman-collection)
- [Troubleshooting](#11-troubleshooting)

---

## Team Members

| ID | Group | Name |
|---|---|---|
| 36 | 4 | محمد خالد عبدالحميد |
| 39 | 4 | ملك سعد عبدالله |
| 46 | 4 | ياسمين بدر ضياء الدين |
| 11 | 4 | اماني عثمان |
| 14 | 4 | اوليفيا مرقص |

---
## Project Structure

```
Library_Management_System/
├── src/
│   └── main/
│       ├── java/com/example/library/
│       │   ├── LibraryApplication.java              # Spring Boot entry point
│       │   ├── controller/                          # REST API endpoints
│       │   │   ├── AuthorController.java
│       │   │   ├── BookController.java
│       │   │   ├── MemberController.java
│       │   │   ├── BorrowRecordController.java
│       │   │   └── BorrowRecordMemberHistoryController.java
│       │   ├── service/                             # Business logic layer
│       │   │   ├── AuthorService.java
│       │   │   ├── BookService.java
│       │   │   ├── MemberService.java
│       │   │   └── BorrowRecordService.java
│       │   ├── repository/                          # Data persistence layer
│       │   │   ├── AuthorRepository.java
│       │   │   ├── BookRepository.java
│       │   │   ├── MemberRepository.java
│       │   │   └── BorrowRecordRepository.java
│       │   ├── entity/                              # JPA entity classes
│       │   │   ├── Author.java
│       │   │   ├── Book.java
│       │   │   ├── Member.java
│       │   │   └── BorrowRecord.java
│       │   ├── dto/                                 # Data Transfer Objects
│       │   │   ├── AuthorRequestDTO.java
│       │   │   ├── AuthorResponseDTO.java
│       │   │   ├── BookRequestDTO.java
│       │   │   ├── BookResponseDTO.java
│       │   │   ├── MemberRequestDTO.java
│       │   │   ├── MemberResponseDTO.java
│       │   │   ├── BorrowRecordRequestDTO.java
│       │   │   └── BorrowRecordResponseDTO.java
│       │   ├── mapper/                              # MapStruct mappers for DTO conversion
│       │   │   ├── AuthorMapper.java
│       │   │   ├── BookMapper.java
│       │   │   ├── MemberMapper.java
│       │   │   └── BorrowRecordMapper.java
│       │   └── exception/                           # Custom exceptions & global handler
│       │       ├── ResourceNotFoundException.java
│       │       ├── BookAlreadyBorrowedException.java
│       │       ├── DuplicateResourceException.java
│       │       ├── ErrorResponse.java
│       │       └── GlobalExceptionHandler.java
│       └── resources/
│           └── application.properties               # Spring Boot configuration
├── pom.xml                                          # Maven dependencies
├── mvnw / mvnw.cmd                                  # Maven wrapper
├── target/                                          # Build output (generated)
└── README.md                                        # This file
```

### Layer Overview

- **Controller**: Handles HTTP requests and responses
- **Service**: Contains business logic and validation
- **Repository**: Manages database queries using Spring Data JPA
- **Entity**: Represents database tables
- **DTO**: Transfers data between controller and client (external API)
- **Mapper**: Converts between Entity and DTO using MapStruct
- **Exception**: Custom exceptions and centralized error handling

---
## 1. Project description

## 1. Project Description

The API exposes complete CRUD operations over **Authors**, **Books**, and **Members**, plus a
**BorrowRecord** resource for tracking which member currently has which book.

### Key Features

-  **Resource Management**: Full CRUD operations for Authors, Books, and Members
-  **Borrow Management**: Track book borrowing and returns with member history
-  **Data Persistence**: All data stored in embedded **H2 Database**
-  **Clean Architecture**: Entities never exposed directly; all requests/responses use **DTOs**
-  **Automatic Mapping**: **MapStruct** performs efficient Entity ↔ DTO conversion
-  **Error Handling**: Centralized global exception handler returns meaningful HTTP status codes
-  **Performance Optimized**: **@EntityGraph** eliminates N+1 query problems
-  **Pagination**: List endpoints support pagination, sorting, and filtering
-  **Input Validation**: Unique ISBN/email constraints, duplicate prevention

### Technology Stack

| Component | Technology | Purpose |
|-----------|-----------|---------|
| **Framework** | Spring Boot 3 | REST API & dependency management |
| **ORM** | Spring Data JPA & Hibernate | Object-relational mapping |
| **Database** | H2 (Embedded) | In-memory SQL database |
| **Mapping** | MapStruct | Entity ↔ DTO conversion |
| **Build Tool** | Maven 3.9+ | Project build & dependency resolution |
| **Runtime** | Java 21 (LTS) | Runtime environment |

---

## 2. Prerequisites

- **Java 21** (LTS)
- **Maven 3.9+** (the Maven wrapper `./mvnw` is included, so no local install is required)

Check your Java version:
```bash
java -version
```

---

## 3. Build & Run

From the project root directory, use one of the commands below:

### On Linux / macOS:
```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run
```

### On Windows (PowerShell / CMD):
```cmd
# Build
mvnw.cmd clean package

# Run
mvnw.cmd spring-boot:run
```

The API will be available at `http://localhost:8080`

**Verify the application is running:**
```bash
curl http://localhost:8080/api/authors
```

---

## 4. H2 Console

The H2 Database Console is enabled for easy database inspection and SQL query execution.

### Access the Console

1. Start the application (see [Build & Run](#3-build--run))
2. Open your browser and navigate to: `http://localhost:8080/h2-console`

### Connection Configuration

| Setting | Value |
|---------|-------|
| **JDBC URL** | `jdbc:h2:mem:librarydb` |
| **User Name** | `sa` |
| **Password** | *(leave empty)* |

Click **Connect** to access the database. You can browse tables, run SQL queries, and inspect data in real-time.

---

## 5. API Summary

## 5. API Summary

**Base URL**: `http://localhost:8080`

###  Authors — `/api/authors`
Manage library authors and their published books.

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/authors` | List all authors (paginated) |
| GET | `/api/authors/{id}` | Get author details by ID |
| POST | `/api/authors` | Create a new author |
| PUT | `/api/authors/{id}` | Update author information |
| DELETE | `/api/authors/{id}` | Delete an author |
| GET | `/api/authors/{id}/books` | Get all books by an author |

###  Books — `/api/books`
Manage library books with ISBN, genre, and publication year tracking.

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/books` | List all books with author details (paginated) |
| GET | `/api/books/{id}` | Get book details with author info |
| POST | `/api/books` | Add a new book (author must exist) |
| PUT | `/api/books/{id}` | Update book information |
| DELETE | `/api/books/{id}` | Delete a book |
| GET | `/api/books/search` | Search books by `title`, `genre`, or `publishedYear` |

###  Members — `/api/members`
Register and manage library members.

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/members` | List all members (paginated) |
| GET | `/api/members/{id}` | Get member profile by ID |
| GET | `/api/members/search` | Search members by name (`?name=`) |
| POST | `/api/members` | Register a new member |
| PUT | `/api/members/{id}` | Update member information |
| DELETE | `/api/members/{id}` | Delete a member |

###  Borrow Records — `/api/borrow-records` & `/api/borrowrecords`
Track book borrowing and returns, including member borrow history.

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/borrow-records` | Borrow a book (returns 409 if already borrowed) |
| PUT | `/api/borrow-records/{id}/return` | Return a borrowed book |
| GET | `/api/borrowrecords/member/{memberId}` | Get complete borrow history for a member |
| GET | `/api/borrow-records/active` | List all currently borrowed books |

> **Note**: The member-history path uses `/api/borrowrecords/` (without hyphen) per spec assignment section 5.4, while other paths use hyphens. See `BorrowRecordMemberHistoryController` for the special endpoint.

---

## 6. Error Responses

## 6. Error Responses

All error responses return a structured JSON body (never raw exception stack traces). This ensures consistent error handling across all clients.

### Example Error Response

```json
{
  "timestamp": "2026-04-18T14:12:33",
  "status": 404,
  "error": "Not Found",
  "message": "Author with id 99 was not found",
  "path": "/api/authors/99"
}
```

### HTTP Status Codes

| Status | Exception | When |
|--------|-----------|------|
| **200** |  Success | Request completed successfully |
| **400** | Bad Request | Missing/invalid request fields, `IllegalArgumentException` |
| **404** | `ResourceNotFoundException` | Entity ID does not exist in database |
| **409** | `BookAlreadyBorrowedException` or `DuplicateResourceException` | Duplicate ISBN/email, or attempting to borrow an already-checked-out book |
| **500** | General Exception | Any unhandled exception (returns JSON, never stack trace) |

---

## 7. N+1 Analysis

## 7. N+1 Analysis

### The Problem

When fetching books via `GET /api/books`, the response includes author details for each book.
With `FetchType.LAZY` on `Book.author` (per assignment section 6.3) and a naive implementation,
Hibernate issues:
- 1 query to fetch the book list
- **1 additional query per book** to fetch each author

Result: **N+1 queries** — extremely inefficient for large datasets!

### The Solution

We override `BookRepository.findAll(Pageable)` with `@EntityGraph(attributePaths = {"author"})`.
This tells Hibernate to fetch the author **in the same SQL statement** via a `LEFT JOIN`.

**Result**: One query regardless of result size 

### Applied Optimizations

The same `@EntityGraph` technique is applied to:
- `BookRepository.findAll()` — list all books
- `BookRepository.findWithAuthorById()` — fetch single book with author
- `BookRepository.searchBooks()` — search with filters
- `BorrowRecordRepository.findByReturnDateIsNull()` — active borrow records
- `BorrowRecordRepository.findByMemberId()` — member borrow history

This ensures that **nested entities never trigger extra round-trips** to the database.

---

## 8. Testing with curl Commands

Ensure the application is running at `http://localhost:8080` before executing these commands.

###  Prerequisites for Testing

```bash
# Verify the API is responding
curl http://localhost:8080/api/authors

# Expected response: empty list (initially)
# {"content":[],"pageable":...,"empty":true}
```

### Authors Endpoints

#### Create an Author
```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ahmed",
    "lastName": "Ibrahim",
    "nationality": "Egyptian",
    "birthDate": "1980-05-15"
  }'
```

#### Get All Authors (Paginated)
```bash
curl http://localhost:8080/api/authors?page=0&size=10&sort=firstName,asc
```

#### Get Author by ID
```bash
curl http://localhost:8080/api/authors/1
```

#### Get Books by Author
```bash
curl http://localhost:8080/api/authors/1/books
```

#### Update Author
```bash
curl -X PUT http://localhost:8080/api/authors/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Ahmed",
    "lastName": "Ibrahim",
    "nationality": "Egypt",
    "birthDate": "1980-05-15"
  }'
```

#### Delete Author
```bash
curl -X DELETE http://localhost:8080/api/authors/1
```

---

### Books Endpoints

#### Create a Book
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Arabic Literature",
    "isbn": "978-1234567890",
    "publishedYear": 2020,
    "genre": "Literature",
    "authorId": 1
  }'
```

#### Get All Books (with Author details)
```bash
curl http://localhost:8080/api/books?page=0&size=10
```

#### Get Book by ID
```bash
curl http://localhost:8080/api/books/1
```

#### Search Books
```bash
# Search by title
curl "http://localhost:8080/api/books/search?title=Arabic"

# Search by genre
curl "http://localhost:8080/api/books/search?genre=Literature"

# Search by published year
curl "http://localhost:8080/api/books/search?publishedYear=2020"
```

#### Update Book
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The Arabic Literature",
    "isbn": "978-1234567890",
    "publishedYear": 2021,
    "genre": "Literature"
  }'
```

#### Delete Book
```bash
curl -X DELETE http://localhost:8080/api/books/1
```

---

### Members Endpoints

#### Register a Member
```bash
curl -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Mohammed",
    "lastName": "Ali",
    "email": "mohammed.ali@example.com",
    "phoneNumber": "01012345678"
  }'
```

#### Get All Members (Paginated)
```bash
curl http://localhost:8080/api/members?page=0&size=10
```

#### Get Member by ID
```bash
curl http://localhost:8080/api/members/1
```

#### Search Members by Name
```bash
curl "http://localhost:8080/api/members/search?name=Mohammed"
```

#### Update Member
```bash
curl -X PUT http://localhost:8080/api/members/1 \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Mohammed",
    "lastName": "Ali",
    "email": "mohammed.ali@example.com",
    "phoneNumber": "01098765432"
  }'
```

#### Delete Member
```bash
curl -X DELETE http://localhost:8080/api/members/1
```

---

### Borrow Records Endpoints

#### Borrow a Book
```bash
curl -X POST http://localhost:8080/api/borrow-records \
  -H "Content-Type: application/json" \
  -d '{
    "bookId": 1,
    "memberId": 1
  }'
```

#### Return a Book
```bash
curl -X PUT http://localhost:8080/api/borrow-records/1/return
```

#### Get Member Borrow History
```bash
curl http://localhost:8080/api/borrowrecords/member/1
```

#### Get All Active Borrowed Books
```bash
curl http://localhost:8080/api/borrow-records/active
```

---

## 9. Example Workflow

A complete scenario: create an author, add books, register a member, and borrow books.

```bash
# 1. Create Author
AUTHOR=$(curl -s -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Naguib","lastName":"Mahfouz","nationality":"Egyptian","birthDate":"1911-12-12"}')
AUTHOR_ID=$(echo $AUTHOR | grep -o '"id":[0-9]*' | head -1 | cut -d: -f2)

# 2. Create Book
BOOK=$(curl -s -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"Palace Walk\",\"isbn\":\"978-0385339742\",\"publishedYear\":1956,\"genre\":\"Fiction\",\"authorId\":$AUTHOR_ID}")
BOOK_ID=$(echo $BOOK | grep -o '"id":[0-9]*' | head -1 | cut -d: -f2)

# 3. Register Member
MEMBER=$(curl -s -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Fatima","lastName":"Hassan","email":"fatima@example.com","phoneNumber":"01012345678"}')
MEMBER_ID=$(echo $MEMBER | grep -o '"id":[0-9]*' | head -1 | cut -d: -f2)

# 4. Borrow Book
curl -X POST http://localhost:8080/api/borrow-records \
  -H "Content-Type: application/json" \
  -d "{\"bookId\":$BOOK_ID,\"memberId\":$MEMBER_ID}"

# 5. View Borrow History
curl http://localhost:8080/api/borrowrecords/member/$MEMBER_ID

# 6. Return Book
curl -X PUT http://localhost:8080/api/borrow-records/1/return
```

---

## 10. Postman Collection

A ready-to-use Postman collection is provided to test all API endpoints of the Library Management System.

The collection file is located at:

main/postman/LibraryAPI.postman_collection.json


### How to Use

1. **Open Postman** application  
2. Click **Import** (top-left corner)
3. Select **File**
4. Choose: `main/postman/LibraryAPI.postman_collection.json`
5. Click **Import**
6. The collection **Library API** will appear in your workspace

###  Running the Tests

- **Open the imported collection** named **Library API**
- **Click Run Collection** button
- Execute all requests **sequentially or individually**
- **View results** including status codes and response times

### What's Included

 **Authors API** — Create, Read, Update, Delete operations  
 **Books API** — CRUD operations with duplicate ISBN validation  
 **Members API** — CRUD operations with duplicate email validation  
 **Borrow Records Workflow** — Borrow, return, history, and conflict scenarios  
 **Cleanup Requests** — Reset test data between test runs  
 **Automated Assertions** — Status code and response validation

## 11. Troubleshooting

| Issue | Cause | Solution |
|-------|-------|----------|
| Port 8080 already in use | Another process is using port 8080 | Kill the Java process: `Get-Process -Name java \| Stop-Process -Force` (PowerShell) or change port in `application.properties` |
| Compilation error: mappers not found | MapStruct implementations not generated | Run `./mvnw clean compile` to regenerate MapStruct implementations |
| H2 console not accessible | H2 console disabled in config | Verify `spring.h2.console.enabled=true` in `application.properties` |
| ISBN or email already exists | Duplicate constraint violation | Use unique values (ISBN/email) in create/update requests |
| Cannot borrow book already checked out | Book is currently borrowed | The book must be returned first; verify status with `GET /api/borrow-records/active` |
| Tests fail after restart | Database reset | H2 in-memory database clears on restart (expected behavior). Re-create test data. |

### Common Fixes

**Build fails after pulling updates:**
```bash
./mvnw clean compile
```

**Clear all data and start fresh:**
1. Restart the application (H2 in-memory database resets)
2. Or query H2 console directly to run: `DELETE FROM BORROW_RECORD; DELETE FROM BOOK; DELETE FROM MEMBER; DELETE FROM AUTHOR;`

**View application logs:**
```bash
# The application outputs logs to console. To save logs to a file:
./mvnw spring-boot:run > application.log 2>&1
```

---

##  Additional Resources

- **Spring Boot Documentation**: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
- **Spring Data JPA**: [https://spring.io/projects/spring-data-jpa](https://spring.io/projects/spring-data-jpa)
- **MapStruct**: [https://mapstruct.org/](https://mapstruct.org/)
- **H2 Database**: [https://www.h2database.com/](https://www.h2database.com/)

---

**Last Updated**: April 20, 2026
