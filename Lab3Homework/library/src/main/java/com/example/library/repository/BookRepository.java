package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BookRepository {

    private final Map<Long, Book> books =
            new ConcurrentHashMap<>();

    public BookRepository() {
        books.put(101L,
                new Book(101L, "Clean Code"));
        books.put(102L,
                new Book(102L, "Effective Java"));
        books.put(103L,
                new Book(103L, "Spring in Action"));
        books.put(104L,
                new Book(104L, "Design Patterns"));
        books.put(105L,
                new Book(105L, "Java Concurrency in Practice"));
        books.put(106L,
                new Book(106L, "Refactoring"));
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public long countBorrowedByMemberId(Long memberId) {
        return books.values().stream()
                .filter(book -> memberId.equals(
                        book.getBorrowedByMemberId()))
                .count();
    }

    public Collection<Book> findAll() {
        return books.values();
    }
}