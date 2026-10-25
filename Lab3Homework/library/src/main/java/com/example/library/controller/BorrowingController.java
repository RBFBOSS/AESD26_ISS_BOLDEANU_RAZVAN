package com.example.library.controller;

import com.example.library.exception.BorrowingException;
import com.example.library.service.BorrowingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(
            BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("/{memberId}/books/{bookId}")
    public ResponseEntity<String> borrowBook(
            @PathVariable Long memberId,
            @PathVariable Long bookId) {

        borrowingService.borrowBook(memberId, bookId);

        return ResponseEntity.ok(
                "Book borrowed successfully.");
    }

    @DeleteMapping("/{memberId}/books/{bookId}")
    public ResponseEntity<String> returnBook(
            @PathVariable Long memberId,
            @PathVariable Long bookId) {

        borrowingService.returnBook(memberId, bookId);

        return ResponseEntity.ok(
                "Book returned successfully.");
    }

    @ExceptionHandler(BorrowingException.class)
    public ResponseEntity<String> handleBorrowingException(
            BorrowingException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}