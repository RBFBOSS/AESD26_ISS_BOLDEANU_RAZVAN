package com.example.library.service;

import com.example.library.event.ManyBooksBorrowedEvent;
import com.example.library.exception.BorrowingException;
import com.example.library.model.Book;
import com.example.library.model.Member;
import com.example.library.policy.BorrowingPolicy;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class BorrowingService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowingPolicy borrowingPolicy;
    private final ApplicationEventPublisher eventPublisher;
    private final int eventThreshold;

    public BorrowingService(
            MemberRepository memberRepository,
            BookRepository bookRepository,
            BorrowingPolicy borrowingPolicy,
            ApplicationEventPublisher eventPublisher,
            @Value("${library.event-threshold:3}")
            int eventThreshold) {

        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.borrowingPolicy = borrowingPolicy;
        this.eventPublisher = eventPublisher;
        this.eventThreshold = eventThreshold;
    }

    public void borrowBook(Long memberId, Long bookId) {
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
                    "Book is already borrowed: " + book.getTitle());
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

        book.borrow(memberId);

        long newCount =
                bookRepository.countBorrowedByMemberId(memberId);

        if (newCount > eventThreshold) {
            eventPublisher.publishEvent(
                    new ManyBooksBorrowedEvent(
                            member.getId(),
                            member.getName(),
                            newCount
                    )
            );
        }
    }

    public void returnBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new BorrowingException(
                                "Member not found: " + memberId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new BorrowingException(
                                "Book not found: " + bookId));

        if (book.isAvailable()
                || !memberId.equals(
                book.getBorrowedByMemberId())) {

            throw new BorrowingException(
                    member.getName()
                            + " has not borrowed " + book.getTitle());
        }

        book.returnBook();
    }
}