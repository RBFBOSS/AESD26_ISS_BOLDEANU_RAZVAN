package com.example.library.aspect;

import com.example.library.exception.BorrowingException;
import com.example.library.model.Book;
import com.example.library.model.Member;
import com.example.library.policy.BorrowingPolicy;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BorrowingValidationAspect {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowingPolicy borrowingPolicy;

    public BorrowingValidationAspect(
            MemberRepository memberRepository,
            BookRepository bookRepository,
            BorrowingPolicy borrowingPolicy) {

        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.borrowingPolicy = borrowingPolicy;
    }

    @Before(
            "execution(* com.example.library.service.BorrowingService.borrowBook(..))"
                    + " && args(memberId, bookId)"
    )
    public void validateBorrowing(
            Long memberId, Long bookId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new BorrowingException(
                                "Member not found: " + memberId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BorrowingException(
                                "Book not found: " + bookId));

        if (!book.isAvailable()) {
            throw new BorrowingException(
                    "Book is unavailable: " + book.getTitle());
        }

        long currentCount =
                bookRepository.countBorrowedByMemberId(memberId);

        if (!borrowingPolicy.canBorrow(
                member, (int) currentCount)) {

            throw new BorrowingException(
                    member.getName()
                            + " has reached the borrowing limit of "
                            + borrowingPolicy.getBorrowingLimit(member));
        }
    }
}