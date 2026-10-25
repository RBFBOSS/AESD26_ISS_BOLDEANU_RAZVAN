package com.example.library.event;

public record ManyBooksBorrowedEvent(
        Long memberId,
        String memberName,
        long borrowedCount
) {}