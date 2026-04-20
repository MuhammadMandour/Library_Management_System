INSERT INTO authors (id, first_name, last_name, nationality, birth_date) VALUES
  (1001, 'George', 'Orwell', 'British', '1903-06-25'),
  (1002, 'Jane', 'Austen', 'British', '1775-12-16'),
  (1003, 'Naguib', 'Mahfouz', 'Egyptian', '1911-12-11');

INSERT INTO books (id, title, isbn, genre, published_year, author_id) VALUES
  (2001, '1984', '9780451524935', 'Dystopian', 1949, 1001),
  (2002, 'Animal Farm', '9780451526342', 'Political Satire', 1945, 1001),
  (2003, 'Pride and Prejudice', '9780141439518', 'Romance', 1813, 1002),
  (2004, 'Midaq Alley', '9780385264662', 'Literary Fiction', 1947, 1003);

INSERT INTO members (id, first_name, last_name, email, phone_number, membership_date) VALUES
  (3001, 'Sara', 'Hassan', 'sara.hassan@example.com', '+966500000010', CURRENT_TIMESTAMP),
  (3002, 'Omar', 'Ali', 'omar.ali@example.com', '+966500000011', CURRENT_TIMESTAMP);
