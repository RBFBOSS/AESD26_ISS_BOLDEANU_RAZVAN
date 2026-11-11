package com.example.library.service;

import com.example.library.event.ManyBooksBorrowedEvent;
import com.example.library.exception.BorrowingException;
import com.example.library.model.Book;
import com.example.library.model.Member;
import com.example.library.policy.BorrowingDecision;
import com.example.library.policy.BorrowingPolicy;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BorrowingService {
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowingPolicy borrowingPolicy;
    private final ApplicationEventPublisher eventPublisher;
    private final int eventThreshold;

    public BorrowingService(MemberRepository memberRepository,
                            BookRepository bookRepository,
                            BorrowingPolicy borrowingPolicy,
                            ApplicationEventPublisher eventPublisher,
                            @Value("${library.event-threshold:3}") int eventThreshold) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.borrowingPolicy = borrowingPolicy;
        this.eventPublisher = eventPublisher;
        this.eventThreshold = eventThreshold;
    }

    public LocalDate borrowBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BorrowingException("Member not found: " + memberId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BorrowingException("Book not found: " + bookId));

        int count = Math.toIntExact(bookRepository.countBorrowedByMemberId(memberId));
        BorrowingDecision decision = borrowingPolicy.evaluate(member, book, count);
        if (!decision.isAllowed()) {
            throw new BorrowingException(decision.getReason());
        }
        book.borrow(memberId, decision.getBorrowingDays());
        long newCount = bookRepository.countBorrowedByMemberId(memberId);
        if (newCount > eventThreshold) {
            eventPublisher.publishEvent(new ManyBooksBorrowedEvent(
                    member.getId(), member.getName(), newCount));
        }
        return book.getDueDate();
    }

    public void returnBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BorrowingException("Member not found: " + memberId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BorrowingException("Book not found: " + bookId));
        if (book.isAvailable() || !memberId.equals(book.getBorrowedByMemberId())) {
            throw new BorrowingException(member.getName() + " has not borrowed " + book.getTitle());
        }
        book.returnBook();
    }
}
