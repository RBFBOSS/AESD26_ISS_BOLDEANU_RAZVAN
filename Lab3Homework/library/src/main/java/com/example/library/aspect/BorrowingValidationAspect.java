package com.example.library.aspect;

import com.example.library.exception.BorrowingException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BorrowingValidationAspect {
    private final MemberRepository members;
    private final BookRepository books;

    public BorrowingValidationAspect(MemberRepository members, BookRepository books) {
        this.members = members;
        this.books = books;
    }

    @Before("execution(* com.example.library.service.BorrowingService.borrowBook(..)) && args(memberId, bookId)")
    public void validateBorrowing(Long memberId, Long bookId) {
        if (members.findById(memberId).isEmpty()) {
            throw new BorrowingException("Member not found: " + memberId);
        }
        if (books.findById(bookId).isEmpty()) {
            throw new BorrowingException("Book not found: " + bookId);
        }
        // Business-rule validation is delegated to the selected BorrowingPolicy.
    }
}
