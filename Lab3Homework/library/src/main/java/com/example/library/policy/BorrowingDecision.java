package com.example.library.policy;

public class BorrowingDecision {
    private boolean allowed;
    private int borrowingDays;
    private String reason = "No borrowing rule matched.";
    private boolean resolved;

    public boolean isAllowed() { return allowed; }
    public int getBorrowingDays() { return borrowingDays; }
    public String getReason() { return reason; }
    public boolean isResolved() { return resolved; }
    public void approve(int days) {
        allowed = true;
        borrowingDays = days;
        reason = "Approved";
        resolved = true;
    }
    public void reject(String message) {
        allowed = false;
        borrowingDays = 0;
        reason = message;
        resolved = true;
    }
}
