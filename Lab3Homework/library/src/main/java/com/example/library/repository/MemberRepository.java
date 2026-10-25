package com.example.library.repository;

import com.example.library.model.Member;
import com.example.library.model.MemberRole;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemberRepository {

    private final Map<Long, Member> members =
            new ConcurrentHashMap<>();

    public MemberRepository() {
        members.put(1L,
                new Member(1L, "Alice", MemberRole.STUDENT));
        members.put(2L,
                new Member(2L, "Bob", MemberRole.REGULAR));
        members.put(3L,
                new Member(3L, "Charlie", MemberRole.PREMIUM));
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(members.get(id));
    }
}