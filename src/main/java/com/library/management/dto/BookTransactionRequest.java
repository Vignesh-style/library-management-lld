package com.library.management.dto;

import java.io.Serializable;

public class BookTransactionRequest implements Serializable {
    private Long bookId;
    private Long userId;

    public BookTransactionRequest() {} // Default constructor for deserialization

    public BookTransactionRequest(Long bookId, Long userId) {
        this.bookId = bookId;
        this.userId = userId;
    }

    // Getters
    public Long getBookId() { return bookId; }
    public Long getUserId() { return userId; }
}