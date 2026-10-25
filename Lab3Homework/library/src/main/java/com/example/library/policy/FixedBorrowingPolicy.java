package com.example.library.policy;

import com.example.library.model.Member;
import org.springframework.stereotype.Component;

@Component("fixed")
public class FixedBorrowingPolicy implements BorrowingPolicy {

    private static final int LIMIT = 3;

    @Override
    public int getBorrowingLimit(Member member) {
        return LIMIT;
    }
}