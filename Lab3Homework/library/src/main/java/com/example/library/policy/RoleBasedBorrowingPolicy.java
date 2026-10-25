package com.example.library.policy;

import com.example.library.model.Member;
import com.example.library.model.MemberRole;
import org.springframework.stereotype.Component;

@Component("roleBased")
public class RoleBasedBorrowingPolicy implements BorrowingPolicy {

    @Override
    public int getBorrowingLimit(Member member) {
        return switch (member.getRole()) {
            case STUDENT -> 2;
            case REGULAR -> 3;
            case PREMIUM -> 5;
        };
    }
}