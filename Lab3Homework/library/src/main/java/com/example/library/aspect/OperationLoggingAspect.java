package com.example.library.aspect;

import com.example.library.model.Book;
import com.example.library.model.Member;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OperationLoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(
                    OperationLoggingAspect.class);

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public OperationLoggingAspect(
            MemberRepository memberRepository,
            BookRepository bookRepository) {

        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
    }

    @Around(
            "execution(* com.example.library.service.BorrowingService.borrowBook(..))"
                    + " || execution(* com.example.library.service.BorrowingService.returnBook(..))"
    )
    public Object logOperation(
            ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.nanoTime();

        Object[] args = joinPoint.getArgs();
        Long memberId = (Long) args[0];
        Long bookId = (Long) args[1];

        try {
            return joinPoint.proceed();
        } finally {
            long elapsedNanos =
                    System.nanoTime() - start;

            String memberName = memberRepository
                    .findById(memberId)
                    .map(Member::getName)
                    .orElse("Unknown member");

            String bookTitle = bookRepository
                    .findById(bookId)
                    .map(Book::getTitle)
                    .orElse("Unknown book");

            log.info(
                    "Method: {}, Member: {}, Book: {}, Execution time: {} ns",
                    joinPoint.getSignature().getName(),
                    memberName,
                    bookTitle,
                    elapsedNanos
            );
        }
    }
}