package com.example.library.policy;

import com.example.library.model.Book;
import com.example.library.model.Member;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

@Component("drools")
public class DroolsBorrowingPolicy implements BorrowingPolicy {
    private final KieContainer container;

    public DroolsBorrowingPolicy(KieContainer container) {
        this.container = container;
    }

    @Override
    public int getBorrowingLimit(Member member) {
        // Limits are defined by DRL, not Java. This method exists for legacy policies.
        throw new UnsupportedOperationException("Drools limits are evaluated through rules.");
    }

    @Override
    public BorrowingDecision evaluate(Member member, Book book, int currentCount) {
        BorrowingDecision decision = new BorrowingDecision();
        BorrowingRequest request = new BorrowingRequest(member, book, currentCount);
        KieSession session = container.newKieSession();
        try {
            session.insert(request);
            session.insert(decision);
            session.fireAllRules();
        } finally {
            session.dispose();
        }
        return decision;
    }
}
