# Library Management System

A RESTful API for a library, built with Spring Boot 3, Spring Data JPA, MapStruct, and H2.
Satisfies the Web Development — Library Management System assignment.

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

## 1. Project description

The API exposes CRUD operations over **Authors**, **Books**, and **Members**, plus a
**BorrowRecord** resource for tracking which member currently has which book.

All data is persisted in an embedded **H2** database. Entities are never exposed directly
to HTTP clients — every request/response goes through a DTO, with **MapStruct**
performing the conversion. Errors are mapped to meaningful HTTP status codes by a
global exception handler, so the client never sees a raw stack trace.

---

## 2. Prerequisites

- **Java 21** (LTS)
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
./mvnw clean package or  .\mvnw.cmd clean package          

# Run
./mvnw spring-boot:run or  .\mvnw.cmd  spring-boot:run              
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

## 8. Testing with curl Commands

Ensure the application is running at `http://localhost:8080` before executing these commands.

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

1. Open :contentReference[oaicite:0]{index=0}  
2. Click **Import** (top-left corner)
3. Select **File**
4. Choose:
main/postman/LibraryAPI.postman_collection.json

5. Click **Import**
6. The collection **Library API** will appear in your workspace

###  Running the Tests

- Open the imported collection
- Click **Run Collection**
- Execute all requests sequentially or run them individually

###  What’s Included

- Authors API (Create, Read, Update, Delete)
- Books API (CRUD operations + duplicate ISBN validation)
- Members API (CRUD operations + duplicate email validation)
- Borrow Records workflow (borrow, return, history, conflict cases)
- Cleanup requests to reset test data
- Automated test assertions (status codes and response validation)

## 11. Troubleshooting

| Issue | Solution |
|-------|----------|
| Port 8080 already in use | Kill Java process: `Get-Process -Name java \| Stop-Process -Force` or change port in `application.properties` |
| Compilation error: mappers not found | Run `./mvnw clean compile` to regenerate MapStruct implementations |
| H2 console not accessible | Ensure `spring.h2.console.enabled=true` in `application.properties` |
| ISBN or email already exists | Use unique values in create/update requests |
| Cannot borrow book already checked out | Book must be returned first; check with `GET /api/borrow-records/active` |
