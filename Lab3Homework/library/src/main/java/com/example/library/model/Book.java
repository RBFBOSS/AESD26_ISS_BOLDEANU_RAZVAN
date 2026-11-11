package com.example.library.model;

import java.time.LocalDate;

public class Book {
    private final Long id;
    private final String title;
    private final BookType type;
    private Long borrowedByMemberId;
    private LocalDate dueDate;

    public Book(Long id, String title) { this(id, title, BookType.REGULAR); }
    public Book(Long id, String title, BookType type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public BookType getType() { return type; }
    public boolean isAvailable() { return borrowedByMemberId == null; }
    public Long getBorrowedByMemberId() { return borrowedByMemberId; }
    public LocalDate getDueDate() { return dueDate; }
    public void borrow(Long memberId, int days) {
        borrowedByMemberId = memberId;
        dueDate = LocalDate.now().plusDays(days);
    }
    public void returnBook() {
        borrowedByMemberId = null;
        dueDate = null;
    }
}
