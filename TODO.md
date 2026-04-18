# Team TODO List

Deadline: **Monday 20 April 2026**

## What's already done (use as reference)

- Project setup: `pom.xml`, `application.properties`, H2 console
- All 4 JPA entities (Author, Book, Member, BorrowRecord) with relationships + LAZY fetching
- All 4 repositories with derived queries + `@EntityGraph` for N+1 fix
- All exception classes + `GlobalExceptionHandler` (maps to 400/404/409/500)
- **Author stack is fully implemented** — DTOs, mapper, service, controller. Use it as the template for the others.
- `README.md` with the N+1 Analysis section
- Both BorrowRecord controllers (one per spec path) — only the service methods they call are stubbed

## What needs to be done

### 1. BookService — assignee: **________**
File: `src/main/java/com/example/library/service/BookService.java`

- [ ] `findAll(Pageable)` → pageable list, author included
- [ ] `findById(Long)` → use `bookRepository.findWithAuthorById`
- [ ] `create(BookRequestDTO)` → validate author exists, check duplicate ISBN
- [ ] `update(Long, BookRequestDTO)` → remember to re-resolve author if `authorId` changes
- [ ] `delete(Long)`
- [ ] `search(title, genre, publishedYear)` → delegate to `bookRepository.searchBooks`
- [ ] `findByAuthorId(Long)` → used by `/api/authors/{id}/books`

Reference: `AuthorService.java` (same shape, simpler).

### 2. BookMapper — assignee: **________**
File: `src/main/java/com/example/library/mapper/BookMapper.java`

- [ ] In `toEntity`, add `@Mapping(target = "author", ignore = true)` — the service sets the author after fetching it by id.
- [ ] Verify `toResponseDTO` correctly maps `Book.author` → `BookResponseDTO.author` (should work automatically via `uses = AuthorMapper.class`).

### 3. MemberService + MemberMapper — assignee: **________**
Files:
- `src/main/java/com/example/library/service/MemberService.java`
- `src/main/java/com/example/library/mapper/MemberMapper.java`

- [ ] Fill in all 6 service methods (see TODOs in the file)
- [ ] For `create`, check duplicate email → throw `DuplicateResourceException`
- [ ] Mapper should be pure annotation-based (no custom logic needed)

### 4. BorrowRecordService + BorrowRecordMapper — assignee: **________**
Files:
- `src/main/java/com/example/library/service/BorrowRecordService.java`
- `src/main/java/com/example/library/mapper/BorrowRecordMapper.java`

- [ ] `borrowBook`: fetch book + member, check if book is already borrowed (`findByBookIdAndReturnDateIsNull`), throw `BookAlreadyBorrowedException` if it is, then save new record.
- [ ] `returnBook`: fetch record, reject if already returned, set `returnDate = now()`, save.
- [ ] `findByMember`: validate member exists, then return list.
- [ ] `findActive`: return `borrowRecordRepository.findByReturnDateIsNull()`.

This one has the real business logic — read the javadoc in `BorrowRecordService.java` carefully.

### 5. Postman collection + sample data — assignee: **________**
- [ ] Create a Postman collection covering **every** endpoint in section 5 of the spec.
- [ ] Save as `postman/LibraryAPI.postman_collection.json`.
- [ ] Add environment variables for `baseUrl`, a created author id, a created book id, etc.
- [ ] Include at least one "happy path" and one error case (404, 409) per resource.

Optional: a `data.sql` file in `src/main/resources/` that pre-seeds a few authors + books so the reviewer sees data immediately after starting the app.

### 6. Final polish — everyone, before submission
- [ ] Run `./mvnw clean package` from a fresh clone — it MUST compile.
- [ ] Fill in `groups.txt` with the group number and all member names.
- [ ] Manually exercise every endpoint at least once via Postman.
- [ ] Verify `http://localhost:8080/h2-console` opens and the tables exist.
- [ ] Push to the group GitHub repo and drop the link in Google Classroom.

---

## Tips for teammates picking up skeletons

1. **Read AuthorService.java first.** Every other service follows the same pattern.
2. **Constructor injection only** — the fields are already `final` and the constructor is already there. Just fill in method bodies.
3. **Throw the right exception**, don't manually build `ResponseEntity`s in services. The handler maps them:
   - id not found → `throw new ResourceNotFoundException("Author", id);`
   - duplicate field → `throw new DuplicateResourceException("Email already in use");`
   - book already out → `throw new BookAlreadyBorrowedException(bookId);`
4. **Keep controllers thin.** Every controller method should be ~1 line of logic + return.
5. **When in doubt, follow the N+1 fix pattern** — if a new repository method returns entities whose lazy fields are read later in the service or mapper, add `@EntityGraph`.
