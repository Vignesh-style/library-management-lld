package com.library.management.models;

import java.util.Date;

public class Transaction {
    public enum Status { BORROWED, RETURNED }

    private Long id;
    private Long bookId;
    private Long userId;
    private long borrowDate; // Timestamp
    private long returnDate; // Timestamp, 0 if not yet returned
    private Status status;

    public Transaction(Long id, Long bookId, Long userId, long borrowDate) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.borrowDate = borrowDate;
        this.returnDate = 0;
        this.status = Status.BORROWED;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Long getBookId() { return bookId; }
    public Long getUserId() { return userId; }
    public long getBorrowDate() { return borrowDate; }
    public long getReturnDate() { return returnDate; }
    public Status getStatus() { return status; }
    public void setReturnDate(long returnDate) { this.returnDate = returnDate; }
    public void setStatus(Status status) { this.status = Status.RETURNED; }

    // Use a copy constructor for updating
    public Transaction(Transaction other) {
        this.id = other.id;
        this.bookId = other.bookId;
        this.userId = other.userId;
        this.borrowDate = other.borrowDate;
        this.returnDate = other.returnDate;
        this.status = other.status;
    }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", bookId=" + bookId + ", userId=" + userId +
               ", status=" + status + ", borrowDate=" + new Date(borrowDate) + '}';
    }
}