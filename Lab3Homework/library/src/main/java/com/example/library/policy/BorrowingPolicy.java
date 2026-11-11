package com.example.library.policy;

import com.example.library.model.Book;
import com.example.library.model.Member;

public interface BorrowingPolicy {
    int getBorrowingLimit(Member member);

    default BorrowingDecision evaluate(Member member, Book book, int currentCount) {
        BorrowingDecision result = new BorrowingDecision();
        if (!book.isAvailable()) {
            result.reject("Book is unavailable: " + book.getTitle());
        } else if (book.getType() == com.example.library.model.BookType.REFERENCE) {
            result.reject("Reference books require the Drools borrowing policy.");
        } else if (currentCount >= getBorrowingLimit(member)) {
            result.reject(member.getName() + " has reached the borrowing limit of " + getBorrowingLimit(member));
        } else {
            result.approve(14);
        }
        return result;
    }
}
