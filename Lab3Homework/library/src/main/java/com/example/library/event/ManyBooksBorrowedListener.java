package com.example.library.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ManyBooksBorrowedListener {

    private static final Logger log =
            LoggerFactory.getLogger(
                    ManyBooksBorrowedListener.class);

    @EventListener
    public void handleManyBooksBorrowed(
            ManyBooksBorrowedEvent event) {

        log.warn(
                "ALERT: {} has borrowed {} books (member ID: {})",
                event.memberName(),
                event.borrowedCount(),
                event.memberId()
        );
    }
}