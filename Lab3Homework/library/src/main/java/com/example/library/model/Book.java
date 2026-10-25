package com.example.library.model;

public class Book {
    private final Long id;
    private final String title;
    private Long borrowedByMemberId;

    public Book(Long id, String title) {
        this.id = id;
        this.title = title;
        this.borrowedByMemberId = null;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return borrowedByMemberId == null;
    }

    public Long getBorrowedByMemberId() {
        return borrowedByMemberId;
    }

    public void borrow(Long memberId) {
        this.borrowedByMemberId = memberId;
    }

    public void returnBook() {
        this.borrowedByMemberId = null;
    }
}