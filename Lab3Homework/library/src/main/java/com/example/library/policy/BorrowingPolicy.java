package com.example.library.policy;

import com.example.library.model.Member;

public interface BorrowingPolicy {

    int getBorrowingLimit(Member member);

    default boolean canBorrow(Member member, int currentCount) {
        return currentCount < getBorrowingLimit(member);
    }
}