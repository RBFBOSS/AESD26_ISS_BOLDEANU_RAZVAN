package com.example.library.policy;

import com.example.library.model.Book;
import com.example.library.model.Member;

public class BorrowingRequest {
    private final Member member;
    private final Book book;
    private final int currentCount;

    public BorrowingRequest(Member member, Book book, int currentCount) {
        this.member = member;
        this.book = book;
        this.currentCount = currentCount;
    }
    public Member getMember() { return member; }
    public Book getBook() { return book; }
    public int getCurrentCount() { return currentCount; }
}
